package com.github.kaninohon.poi.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.kaninohon.poi.AppContext;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.event.RssSubscribeChangeEvent;
import com.github.kaninohon.poi.model.BusHolder;
import com.github.kaninohon.poi.model.Result;
import com.github.kaninohon.poi.ui.adapter.SingleViewPagerAdapter;
import com.github.kaninohon.poi.utils.UiHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RssSourceDetailActivity extends ToolbarActivity{
    public static final String EXTRA_DETAIL = "EXTRA_DETAIL";
    private CollapsingToolbarLayout collapsingToolbar;
    private Result resultFromSearch;
    private boolean processing = false;
    private Menu mMenu;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_rss_source_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        resultFromSearch = (Result) intent.getSerializableExtra(EXTRA_DETAIL);
        ImageView icon = $(R.id.icon);
        Picasso.with(this).load(resultFromSearch.getVisualUrl()).into(icon);
        ImageView cover = $(R.id.cover);
        Picasso.with(this).load(resultFromSearch.getCoverUrl()).into(cover);
        collapsingToolbar = $(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(resultFromSearch.getTitle());

        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewpager);
        SingleViewPagerAdapter mViewPagerAdapter = new SingleViewPagerAdapter(resultFromSearch.getFeedId(),
                getFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_rss_source_detail, menu);
        if(resultFromSearch.isSubscribed())
            menu.getItem(0).setIcon(R.drawable.ic_remove_white_24dp);
        else
            menu.getItem(0).setIcon(R.drawable.ic_add_white_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add: {
                if(resultFromSearch.isSubscribed())
                    unsubscribeRssSource();
                else
                    subscribeRssSource();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void subscribeRssSource(){
        if(processing)
            return;

        processing = true;
        UiHelper.recyclerviewShowTip(RssSourceDetailActivity.this, "同步中...", Snackbar.LENGTH_LONG);
        FeedlyCloudApi.rssSubscribe(resultFromSearch.getFeedId(), new retrofit.Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                processing = false;
                AppContext.addSubscription2App(resultFromSearch);
                resultFromSearch.setSubscribed(true);
                RssSubscribeChangeEvent event = new RssSubscribeChangeEvent(RssSubscribeChangeEvent.SUBSCRIBE_ADDED,
                        resultFromSearch);
                BusHolder.get().post(event);
            }

            @Override
            public void failure(RetrofitError error) {
                processing = false;
                if (UiHelper.isAuthExpired(RssSourceDetailActivity.this, error))
                    return;
                UiHelper.recyclerviewShowTip(RssSourceDetailActivity.this, "订阅失败，请稍后重试", Snackbar.LENGTH_LONG);
            }
        });
    }

    private void unsubscribeRssSource(){
        if(processing)
            return;

        processing = true;
        UiHelper.recyclerviewShowTip(RssSourceDetailActivity.this, "同步中...", Snackbar.LENGTH_LONG);
        FeedlyCloudApi.rssUnsubscribe(resultFromSearch.getFeedId(), new retrofit.Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                processing = false;
                AppContext.removeSubscription2App(resultFromSearch);
                resultFromSearch.setSubscribed(false);
                RssSubscribeChangeEvent event = new RssSubscribeChangeEvent(RssSubscribeChangeEvent.SUBSCRIBE_REMOVEED,
                        resultFromSearch);
                BusHolder.get().post(event);
            }

            @Override
            public void failure(RetrofitError error) {
                processing = false;
                if (UiHelper.isAuthExpired(RssSourceDetailActivity.this, error))
                    return;
                UiHelper.recyclerviewShowTip(RssSourceDetailActivity.this, "取消订阅失败，请稍后重试", Snackbar.LENGTH_LONG);
            }
        });
    }

    @Subscribe
    public void onRssSourceSubscribed(RssSubscribeChangeEvent event) {
        if(event.getActionType() == RssSubscribeChangeEvent.SUBSCRIBE_ADDED) {
            UiHelper.recyclerviewShowTip(RssSourceDetailActivity.this, "订阅成功", Snackbar.LENGTH_LONG);
            mMenu.getItem(0).setIcon(R.drawable.ic_remove_white_24dp);
        }
        else if(event.getActionType() == RssSubscribeChangeEvent.SUBSCRIBE_REMOVEED){
            UiHelper.recyclerviewShowTip(RssSourceDetailActivity.this, "取消订阅成功", Snackbar.LENGTH_LONG);
            mMenu.getItem(0).setIcon(R.drawable.ic_add_white_24dp);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        BusHolder.get().register(this);
    }

    @Override
    public void onPause() {
        BusHolder.get().unregister(this);

        super.onPause();
    }
}
