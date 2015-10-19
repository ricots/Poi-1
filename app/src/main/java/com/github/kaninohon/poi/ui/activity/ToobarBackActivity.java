package com.github.kaninohon.poi.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.ui.fragment.AboutFragment;
import com.github.kaninohon.poi.ui.fragment.SearchFragment;
import com.github.kaninohon.poi.ui.fragment.SettingsFragment;
import com.github.kaninohon.poi.ui.fragment.WebViewFragment;
import com.github.kaninohon.poi.utils.DeviceUtils;

public class ToobarBackActivity extends ToolbarActivity {

    public static final String EXTRA_START_FRAGMENT = "EXTRA_START_FRAGMENT";
    public static final String WEBVIEW_FRAGMENT = "WEBVIEW_FRAGMENT";
    public static final String SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT";
    public static final String ABOUT_FRAGMENT = "ABOUT_FRAGMENT";
    public static final String SEARCH_FRAGMENT = "SEARCH_FRAGMENT";
    public static final String EXTRA_RSS_CONTENT_TITLE = "EXTRA_RSS_CONTENT_TITLE";

    private Fragment mFragmnet;
    private String startType;
    private SearchView mSearchView;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_toolbar_back;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        startType = intent.getStringExtra(EXTRA_START_FRAGMENT);
        if(startType.equals(WEBVIEW_FRAGMENT)) {
            Bundle bundle = getBundle(intent);
            mFragmnet = new WebViewFragment();
            mFragmnet.setArguments(bundle);
            String rssContent = intent.getStringExtra(WebViewFragment.EXTRA_RSS_CONTENT);
            if(rssContent == null)
                setToolbarTitle("登录");
            else{
                String title = intent.getStringExtra(ToobarBackActivity.EXTRA_RSS_CONTENT_TITLE);
                setToolbarTitle(title);
            }
        }else if(startType.equals(ABOUT_FRAGMENT)){
            mFragmnet = new AboutFragment();
            setToolbarTitle("关于");
        }else if(startType.equals(SETTINGS_FRAGMENT)){
            mFragmnet = new SettingsFragment();
            setToolbarTitle("设置");
        }else if(startType.equals(SEARCH_FRAGMENT)){
            mFragmnet = new SearchFragment();
            setToolbarTitle("搜索");
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mFragmnet)
                .commit();
    }

    private Bundle getBundle(Intent intent){
        Bundle bundle = new Bundle();
        String rssContent = intent.getStringExtra(WebViewFragment.EXTRA_RSS_CONTENT);
        if(rssContent == null){
            //登录
            bundle.putString(WebViewFragment.EXTRA_ACTION_TYPE, WebViewFragment.ACTION_LOGIN);
        }else {
            //打开RSS内容详情
            bundle.putString(WebViewFragment.EXTRA_ACTION_TYPE, WebViewFragment.ACTION_OPEN_RSS_ITEM_DETAIL);
            bundle.putString(WebViewFragment.EXTRA_RSS_CONTENT, rssContent);
        }
        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(startType.equals(SEARCH_FRAGMENT)) {
            getMenuInflater().inflate(R.menu.menu_search, menu);
            MenuItem search = menu.findItem(R.id.search_view);
            mSearchView = (SearchView) search.getActionView();
            mSearchView.setIconifiedByDefault(false);
            mSearchView.setFocusable(true);
            mSearchView.setIconified(false);
            mSearchView.requestFocusFromTouch();
            setupSearchView();
        }
        return true;
    }

    private void setupSearchView(){
        mSearchView.setQueryHint("输入关键字");
        TextView textView = (TextView) mSearchView.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                DeviceUtils.hideSoftKeyboard(mSearchView);
                ((SearchFragment)mFragmnet).search(arg0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                return false;
            }
        });
        mSearchView.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                if (mFragmnet instanceof WebViewFragment) {
                    ((WebViewFragment)mFragmnet).WebViewBack();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mFragmnet instanceof WebViewFragment) {
                        ((WebViewFragment)mFragmnet).WebViewBack();
                        return true;
                    }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
