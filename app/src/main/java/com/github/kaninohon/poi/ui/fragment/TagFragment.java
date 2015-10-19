package com.github.kaninohon.poi.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.event.TagSelectedEvent;
import com.github.kaninohon.poi.model.BusHolder;
import com.github.kaninohon.poi.model.FeedlyTag;
import com.github.kaninohon.poi.ui.activity.MainActivity;
import com.github.kaninohon.poi.ui.widget.AutoWrapLayout;
import com.github.kaninohon.poi.utils.UiHelper;

import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TagFragment extends SwipeRefreshFragment implements View.OnClickListener{

    private LinearLayout mTagsLayout;
    private NestedScrollView nestedScrollView;
    public interface OnTagSelectedListener {
        public void onTagSelected(String tag);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_tag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTagsLayout = $(R.id.tags_layout);
        nestedScrollView = $(R.id.nested_scroll_view);
    }

    @Override
    protected void onTheRefresh() {
        super.onTheRefresh();

        getFeedlyTags();
    }

    public void refreshTag(){
        if(mSwipeRefreshLayout.isRefreshing())
            return;
        getFeedlyTags();
    }

    private void getFeedlyTags(){
        mSwipeRefreshLayout.setRefreshing(true);
        nestedScrollView.smoothScrollTo(0, 0);
        FeedlyCloudApi.getFeedlyUserTags(new Callback<ArrayList<FeedlyTag>>() {

            @Override
            public void success(ArrayList<FeedlyTag> tags, Response response) {
                for(int i = 0;i < tags.size();i++){
                    if (tags.get(i).getId().contains("global.saved")) {
                        tags.remove(tags.get(i));
                        break;
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
                addWTags(TagFragment.this, mTagsLayout, tags);
            }

            @Override
            public void failure(RetrofitError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                if(UiHelper.isAuthExpired(getActivity(), error))
                    return;
            }
        });
    }

    private void addWTags(final Fragment fragment,LinearLayout linearLayout, final ArrayList<FeedlyTag> tags){
        linearLayout.removeAllViews();
        int x = fragment.getActivity().getResources().getDimensionPixelSize(R.dimen.tag_margin_x);
        int y = fragment.getActivity().getResources().getDimensionPixelSize(R.dimen.tag_margin_y);

        LinearLayout tagGroupLayout = new LinearLayout(fragment.getActivity());
        tagGroupLayout.setOrientation(LinearLayout.HORIZONTAL);
        AutoWrapLayout tagLayout = new AutoWrapLayout(fragment.getActivity());

        TextView groupNameView = new TextView(fragment.getActivity());
        groupNameView.setText("标签： ");
        tagGroupLayout.addView(groupNameView);

        for (final FeedlyTag tag : tags) {
            TextView tagView = (TextView) View.inflate(fragment.getActivity().getApplicationContext(), R.layout.layout_tag, null);
            String tmpTag = tag.getId();
            tmpTag = tmpTag.substring(tmpTag.lastIndexOf("/") + 1,tmpTag.length());
            tagView.setText(tmpTag);
            tagView.setOnClickListener(this);
            AutoWrapLayout.LayoutParams alp = new AutoWrapLayout.LayoutParams();
            alp.setMargins(x, y, x, y);
            tagLayout.addView(tagView, alp);
        }
        tagGroupLayout.addView(tagLayout);
        linearLayout.addView(tagGroupLayout);
    }


    @Override
    public void onClick(View view) {
        String tag = ((TextView)view).getText().toString();
        BusHolder.get().post(new TagSelectedEvent(tag));
    }
}
