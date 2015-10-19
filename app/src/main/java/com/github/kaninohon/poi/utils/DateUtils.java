package com.github.kaninohon.poi.utils;

public class DateUtils {

    public static String getUpdateString(long timestamp) {
        long now = System.currentTimeMillis();
        long distance = now - timestamp;
        distance /= 1000;
        if(distance < 60)
            return "现在";
        else if(distance >= 60 && distance < 3600)
            return (distance / 60) + "分钟前";
        else if(distance >= 3600 && distance < 24 * 3600)
            return (distance / 3600) + "小时前";
        else
            return (distance / (24 * 3600)) + "天前";
    }
}
