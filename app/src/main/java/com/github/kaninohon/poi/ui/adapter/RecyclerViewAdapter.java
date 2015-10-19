package com.github.kaninohon.poi.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.model.FeedlyStream;
import com.github.kaninohon.poi.model.Item;
import com.github.kaninohon.poi.ui.activity.ToobarBackActivity;
import com.github.kaninohon.poi.ui.fragment.WebViewFragment;
import com.github.kaninohon.poi.ui.widget.ContextMenuRecyclerView;
import com.github.kaninohon.poi.utils.DateUtils;
import com.github.kaninohon.poi.utils.UiHelper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private FeedlyStream feedlyStream;
    private Activity mActivity;
    private ContextMenuRecyclerView contextMenuRecyclerView;

    public RecyclerViewAdapter(FeedlyStream feedlyStream,Activity activity, ContextMenuRecyclerView contextMenuRecyclerView) {
        this.feedlyStream = feedlyStream;
        this.mActivity = activity;
        this.contextMenuRecyclerView = contextMenuRecyclerView;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.feedlyStreamItem = feedlyStream.getItems().get(position);
        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                contextMenuRecyclerView.saveContext(position,viewHolder);
                return false;
            }
        });
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String rssContent = "";
                if (viewHolder.feedlyStreamItem.getContent() != null) {
                    rssContent = viewHolder.feedlyStreamItem.getContent().getContent();
                } else {
                    if (viewHolder.feedlyStreamItem.getSummary() != null)
                        rssContent = viewHolder.feedlyStreamItem.getSummary().getContent();
                }

                bundle.putString(ToobarBackActivity.EXTRA_START_FRAGMENT, ToobarBackActivity.WEBVIEW_FRAGMENT);
                bundle.putString(WebViewFragment.EXTRA_RSS_CONTENT, rssContent);
                bundle.putString(ToobarBackActivity.EXTRA_RSS_CONTENT_TITLE, viewHolder.feedlyStreamItem.getTitle());
                Intent intent = new Intent(mActivity, ToobarBackActivity.class);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });

        setupViewHolderTextView(viewHolder);
        setupStar(viewHolder, position);
    }

    private void setupViewHolderTextView(ViewHolder viewHolder) {
        viewHolder.origin.setText(viewHolder.feedlyStreamItem.getOrigin().getTitle());
        viewHolder.time.setText(DateUtils.getUpdateString(viewHolder.feedlyStreamItem.getCrawled()));
        viewHolder.title.setText(viewHolder.feedlyStreamItem.getTitle());
        String sumary = viewHolder.feedlyStreamItem.getSummary().getContent();
        int length = sumary.length();
        length = length >= 200?200:length;
        viewHolder.summary.setText(viewHolder.feedlyStreamItem.getSummary().getContent().substring(0,length - 1));
    }

    private void setupStar(final RecyclerViewAdapter.ViewHolder viewHolder, final int position){
        if(feedlyStream.getItems().get(position).isStar())
            viewHolder.star.setImageResource(R.drawable.ic_star_black_36dp);
        else
            viewHolder.star.setImageResource(R.drawable.ic_star_outline_black_36dp);

        viewHolder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStarClick(viewHolder, position);
            }
        });
    }

    public void onStarClick(final ViewHolder viewHolder, final int position) {
        final String id = viewHolder.feedlyStreamItem.getId();
        if ((viewHolder.origin.getText()).toString().contains("同步中"))//防止反复点击
            return;

        viewHolder.origin.setText(viewHolder.feedlyStreamItem.getOrigin().getTitle() + "    同步中...");
        if (feedlyStream.getItems().get(position).isStar())
            unstarRssItem(viewHolder, position, id);
        else
            starRssItem(viewHolder, position, id);
    }

    private void starRssItem(final ViewHolder viewHolder, final int position, String id) {
        FeedlyCloudApi.starRssItem(id, new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                viewHolder.origin.setText(viewHolder.feedlyStreamItem.getOrigin().getTitle());
                feedlyStream.getItems().get(position).setIsStar(true);
                viewHolder.star.setImageResource(R.drawable.ic_star_black_36dp);
            }

            @Override
            public void failure(RetrofitError error) {
                viewHolder.origin.setText(viewHolder.feedlyStreamItem.getOrigin().getTitle() + "    同步失败");
                UiHelper.isAuthExpired(mActivity, error);
            }
        });
    }

    private void unstarRssItem(final ViewHolder viewHolder, final int position, String id) {
        FeedlyCloudApi.unStarRssItem(id, new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                viewHolder.origin.setText(viewHolder.feedlyStreamItem.getOrigin().getTitle());
                feedlyStream.getItems().get(position).setIsStar(false);
                viewHolder.star.setImageResource(R.drawable.ic_star_outline_black_36dp);
            }

            @Override
            public void failure(RetrofitError error) {
                viewHolder.origin.setText(viewHolder.feedlyStreamItem.getOrigin().getTitle() + "    同步失败");
                UiHelper.isAuthExpired(mActivity, error);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedlyStream.getItems().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView star;
        public TextView origin;
        public TextView title;
        public TextView summary;
        public TextView time;
        public Item feedlyStreamItem;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            star = (ImageView) itemView.findViewById(R.id.star);
            origin = (TextView) itemView.findViewById(R.id.origin);
            title = (TextView) itemView.findViewById(R.id.content_title);
            summary = (TextView) itemView.findViewById(R.id.summary);
            time = (TextView) itemView.findViewById(R.id.content_time);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
         }
    }

 }
