package com.github.kaninohon.poi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferManager {
    private static SharedPreferManager ourInstance = new SharedPreferManager();

    private static final String USER_ID = "USER_ID";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String AVATAR_URL = "AVATAR_URL";//头像资源地址
    private static final String AUTH_STRING = "AUTH_STRING";//授权字符串
    private static final String IS_3G_NO_IMAGE = "IS_3G_NO_IMAGE";

    private static Context mContext;

    public static SharedPreferManager getInstance() {
        return ourInstance;
    }

    private SharedPreferManager() {
    }

    public void setContext(Context context){
        mContext = context;
    }

    public boolean hasLogin(){//获取个人信息成功后，才算登录成功
        if(getAuthString().equals("") || getUserEmail().equals("")){
            return false;
        }
        return true;
    }

    public void saveUserProfile(String email,String avatarUrl){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL,email);
        editor.putString(AVATAR_URL,avatarUrl);
        editor.commit();
    }

    public void saveAuthString(String authString){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_STRING,authString);
        editor.commit();
    }

    public String getAuthString(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(AUTH_STRING, "");
    }

    public String getAvatarUrl() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(AVATAR_URL, "");
    }

    public String getUserEmail() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(USER_ID, "");
    }

    public void saveUserId(String userId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID,userId);
        editor.commit();
    }

    public void setIf3gNoImage(Boolean noImage){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_3G_NO_IMAGE,noImage);
        editor.commit();
    }

    public boolean is3gNoImage(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getBoolean(IS_3G_NO_IMAGE, true);
    }
}
