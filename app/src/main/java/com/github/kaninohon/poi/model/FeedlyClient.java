package com.github.kaninohon.poi.model;

import android.util.Log;

import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class FeedlyClient{
    private static FeedlyService service;

    public static FeedlyService getFeedlyClient() {
        if (service == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .serializeNulls()
                    .create();

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            client.setWriteTimeout(30, TimeUnit.SECONDS);

            RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(client))
                    .setEndpoint(FeedlyCloudApi.getFeedlyApiUrl())
                    .setConverter(new GsonConverter(gson))
                    //.setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("kyukyu"))
                    .build();
            service = restAdapter.create(FeedlyService.class);
        }
        return service;
    }
}