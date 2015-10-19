package com.github.kaninohon.poi.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.kaninohon.poi.AppContext;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.event.RssSubscribeChangeEvent;
import com.github.kaninohon.poi.model.BusHolder;
import com.github.kaninohon.poi.model.Result;
import com.github.kaninohon.poi.model.SearchResult;
import com.github.kaninohon.poi.model.UserSubscription;
import com.github.kaninohon.poi.ui.adapter.RssSearchAdapter;
import com.github.kaninohon.poi.ui.widget.ContextMenuRecyclerView;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchFragment extends SwipeRefreshFragment {

    private ContextMenuRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SearchResult searchResult;
    private RssSearchAdapter mAdapter;
    private static final int MAX_ITEM_COUNT = 100;
    private String keyWord;
    private TextInputLayout errorTip;


    @Override
    protected int provideContentViewId() {
        return R.layout.view_refresh;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupRecyclerView();
        errorTip = $(R.id.text_input_layout);
        errorTip.setErrorEnabled(true);
    }

    private void setupRecyclerView(){
        mRecyclerView = $(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        searchResult = new SearchResult();
        searchResult.setResults(new ArrayList<Result>());
        mAdapter = new RssSearchAdapter(searchResult, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void search(String keyWord){
        this.keyWord = keyWord;
        if(mSwipeRefreshLayout.isRefreshing())
            return;

        search();
    }

    @Override
    protected void onTheRefresh() {
        super.onTheRefresh();

        if(keyWord == null || keyWord.equals("")) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        search();
    }

    private void search(){
        mRecyclerView.smoothScrollToPosition(0);
        mSwipeRefreshLayout.setRefreshing(true);
        errorTip.setVisibility(View.GONE);

        if(AppContext.getUserSubscriptions() == null){
            FeedlyCloudApi.getUserSubscription(new Callback<ArrayList<UserSubscription>>() {

                @Override
                public void success(ArrayList<UserSubscription> userSubscriptions, Response response) {
                    if (userSubscriptions == null || userSubscriptions.size() == 0)
                        AppContext.setUserSubscriptions(new ArrayList<UserSubscription>());
                    else
                        AppContext.setUserSubscriptions(userSubscriptions);
                    searchKeyWord(keyWord);
                }

                @Override
                public void failure(RetrofitError error) {
                    onSearchError("搜索连接失败，请稍后重试");
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        else
            searchKeyWord(keyWord);
    }

    private void searchKeyWord(String keyWord){
        FeedlyCloudApi.rssSearch(keyWord, MAX_ITEM_COUNT, new Callback<SearchResult>() {

            @Override
            public void success(SearchResult searchResult, Response response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (searchResult.getResults() == null || searchResult.getResults().size() == 0) {
                    onSearchError("抱歉，没有找到相关内容");
                    return;
                }
                SearchFragment.this.searchResult.getResults().clear();
                SearchFragment.this.searchResult.getResults().addAll(searchResult.getResults());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                onSearchError("连接失败，请稍后重试");
            }
        });
    }

    private void onSearchError(String tip){
        mSwipeRefreshLayout.setRefreshing(false);
        errorTip.setError(tip);
        errorTip.setVisibility(View.VISIBLE);
        SearchFragment.this.searchResult.getResults().clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BusHolder.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BusHolder.get().unregister(this);
    }

    @Subscribe
    public void onRssSourceSubscribed(RssSubscribeChangeEvent event) {
        RssSearchAdapter.ViewHolder viewHolder = mAdapter.getLastClickViewHolder();
        Result result = event.getResult();
        //先要修改searchResult的状态，免得再次进入详情页数据又不同步了
        for(Result tmp : searchResult.getResults()){
            if(tmp.getFeedId().equals(result.getFeedId()))
                tmp.setSubscribed(result.isSubscribed());
        }
        mAdapter.resetAddRemove(viewHolder, result);
     }

}
