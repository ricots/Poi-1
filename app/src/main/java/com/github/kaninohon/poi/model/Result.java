package com.github.kaninohon.poi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable{


    public ArrayList<String> getDeliciousTags() {
        return deliciousTags;
    }

    public void setDeliciousTags(ArrayList<String> deliciousTags) {
        this.deliciousTags = deliciousTags;
    }

    private ArrayList<String> deliciousTags ;

    private String feedId;

    private String language;

    private String title;

    private double velocity;

    private int subscribers;

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private long lastUpdated;

    private String website;

    private double score;

    private String websiteTitle;

    private double coverageScore;

    private double coverage;

    private int estimatedEngagement;

    private String hint;

    private boolean curated;

    private boolean featured;

    private String description;

    private String scheme;

    private String contentType;

    private String twitterScreenName;

    private int twitterFollowers;

    private boolean partial;

    private String iconUrl;

    private String visualUrl;

    private String coverColor;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    private String coverUrl;

    private double art;

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    private boolean subscribed;


    public void setFeedId(String feedId){
        this.feedId = feedId;
    }
    public String getFeedId(){
        return this.feedId;
    }
    public void setLanguage(String language){
        this.language = language;
    }
    public String getLanguage(){
        return this.language;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
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

    public void setWebsite(String website){
        this.website = website;
    }
    public String getWebsite(){
        return this.website;
    }
    public void setScore(double score){
        this.score = score;
    }
    public double getScore(){
        return this.score;
    }
    public void setWebsiteTitle(String websiteTitle){
        this.websiteTitle = websiteTitle;
    }
    public String getWebsiteTitle(){
        return this.websiteTitle;
    }
    public void setCoverageScore(double coverageScore){
        this.coverageScore = coverageScore;
    }
    public double getCoverageScore(){
        return this.coverageScore;
    }
    public void setCoverage(double coverage){
        this.coverage = coverage;
    }
    public double getCoverage(){
        return this.coverage;
    }
    public void setEstimatedEngagement(int estimatedEngagement){
        this.estimatedEngagement = estimatedEngagement;
    }
    public int getEstimatedEngagement(){
        return this.estimatedEngagement;
    }
    public void setHint(String hint){
        this.hint = hint;
    }
    public String getHint(){
        return this.hint;
    }
    public void setCurated(boolean curated){
        this.curated = curated;
    }
    public boolean getCurated(){
        return this.curated;
    }
    public void setFeatured(boolean featured){
        this.featured = featured;
    }
    public boolean getFeatured(){
        return this.featured;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setScheme(String scheme){
        this.scheme = scheme;
    }
    public String getScheme(){
        return this.scheme;
    }
    public void setContentType(String contentType){
        this.contentType = contentType;
    }
    public String getContentType(){
        return this.contentType;
    }
    public void setTwitterScreenName(String twitterScreenName){
        this.twitterScreenName = twitterScreenName;
    }
    public String getTwitterScreenName(){
        return this.twitterScreenName;
    }
    public void setTwitterFollowers(int twitterFollowers){
        this.twitterFollowers = twitterFollowers;
    }
    public int getTwitterFollowers(){
        return this.twitterFollowers;
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
    public void setCoverColor(String coverColor){
        this.coverColor = coverColor;
    }
    public String getCoverColor(){
        return this.coverColor;
    }
    public void setArt(double art){
        this.art = art;
    }
    public double getArt(){
        return this.art;
    }

}
