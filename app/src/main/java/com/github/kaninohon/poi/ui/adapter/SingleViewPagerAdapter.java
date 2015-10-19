package com.github.kaninohon.poi.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.github.kaninohon.poi.ui.fragment.RssItemFragment;

import java.util.ArrayList;

/**
 * Created by kaninohon on 2015/9/22.
 */
public class SingleViewPagerAdapter extends FragmentPagerAdapter {

    private String feedId;

    public SingleViewPagerAdapter(String feedId, FragmentManager fm) {
        super(fm);

        this.feedId = feedId;
    }

    @Override
    public Fragment getItem(int position) {
        RssItemFragment fragment = new RssItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RssItemFragment.EXTRA_ITEM_TYPE, RssItemFragment.EXTRA_SOURCE_STREAM);
        bundle.putString(RssItemFragment.FEED_ID, feedId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
