package com.github.kaninohon.poi.event;

import java.security.spec.RSAOtherPrimeInfo;

/**
 * Created by kaninohon on 2015/9/26.
 */
public class TagSelectedEvent {


    public TagSelectedEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;
}
