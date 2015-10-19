package com.github.kaninohon.poi.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;

public class DoubleClickExitHelper {

	private final Activity mActivity;

	public DoubleClickExitHelper(Activity activity) {
		mActivity = activity;
	}

	private long exitTime = 0;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode != KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			UiHelper.recyclerviewShowTip(mActivity, "再按一次退出", Snackbar.LENGTH_SHORT);
			exitTime = System.currentTimeMillis();
		} else {
			AppManager.getAppManager().AppExit(mActivity);
		}
		return true;
	}
}
