package com.github.kaninohon.poi.api;

import android.content.Context;

import com.github.kaninohon.poi.AppContext;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.model.Category;
import com.github.kaninohon.poi.model.FeedlyClient;
import com.github.kaninohon.poi.model.FeedlyProfile;
import com.github.kaninohon.poi.model.FeedlyStream;
import com.github.kaninohon.poi.model.FeedlyTag;
import com.github.kaninohon.poi.model.FeedlyToken;
import com.github.kaninohon.poi.model.MarkerRequest;
import com.github.kaninohon.poi.model.SearchResult;
import com.github.kaninohon.poi.model.SubscribeRequest;
import com.github.kaninohon.poi.model.UserSubscription;
import com.github.kaninohon.poi.utils.SharedPreferManager;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Response;

public class FeedlyCloudApi {
    public static final String FEEDLY_API_PARAM_SCOPE = "https://cloud.feedly.com/subscriptions";
    public static final String FEEDLY_API_PARAM_RESPONSE_TYPE = "code";
    public static final String FEEDLY_API_PARAM_PROVIDER = "google";
    public static final String FEEDLY_API_PARAM_GRANT_TYPE = "authorization_code";

    public static final int LOGIN_OK = 1;
    public static final int LOGIN_FAIL= 0;

    public static String getGoogleLoginUrl(){
        return getFeedlyApiUrl() + "/v3/auth/auth" + "?client_id=" +
                getFeedlyClientId() + "&redirect_uri=" + getFeedlyRedirectUri() + "&scope=" +
                FEEDLY_API_PARAM_SCOPE + "&response_type=" + FEEDLY_API_PARAM_RESPONSE_TYPE + "&provider="+
                FEEDLY_API_PARAM_PROVIDER + "&migrate=false";
    }

    private static String getResourceString(int resourceId)
    {
        Context appContext = AppContext.getContext();
        return appContext.getResources().getString(resourceId);
    }

    public static String getFeedlyApiUrl(){
        return getResourceString(R.string.feedly_api_url);
    }

    public static String getFeedlyRedirectUri(){
        return getResourceString(R.string.feedly_redirect_uri);
    }

    public static String getFeedlyClientSecret(){
        return getResourceString(R.string.feedly_client_secret);
    }

    public static String getFeedlyClientId(){
        return getResourceString(R.string.feedly_client_id);
    }

    public static void getToken(String googleOauthCode, Callback<FeedlyToken> callback){
        FeedlyClient.getFeedlyClient().getFeedlyToken(FeedlyCloudApi.getFeedlyClientId(),
                FeedlyCloudApi.getFeedlyClientSecret(), FeedlyCloudApi.FEEDLY_API_PARAM_GRANT_TYPE,
                FeedlyCloudApi.getFeedlyRedirectUri(), googleOauthCode, callback);
    }

    public static void getFeedlyProfile(Callback<FeedlyProfile> callback){
        FeedlyClient.getFeedlyClient().getFeedlyProfile(SharedPreferManager.getInstance().getAuthString(), callback);
    }

    public static void getFeedlyUserTags(Callback<ArrayList<FeedlyTag>> callback){
        FeedlyClient.getFeedlyClient().getFeedlyUserTags(SharedPreferManager.getInstance().getAuthString(), callback);
    }

    public static void getCategoryStream(String category, boolean isAll, String continuation, Callback<FeedlyStream> callback){
        FeedlyClient.getFeedlyClient().getFeedlyStream(SharedPreferManager.getInstance().getAuthString(),
                "user/" + SharedPreferManager.getInstance().getUserId() + "/category/" + category, isAll, continuation,
                callback);
    }

    public static void getSourceStream(String feedId, boolean isAll, String continuation, Callback<FeedlyStream> callback){
        FeedlyClient.getFeedlyClient().getFeedlyStream(SharedPreferManager.getInstance().getAuthString(), feedId,
                isAll, continuation, callback);
    }

    public static void getTagStream(String tag, String continuation, Callback<FeedlyStream> callback){
        FeedlyClient.getFeedlyClient().getFeedlyStream(SharedPreferManager.getInstance().getAuthString(),
                "user/" + SharedPreferManager.getInstance().getUserId() +  "/tag/" + tag, false, continuation, callback);
    }

    public static void starRssItem(String itemId, Callback<Response> callback){
        ArrayList<String> items = new ArrayList<>();
        items.add(itemId);
        MarkerRequest markerRequest = new MarkerRequest("markAsSaved","entries",items);

        FeedlyClient.getFeedlyClient().starRssItem(SharedPreferManager.getInstance().getAuthString(), markerRequest,
                callback);
    }

    public static void unStarRssItem(String itemId, Callback<Response> callback){
        ArrayList<String> items = new ArrayList<>();
        items.add(itemId);
        MarkerRequest markerRequest = new MarkerRequest("markAsUnsaved","entries",items);

        FeedlyClient.getFeedlyClient().starRssItem(SharedPreferManager.getInstance().getAuthString(),markerRequest,
                callback);
    }

    public static void setRssItemsAsRead(ArrayList<String> itemIds, Callback<Response> callback){
        MarkerRequest markerRequest = new MarkerRequest("markAsRead","entries",itemIds);

        FeedlyClient.getFeedlyClient().starRssItem(SharedPreferManager.getInstance().getAuthString(),markerRequest,
                callback);
    }

    public static void rssSearch(String query, int count, Callback<SearchResult> callback){
        FeedlyClient.getFeedlyClient().rssSearch(query, count, callback);
    }

    public static void getUserSubscription(Callback<ArrayList<UserSubscription>> callback){
        FeedlyClient.getFeedlyClient().getUserSubscription(SharedPreferManager.getInstance().getAuthString(), callback);
    }

    public static void rssUnsubscribe(String feedId, Callback<Response> callback){
        FeedlyClient.getFeedlyClient().rssUnsubscribe(SharedPreferManager.getInstance().getAuthString(), feedId, callback);
    }

    public static void rssSubscribe(String feedId, Callback<Response> callback){
        FeedlyClient.getFeedlyClient().rssSubscribe(SharedPreferManager.getInstance().getAuthString(),
                new SubscribeRequest(feedId), callback);
    }
}
