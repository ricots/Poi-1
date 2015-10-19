package com.github.kaninohon.poi.model;

import java.util.List;

public class UserSubscription {
    private String id;

    private String title;

    private String website;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    private List<Category> categories;

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    private long updated;

    private double velocity;

    private int subscribers;


    private String contentType;

    private boolean partial;

    private String iconUrl;

    private String visualUrl;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setWebsite(String website){
        this.website = website;
    }
    public String getWebsite(){
        return this.website;
    }

    public void setVelocity(double velocity){
        this.velocity = velocity;
    }
    public double getVelocity(){
        return this.velocity;
    }
    public void setSubscribers(int subscribers){
        this.subscribers = subscribers;
    }
    public int getSubscribers(){
        return this.subscribers;
    }

    public void setContentType(String contentType){
        this.contentType = contentType;
    }
    public String getContentType(){
        return this.contentType;
    }
    public void setPartial(boolean partial){
        this.partial = partial;
    }
    public boolean getPartial(){
        return this.partial;
    }
    public void setIconUrl(String iconUrl){
        this.iconUrl = iconUrl;
    }
    public String getIconUrl(){
        return this.iconUrl;
    }
    public void setVisualUrl(String visualUrl){
        this.visualUrl = visualUrl;
    }
    public String getVisualUrl(){
        return this.visualUrl;
    }
}
