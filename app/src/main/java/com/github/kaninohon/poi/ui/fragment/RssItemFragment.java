package com.github.kaninohon.poi.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.model.BusHolder;
import com.github.kaninohon.poi.model.FeedlyStream;
import com.github.kaninohon.poi.model.Item;
import com.github.kaninohon.poi.ui.adapter.RecyclerViewAdapter;
import com.github.kaninohon.poi.ui.widget.CircleTransform;
import com.github.kaninohon.poi.ui.widget.ContextMenuRecyclerView;
import com.github.kaninohon.poi.utils.SharedPreferManager;
import com.github.kaninohon.poi.utils.UiHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RssItemFragment  extends PageLoadFragment{
    public static final String EXTRA_ITEM_TYPE = "EXTRA_ITEM_TYPE";
    public static final String EXTRA_ALL_ITEM = "EXTRA_ALL_ITEM";
    public static final String EXTRA_STAR_ITEM = "EXTRA_STAR_ITEM";
    public static final String EXTRA_SOURCE_STREAM = "EXTRA_SOURCE_STREAM";
    public static final String FEED_ID = "FEED_ID";

    private ContextMenuRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FeedlyStream feedlyStream;
    private RecyclerViewAdapter mAdapter;
    private String extraType;
    private Callback<FeedlyStream> firstPageRefreshCallback,nextPageRefreshCallback;
    private String feedId;

    @Override
    protected int provideContentViewId() {
        return R.layout.view_refresh;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        extraType = getArguments().getString(EXTRA_ITEM_TYPE);
        if(extraType == null)
            return;

        if(extraType.equals(EXTRA_SOURCE_STREAM))
            feedId = getArguments().getString(FEED_ID);

        if(SharedPreferManager.getInstance().hasLogin() && extraType.equals(EXTRA_ALL_ITEM)){
            ImageView avatar = (ImageView)getActivity().findViewById(R.id.avatar);
            Picasso.with(getActivity()).load(SharedPreferManager.getInstance().getAvatarUrl())
                    .transform(new CircleTransform())
                    .into(avatar);
            TextView userName = (TextView)getActivity().findViewById(R.id.user_name);
            userName.setText(SharedPreferManager.getInstance().getUserEmail());
        }

        //确保只有viewpager的第0页在刷新，不要预加载其他页面的数据
        if(extraType.equals(EXTRA_ALL_ITEM) || extraType.equals(EXTRA_SOURCE_STREAM)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshToFirstPage();
                        }
                    });
                }
            }, 250);
        }

        setupRecyclerView();
        setupFirstPageRefreshCallback();
        setupNextPageRefreshCallback();
    }

    private void setupNextPageRefreshCallback() {
        nextPageRefreshCallback = new Callback<FeedlyStream>() {
            @Override
            public void success(FeedlyStream feedlyStream, Response response) {
                List<Item> rssItems = feedlyStream.getItems();
                for(Item item : rssItems){
                    if(extraType.equals(EXTRA_STAR_ITEM))
                        item.setIsStar(true);
                    else
                        item.setIsStar(false);
                }
                RssItemFragment.this.feedlyStream.setContinuation(feedlyStream.getContinuation());
                boolean hasAdd = onRefreshNextPageSuccess(RssItemFragment.this.feedlyStream.getItems(),
                        feedlyStream.getItems());
                if(hasAdd)
                    mAdapter.notifyDataSetChanged();
                else
                    UiHelper.recyclerviewShowTip(RssItemFragment.this, "已到最后一页", Snackbar.LENGTH_LONG);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                onRefreshFail();
                mSwipeRefreshLayout.setRefreshing(false);

                if(UiHelper.isAuthExpired(getActivity(),error))
                    return;
                UiHelper.recyclerviewShowTip(RssItemFragment.this, "内容加载失败，请稍后重试", Snackbar.LENGTH_LONG);
            }
        };
    }

    private void setupFirstPageRefreshCallback() {
        firstPageRefreshCallback = new Callback<FeedlyStream>() {
            @Override
            public void success(FeedlyStream feedlyStream, Response response) {
                List<Item> rssItems = feedlyStream.getItems();
                for(Item item : rssItems){
                    if(extraType.equals(EXTRA_STAR_ITEM))
                        item.setIsStar(true);
                    else
                        item.setIsStar(false);
                }
                RssItemFragment.this.feedlyStream.setContinuation(feedlyStream.getContinuation());
                onRefreshFirstPageSuccess(RssItemFragment.this.feedlyStream.getItems(), feedlyStream.getItems());
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                if(RssItemFragment.this.feedlyStream.getItems().size() == 0)
                    UiHelper.recyclerviewShowTip(RssItemFragment.this, "暂时没有新内容，请稍后重试", Snackbar.LENGTH_LONG);
            }

            @Override
            public void failure(RetrofitError error) {
                onRefreshFail();
                mSwipeRefreshLayout.setRefreshing(false);
                if(UiHelper.isAuthExpired(getActivity(),error))
                    return;
                UiHelper.recyclerviewShowTip(RssItemFragment.this, "内容加载失败，请稍后重试", Snackbar.LENGTH_LONG);
            }
        };
    }

    @Override
    protected void onTheRefresh() {
        refreshToFirstPage();
    }

    private void setupRecyclerView(){
        mRecyclerView = $(R.id.recycler_view);
        mRecyclerView.setOnCreateContextMenuListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        feedlyStream = new FeedlyStream();
        feedlyStream.setItems(new ArrayList<Item>());
        mAdapter = new RecyclerViewAdapter(feedlyStream, getActivity(), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                //滑动到底端，并且是向下滑动
                if (dy > 0 && !mSwipeRefreshLayout.isRefreshing() &&
                        mLayoutManager.findLastCompletelyVisibleItemPosition() == feedlyStream.getItems().size() - 1) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    refreshNextPage();
                }
            }
        });
    }

    @Override
    protected void refreshNextPage() {
        super.refreshNextPage();

        if(extraType.equals(EXTRA_ALL_ITEM))
            FeedlyCloudApi.getCategoryStream("global.all", true, feedlyStream.getContinuation(), nextPageRefreshCallback);
        else if(extraType.equals(EXTRA_STAR_ITEM))
            FeedlyCloudApi.getTagStream("global.saved", feedlyStream.getContinuation(), nextPageRefreshCallback);
        else if(extraType.equals(EXTRA_SOURCE_STREAM))
            FeedlyCloudApi.getSourceStream(feedId, false, feedlyStream.getContinuation(), nextPageRefreshCallback);
    }

    @Override
    public void refreshToFirstPage(){
        super.refreshToFirstPage();

        mSwipeRefreshLayout.setRefreshing(true);
        RecyclerView rv = $(R.id.recycler_view);
        rv.smoothScrollToPosition(0);
        if(!ifLogin()){
            return;
        }

        if(extraType.equals(EXTRA_ALL_ITEM))
            FeedlyCloudApi.getCategoryStream("global.all", true, "", firstPageRefreshCallback);
        else if(extraType.equals(EXTRA_STAR_ITEM))
            FeedlyCloudApi.getTagStream("global.saved", "", firstPageRefreshCallback);
        else if(extraType.equals(EXTRA_SOURCE_STREAM))
            FeedlyCloudApi.getSourceStream(feedId, false, "", firstPageRefreshCallback);
    }

    public void refreshTagStream(String tag) {
        if(!ifLogin())
            return;

        feedlyStream.getItems().clear();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(true);
        FeedlyCloudApi.getTagStream(tag, "", firstPageRefreshCallback);
    }

    private boolean ifLogin(){
        if(!SharedPreferManager.getInstance().hasLogin()){
            mSwipeRefreshLayout.setRefreshing(false);
            UiHelper.recyclerviewShowTip(RssItemFragment.this, "尚未登录", Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    public void setItemReceivedAsRead(){
        if(mSwipeRefreshLayout.isRefreshing())
            return;

        ArrayList<String> itemIds = new ArrayList<>();
        List<Item> rssItems = feedlyStream.getItems();
        for(Item item : rssItems)
            itemIds.add(item.getId());

        mSwipeRefreshLayout.setRefreshing(true);
        FeedlyCloudApi.setRssItemsAsRead(itemIds, new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                UiHelper.recyclerviewShowTip(RssItemFragment.this, "同步成功", Snackbar.LENGTH_SHORT);
                refreshToFirstPage();
            }

            @Override
            public void failure(RetrofitError error) {
                mSwipeRefreshLayout.setRefreshing(false);

                if (UiHelper.isAuthExpired(getActivity(), error))
                    return;
                UiHelper.recyclerviewShowTip(RssItemFragment.this, "同步失败，请稍后重试", Snackbar.LENGTH_LONG);

            }
        });
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerContextMenuInfo info = (ContextMenuRecyclerView
                .RecyclerContextMenuInfo) item
                .getMenuInfo();

        mAdapter.onStarClick(info.viewHolder, info.position);
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {

        ContextMenuRecyclerView.RecyclerContextMenuInfo info = (ContextMenuRecyclerView
                .RecyclerContextMenuInfo) menuInfo;

        //这里貌似会进入两次，一次是长按，一次是本身自动触发
        if(info.viewHolder == null)
            return;
        if(info.viewHolder.feedlyStreamItem.isStar())
            menu.add(0, v.getId(), 0, "取消收藏");//groupId, itemId, order, title
        else
            menu.add(0, v.getId(), 0, "收藏该条目");
    }
}
