package com.github.kaninohon.poi.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.ui.fragment.RssItemFragment;
import com.github.kaninohon.poi.ui.fragment.TagFragment;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> mPageFragments = new ArrayList<>();
	private String[] mTitles;

	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mTitles = context.getResources().getStringArray(R.array.tabs);
		{
			RssItemFragment all = new RssItemFragment();
			Bundle bundle = new Bundle();
			bundle.putString(RssItemFragment.EXTRA_ITEM_TYPE,RssItemFragment.EXTRA_ALL_ITEM);
			all.setArguments(bundle);
			mPageFragments.add(all);
		}
		{
			RssItemFragment star = new RssItemFragment();
			Bundle bundle = new Bundle();
			bundle.putString(RssItemFragment.EXTRA_ITEM_TYPE,RssItemFragment.EXTRA_STAR_ITEM);
			star.setArguments(bundle);
			mPageFragments.add(star);
		}

		mPageFragments.add(new TagFragment());
	}

	@Override
	public Fragment getItem(int position) {

		return mPageFragments.get(position);
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}

}
