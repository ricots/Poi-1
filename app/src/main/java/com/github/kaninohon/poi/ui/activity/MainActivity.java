package com.github.kaninohon.poi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.event.TagSelectedEvent;
import com.github.kaninohon.poi.model.BusHolder;
import com.github.kaninohon.poi.model.FeedlyProfile;
import com.github.kaninohon.poi.ui.adapter.ViewPagerAdapter;
import com.github.kaninohon.poi.ui.fragment.RssItemFragment;
import com.github.kaninohon.poi.ui.fragment.TagFragment;
import com.github.kaninohon.poi.ui.widget.CircleTransform;
import com.github.kaninohon.poi.utils.DialogHelper;
import com.github.kaninohon.poi.utils.DoubleClickExitHelper;
import com.github.kaninohon.poi.utils.FileUtils;
import com.github.kaninohon.poi.utils.SharedPreferManager;
import com.github.kaninohon.poi.utils.UiHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ToolbarActivity{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private DoubleClickExitHelper mDoubleClickExitHelper;
    private static final int LOGIN_REQUEST = 1;
    private ImageView avatar;
    private TextView userName;
    private FloatingActionButton fab;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private Menu mMenu;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = $(R.id.user_name);
        mViewPager = $(R.id.viewpager);
        avatar = $(R.id.avatar);
        mDrawerLayout = $(R.id.drawer_layout);

        mDoubleClickExitHelper = new DoubleClickExitHelper(this);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLogin();
            }
        });

        mViewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), getFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        BusHolder.get().register(this);

        setupNavigationView();
        setupTabLayout();
        setupFab();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BusHolder.get().unregister(this);
    }

    private void setupFab() {
        fab = $(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mTabLayout.getSelectedTabPosition();
                if (position == 0 || position == 1) {
                    RssItemFragment currentFragment = (RssItemFragment) mViewPagerAdapter.getItem(position);
                    currentFragment.refreshToFirstPage();
                } else if (position == 2) {
                    TagFragment currentFragment = (TagFragment) mViewPagerAdapter.getItem(position);
                    currentFragment.refreshTag();
                }
            }
        });
    }

    private void setupNavigationView() {
        mNavigationView = $(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawer(mNavigationView);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_settings: {
                        Intent intent = new Intent(MainActivity.this, ToobarBackActivity.class);
                        intent.putExtra(ToobarBackActivity.EXTRA_START_FRAGMENT, ToobarBackActivity.SETTINGS_FRAGMENT);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.navigation_item_about: {
                        Intent intent = new Intent(MainActivity.this, ToobarBackActivity.class);
                        intent.putExtra(ToobarBackActivity.EXTRA_START_FRAGMENT, ToobarBackActivity.ABOUT_FRAGMENT);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void setupTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentItem = tab.getPosition();
                mViewPager.setCurrentItem(currentItem);
                if (currentItem != 0) {
                    mMenu.getItem(0).setVisible(false);
                } else {
                    mMenu.getItem(0).setVisible(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case LOGIN_REQUEST:
                mDrawerLayout.closeDrawer(mNavigationView);
                if(resultCode == FeedlyCloudApi.LOGIN_OK){
                    getProfile();
                } else {
                    Snackbar.make($(R.id.coordinator_layout), "登录失败", Snackbar.LENGTH_LONG)
                            .setAction("重试", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    gotoLogin();
                                }
                            })
                            .show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                mDrawerLayout.openDrawer(mNavigationView);
                return true;
            case R.id.action_set_as_read: {
                DialogHelper.getConfirmDialog(this, "是否把条目都标为已读", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RssItemFragment currentFragment = (RssItemFragment)mViewPagerAdapter.getItem(0);
                        currentFragment.setItemReceivedAsRead();
                    }
                }).show();
                return true;
            }
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, ToobarBackActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ToobarBackActivity.EXTRA_START_FRAGMENT, ToobarBackActivity.SEARCH_FRAGMENT);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDoubleClickExitHelper.onKeyDown(keyCode, event);
    }

    private void gotoLogin(){
        if(SharedPreferManager.getInstance().hasLogin()) {
            logout();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ToobarBackActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ToobarBackActivity.EXTRA_START_FRAGMENT, ToobarBackActivity.WEBVIEW_FRAGMENT);
        intent.putExtras(bundle);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    public void logout(){
        DialogHelper.getConfirmDialog(this, "确定要退出吗", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferManager.getInstance().saveUserId("");
                SharedPreferManager.getInstance().saveAuthString("");
                SharedPreferManager.getInstance().saveUserProfile("", "");
                mDrawerLayout.closeDrawer(mNavigationView);
                userName.setText("点击头像登录");
                avatar.setImageResource(R.drawable.ic_avatar);
                //FileUtils.clearAppCache(MainActivity.this);
                // 清除cookie即可彻底清除缓存
                    CookieSyncManager.createInstance(MainActivity.this);
                    CookieManager.getInstance().removeAllCookie();

                UiHelper.recyclerviewShowTip(MainActivity.this, "退出登录成功", Snackbar.LENGTH_LONG);
            }
        }).show();
    }





    private void getProfile(){
        FeedlyCloudApi.getFeedlyProfile(new Callback<FeedlyProfile>() {

            @Override
            public void success(FeedlyProfile feedlyProfile, Response response) {
                Picasso.with(MainActivity.this).load(feedlyProfile.getPicture())
                        .placeholder(R.drawable.ic_avatar).transform(new CircleTransform())
                        .into(avatar);
                userName.setText(feedlyProfile.getEmail());
                SharedPreferManager.getInstance().saveUserProfile(feedlyProfile.getEmail(), feedlyProfile.getPicture());
                resetViewpager();
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make($(R.id.coordinator_layout), "登录失败", Snackbar.LENGTH_LONG)
                        .setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gotoLogin();
                            }
                        })
                        .show();
            }
        });
    }

    private void resetViewpager(){
        mViewPager.setCurrentItem(0);
        RssItemFragment currentFragment = (RssItemFragment)mViewPagerAdapter.getItem(0);
        currentFragment.refreshToFirstPage();
    }

    //标签页的标签被选中之后调用这里
    @Subscribe
    public void onTagSelected(TagSelectedEvent event) {
        mViewPager.setCurrentItem(0);
        RssItemFragment currentFragment = (RssItemFragment)mViewPagerAdapter.getItem(0);
        currentFragment.refreshTagStream(event.getTag());
    }

}
