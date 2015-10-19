package com.github.kaninohon.poi.utils;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.github.kaninohon.poi.AppContext;

public class DeviceUtils {
    public static void hideSoftKeyboard(View view) {
        if (view == null)
            return;
        ((InputMethodManager) AppContext.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }
}
