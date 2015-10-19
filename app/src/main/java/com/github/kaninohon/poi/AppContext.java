package com.github.kaninohon.poi;

import android.app.Application;
import android.content.Context;

import com.github.kaninohon.poi.model.FeedlyToken;
import com.github.kaninohon.poi.model.Result;
import com.github.kaninohon.poi.model.UserSubscription;
import com.github.kaninohon.poi.utils.SharedPreferManager;

import java.util.ArrayList;

public class AppContext extends Application {

    private static Context mContext;


    public static ArrayList<UserSubscription> getUserSubscriptions() {
        return userSubscriptions;
    }

    public static void setUserSubscriptions(ArrayList<UserSubscription> userSubscriptions) {
        AppContext.userSubscriptions = userSubscriptions;
    }

    private static ArrayList<UserSubscription> userSubscriptions;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        SharedPreferManager.getInstance().setContext(this);

    }

    public static Context getContext() {
        return mContext;
    }

    public static void addSubscription2App(Result result){
        UserSubscription tmp = new UserSubscription();
        tmp.setId(result.getFeedId());
        AppContext.getUserSubscriptions().add(tmp);
    }

    public static void removeSubscription2App(Result result){
        ArrayList<UserSubscription> userSubscriptions = AppContext.getUserSubscriptions();
        for(UserSubscription userSubscription : userSubscriptions){
            if(userSubscription.getId().equals(result.getFeedId())){
                userSubscriptions.remove(userSubscription);
                break;
            }
        }
    }
}
