package com.github.kaninohon.poi.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kaninohon.poi.AppContext;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.model.Result;
import com.github.kaninohon.poi.model.SearchResult;
import com.github.kaninohon.poi.model.UserSubscription;
import com.github.kaninohon.poi.ui.activity.RssSourceDetailActivity;
import com.github.kaninohon.poi.utils.UiHelper;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RssSearchAdapter extends RecyclerView.Adapter<RssSearchAdapter.ViewHolder>{
    private SearchResult searchResult;
    private Activity mActivity;
    private RssSearchAdapter.ViewHolder lastClickViewHolder;

    public ViewHolder getLastClickViewHolder() {
        return lastClickViewHolder;
    }

    public void setLastClickViewHolder(ViewHolder lastClickViewHolder) {
        this.lastClickViewHolder = lastClickViewHolder;
    }


    public RssSearchAdapter(SearchResult searchResult,Activity activity) {
        this.searchResult = searchResult;
        this.mActivity = activity;
    }

    @Override
    public RssSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_recyclerview, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RssSearchAdapter.ViewHolder viewHolder, final int position) {
        final Result result = viewHolder.result = searchResult.getResults().get(position);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastClickViewHolder(viewHolder);
                Intent intent = new Intent(mActivity,RssSourceDetailActivity.class);
                intent.putExtra(RssSourceDetailActivity.EXTRA_DETAIL,result);
                mActivity.startActivity(intent);
            }
        });

        resetAddRemove(viewHolder, result);
        Picasso.with(mActivity).load(result.getIconUrl()).into(viewHolder.icon);
        viewHolder.title.setText(result.getTitle());
        viewHolder.description.setText(result.getDescription());
        viewHolder.subscribers.setText("订阅数\n" + result.getSubscribers());
        viewHolder.velocity.setText("篇/周\n" + result.getVelocity());
        if(result.getPartial())
            viewHolder.partial.setText("输出类型\n摘要");
        else
            viewHolder.partial.setText("输出类型\n全文");
     }

    //根据RSS是否已订阅来设置显示添加还是删除图标，设置监听添加源还是删除原操作
    public void resetAddRemove(final RssSearchAdapter.ViewHolder viewHolder, final Result result){
        final String id = result.getFeedId();
        result.setSubscribed(false);
        //已经订阅过的只允许取消订阅
        for(UserSubscription userSubscription : AppContext.getUserSubscriptions()){
            if(userSubscription.getId().equals(id)) {
                viewHolder.add.setImageResource(R.drawable.ic_remove_black_24dp);
                result.setSubscribed(true);
                viewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rssUnsubscribe(result, viewHolder);
                    }
                });
                return;
            }
        }
        //没订阅过的可以订阅
        viewHolder.add.setImageResource(R.drawable.ic_add_black_24dp);
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rssSubscribe(result, viewHolder);
            }
        });
    }

    private void rssUnsubscribe(final Result result, final ViewHolder viewHolder){
        if((viewHolder.title.getText()).toString().contains("同步中"))//防止反复点击
            return;

        viewHolder.title.setText("同步中...    " + result.getTitle());
        FeedlyCloudApi.rssUnsubscribe(result.getFeedId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                viewHolder.add.setImageResource(R.drawable.ic_add_black_24dp);
                viewHolder.title.setText(viewHolder.title.getText().toString().replace("同步中...    ", ""));
                AppContext.removeSubscription2App(result);
                result.setSubscribed(false);
                resetAddRemove(viewHolder,result);
            }

            @Override
            public void failure(RetrofitError error) {
                viewHolder.title.setText(viewHolder.title.getText().toString().replace("同步中...", "同步失败"));
                UiHelper.isAuthExpired(mActivity, error);
            }
        });
    }

    private void rssSubscribe(final Result result, final ViewHolder viewHolder){
        if((viewHolder.title.getText()).toString().contains("同步中"))//防止反复点击
            return;

        viewHolder.title.setText("同步中...    " + result.getTitle());
        FeedlyCloudApi.rssSubscribe(result.getFeedId(), new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                viewHolder.add.setImageResource(R.drawable.ic_remove_black_24dp);
                viewHolder.title.setText(viewHolder.title.getText().toString().replace("同步中...    ", ""));
                AppContext.addSubscription2App(result);
                result.setSubscribed(true);
                resetAddRemove(viewHolder, result);
            }

            @Override
            public void failure(RetrofitError error) {
                viewHolder.title.setText(viewHolder.title.getText().toString().replace("同步中...", "同步失败"));
                UiHelper.isAuthExpired(mActivity, error);
            }
        });
    }



    @Override
    public int getItemCount() {
        return searchResult.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView title;
        public TextView description;
        public TextView subscribers;
        public TextView velocity;
        public TextView partial;
        public ImageView add;
        public CardView cardView;
        public Result result;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView)itemView.findViewById(R.id.icon);
            title = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
            subscribers = (TextView)itemView.findViewById(R.id.subscribers);
            velocity = (TextView)itemView.findViewById(R.id.velocity);
            partial = (TextView)itemView.findViewById(R.id.partial);
            add = (ImageView)itemView.findViewById(R.id.add);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

}
