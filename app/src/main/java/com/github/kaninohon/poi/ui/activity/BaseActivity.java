package com.github.kaninohon.poi.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.github.kaninohon.poi.R;
import com.github.kaninohon.poi.utils.AppManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public abstract class BaseActivity extends AppCompatActivity {

    abstract protected int provideContentViewId();

    //这个activity的基类主要负责activity的状态栏和导航栏的着色，activity的添加和移除
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());

        AppManager.getAppManager().addActivity(this);
        //给状态栏和导航栏着色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucent(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setTintColor(getResources().getColor(R.color.primary_dark));
        }
    }

    @TargetApi(19)
    private void setTranslucent(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    protected <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

}
