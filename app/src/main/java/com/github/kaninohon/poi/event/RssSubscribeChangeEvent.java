package com.github.kaninohon.poi.event;


import android.view.Menu;

import com.github.kaninohon.poi.model.Result;

public class RssSubscribeChangeEvent {
    private int actionType;
    private Result result;


    public static final int SUBSCRIBE_ADDED = 1;
    public static final int SUBSCRIBE_REMOVEED = 2;

    public RssSubscribeChangeEvent(int actionType, Result result) {
        this.actionType = actionType;
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }


}
