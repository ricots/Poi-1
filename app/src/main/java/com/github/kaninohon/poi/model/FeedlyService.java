package com.github.kaninohon.poi.model;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface FeedlyService {

    @FormUrlEncoded
    @POST("/v3/auth/token")
    void getFeedlyToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret,
                        @Field("grant_type") String grantType, @Field("redirect_uri") String redirectUri,
                        @Field("code") String code, Callback<FeedlyToken> callback);

    @GET("/v3/profile")
    void getFeedlyProfile(@Header("Authorization") String header, Callback<FeedlyProfile> callback);

    @GET("/v3/streams/{categoryId}/contents")
    void getFeedlyStream(@Header("Authorization") String header, @Path("categoryId")  String categoryId,
                         @Query("unreadOnly") boolean unreadOnly, @Query("continuation") String continuation,
                         Callback<FeedlyStream> callback);

    @GET("/v3/tags")
    void getFeedlyUserTags(@Header("Authorization") String header, Callback<ArrayList<FeedlyTag>> callback);

    @Headers("Content-Type: application/json")
    @POST("/v3/markers")
    void starRssItem(@Header("Authorization") String header, @Body MarkerRequest starRequest, Callback<Response> callback);

    @GET("/v3/search/feeds")
    void rssSearch(@Query("query") String query, @Query("count") int count, Callback<SearchResult> callback);

    @GET("/v3/subscriptions")
    void getUserSubscription(@Header("Authorization") String header, Callback<ArrayList<UserSubscription>> callback);

    @Headers("Content-Type: application/json")
    @POST("/v3/subscriptions")
    void rssSubscribe(@Header("Authorization") String header, @Body SubscribeRequest subscribeRequest,
                      Callback<Response> callback);

    @DELETE("/v3/subscriptions/{feedId}")
    void rssUnsubscribe(@Header("Authorization") String header, @Path("feedId")  String feedId,Callback<Response> callback);
}