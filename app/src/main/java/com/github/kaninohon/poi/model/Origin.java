package com.github.kaninohon.poi.model;

public class Origin {
    private String htmlUrl;

    private String title;

    private String streamId;

    public void setHtmlUrl(String htmlUrl){
        this.htmlUrl = htmlUrl;
    }
    public String getHtmlUrl(){
        return this.htmlUrl;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setStreamId(String streamId){
        this.streamId = streamId;
    }
    public String getStreamId(){
        return this.streamId;
    }

}