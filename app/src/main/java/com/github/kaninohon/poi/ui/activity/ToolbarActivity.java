package com.github.kaninohon.poi.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import com.github.kaninohon.poi.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public abstract class ToolbarActivity extends BaseActivity {

    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected CoordinatorLayout mCoordinatorLayout;
    protected boolean isHidden = false;

    public void onToolbarClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAppBar = $(R.id.app_bar_layout);
        mToolbar = $(R.id.toolbar);
        mTabLayout = $(R.id.tab_layout);
        mCoordinatorLayout = $(R.id.coordinator_layout);

        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException("no toolbar");
        }

        mToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onToolbarClick();
                    }
                }
        );

        setSupportActionBar(mToolbar);
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBar.setElevation(10.6f);
        }
    }

    public AppBarLayout getAppBar() {
        return mAppBar;
    }

    public boolean canBack() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void setAppBarAlpha(float alpha) {
        mAppBar.setAlpha(alpha);
    }

    protected void hideOrShowToolbar() {
        mAppBar.animate()
               .translationY(isHidden ? 0 : -mAppBar.getHeight())
               .setInterpolator(new DecelerateInterpolator(2))
               .start();

        isHidden = !isHidden;
    }

    public void setToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
    }

}
