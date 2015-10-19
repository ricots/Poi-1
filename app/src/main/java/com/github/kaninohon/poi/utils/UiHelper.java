package com.github.kaninohon.poi.utils;

import android.app.Activity;
import android.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import com.github.kaninohon.poi.R;

import retrofit.RetrofitError;

public class UiHelper {

    public static void recyclerviewShowTip(Fragment fragment, String tip, final int showTime){
        //在一个Fragment的recyclervie上面显示一个snackbar，提示一些信息
        RecyclerView recyclerView = (RecyclerView)fragment.getActivity().findViewById(R.id.recycler_view);
        if(recyclerView != null)
            Snackbar.make(recyclerView, tip, showTime).show();
    }

    public static void recyclerviewShowTip(Activity activity, String tip, final int showTime){
        //在一个Activity的recyclervie上面显示一个snackbar，提示一些信息
        RecyclerView recyclerView = (RecyclerView)activity.findViewById(R.id.recycler_view);
        if(recyclerView != null)
            Snackbar.make(recyclerView, tip, showTime).show();
    }

    public static boolean isAuthExpired(Activity activity, RetrofitError error){
        if(error.getResponse() != null && error.getResponse().getStatus() == 401){
            //擦除登录信息
            SharedPreferManager.getInstance().saveAuthString("");
            SharedPreferManager.getInstance().saveUserProfile("", "");
            SharedPreferManager.getInstance().saveUserId("");
            UiHelper.recyclerviewShowTip(activity, "授权过期，请重新登录", Snackbar.LENGTH_LONG);
            return true;
        }
        return false;
    }

}
