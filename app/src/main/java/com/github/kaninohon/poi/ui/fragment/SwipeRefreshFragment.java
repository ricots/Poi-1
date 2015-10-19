package com.github.kaninohon.poi.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.ui.activity.ToolbarActivity;

public class SwipeRefreshFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener{
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int provideContentViewId() {
        return 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout = $(R.id.swipe_refresh_layout);
        setupSwipeRefreshLayout();
    }

    private void setupSwipeRefreshLayout(){
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light
            );

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onTheRefresh();
                }
            });
        }
    }

    protected void onTheRefresh(){

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mSwipeRefreshLayout.setEnabled(i == 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppBarLayout appBarLayout = ((ToolbarActivity) getActivity()).getAppBar();
        if(appBarLayout != null)
            appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppBarLayout appBarLayout = ((ToolbarActivity) getActivity()).getAppBar();
        if(appBarLayout != null)
            appBarLayout.removeOnOffsetChangedListener(this);
    }
}
