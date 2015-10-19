package com.github.kaninohon.poi.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;

import com.github.kaninohon.poi.AppContext;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.api.FeedlyCloudApi;
import com.github.kaninohon.poi.model.FeedlyToken;
import com.github.kaninohon.poi.utils.NetworkUtils;
import com.github.kaninohon.poi.utils.SharedPreferManager;
import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WebViewFragment extends BaseFragment{

    public static final String EXTRA_ACTION_TYPE = "EXTRA_ACTION_TYPE";
    public static final String ACTION_LOGIN = "ACTION_LOGIN";
    public static final String ACTION_OPEN_RSS_ITEM_DETAIL = "ACTION_OPEN_RSS_ITEM_DETAIL";
    public static final String EXTRA_RSS_CONTENT = "EXTRA_RSS_CONTENT";


    private ProgressBar mProgressbar;
    private WebView mWebView;

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_webview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initWebview();
        String extraType = getArguments().getString(EXTRA_ACTION_TYPE);
        if(extraType != null) {
            if (extraType.equals(ACTION_LOGIN)){
                mProgressbar = $(R.id.progressbar);
                mProgressbar.setIndeterminateDrawable(new IndeterminateProgressDrawable(getActivity()));
                mWebView.loadUrl(FeedlyCloudApi.getGoogleLoginUrl());
            } else if (extraType.equals(ACTION_OPEN_RSS_ITEM_DETAIL)) {
                String content = getArguments().getString(EXTRA_RSS_CONTENT);
                if (SharedPreferManager.getInstance().is3gNoImage() && !NetworkUtils.isWifiOpen()) {
                    //无图模式下把img标签过滤掉
                    content = content.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
                }
                mWebView.loadData(content, "text/html; charset=UTF-8", null);
            }
        }
    }

    private void initWebview(){
        mWebView = $(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setDefaultFontSize(15);
        //settings.setDefaultFixedFontSize(30);
        //settings.setUseWideViewPort(true);
        //settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(mWebView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        settings.setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new WebClient());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null)
            mWebView.destroy();
    }

    @Override
    public void onPause() {
        if (mWebView != null)
            mWebView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.onResume();
    }

    private class WebClient extends WebViewClient {
        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null)
                view.loadUrl(url);
            //如果不需要其他对点击链接事件的处理返回true，否则返回false
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);
            if(mProgressbar != null)
                mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            if(mProgressbar != null)
                mProgressbar.setVisibility(View.GONE);
            if(url.startsWith("https://cloud.feedly.com/feedly.html?code=")){
                //google授权结束，取得了授权code，开始获取feedly的token
                getFeedlyOauthToken(url);
            }
        }
    }

    private String getCodeFromUrl(String url) {
        return url.substring(url.indexOf("code=") + 5, url.indexOf("&state=#"));
    }

    private void getFeedlyOauthToken(String url){
        FeedlyCloudApi.getToken(getCodeFromUrl(url), new Callback<FeedlyToken>() {

            @Override
            public void success(FeedlyToken feedlyToken, Response response) {
                SharedPreferManager.getInstance().saveAuthString(feedlyToken.getToken_type() + " " +
                        feedlyToken.getAccess_token());
                SharedPreferManager.getInstance().saveUserId(feedlyToken.getId());
                Intent intent = new Intent();
                getActivity().setResult(FeedlyCloudApi.LOGIN_OK, intent);
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Intent intent = new Intent();
                getActivity().setResult(FeedlyCloudApi.LOGIN_FAIL, intent);
                getActivity().finish();
            }
        });
    }

    public void WebViewBack(){
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }else{
            getActivity().finish();
        }
    }
}
