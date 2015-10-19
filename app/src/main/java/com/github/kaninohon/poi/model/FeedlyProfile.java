package com.github.kaninohon.poi.model;

import java.util.List;
//有可能部分属性被我去掉了

public class FeedlyProfile {
    private String id;

    private List<Login> logins;


    private boolean evernoteConnected;

    private boolean pocketConnected;

    private String anonymizedHash;

    private String client;

    private String wave;

    private String email;

    private String picture;

    private String givenName;

    private String familyName;

    private String google;

    private String gender;


    private boolean dropboxConnected;


    private boolean twitterConnected;

    private boolean windowsLiveConnected;

    private boolean facebookConnected;

    private boolean wordPressConnected;

    private String locale;

    private String fullName;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setLogins(List<Login> logins){
        this.logins = logins;
    }
    public List<Login> getLogins(){
        return this.logins;
    }
    public void setEvernoteConnected(boolean evernoteConnected){
        this.evernoteConnected = evernoteConnected;
    }
    public boolean getEvernoteConnected(){
        return this.evernoteConnected;
    }
    public void setPocketConnected(boolean pocketConnected){
        this.pocketConnected = pocketConnected;
    }
    public boolean getPocketConnected(){
        return this.pocketConnected;
    }
    public void setAnonymizedHash(String anonymizedHash){
        this.anonymizedHash = anonymizedHash;
    }
    public String getAnonymizedHash(){
        return this.anonymizedHash;
    }
    public void setClient(String client){
        this.client = client;
    }
    public String getClient(){
        return this.client;
    }
    public void setWave(String wave){
        this.wave = wave;
    }
    public String getWave(){
        return this.wave;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setPicture(String picture){
        this.picture = picture;
    }
    public String getPicture(){
        return this.picture;
    }
    public void setGivenName(String givenName){
        this.givenName = givenName;
    }
    public String getGivenName(){
        return this.givenName;
    }
    public void setFamilyName(String familyName){
        this.familyName = familyName;
    }
    public String getFamilyName(){
        return this.familyName;
    }
    public void setGoogle(String google){
        this.google = google;
    }
    public String getGoogle(){
        return this.google;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return this.gender;
    }
    public void setDropboxConnected(boolean dropboxConnected){
        this.dropboxConnected = dropboxConnected;
    }
    public boolean getDropboxConnected(){
        return this.dropboxConnected;
    }
    public void setTwitterConnected(boolean twitterConnected){
        this.twitterConnected = twitterConnected;
    }
    public boolean getTwitterConnected(){
        return this.twitterConnected;
    }
    public void setWindowsLiveConnected(boolean windowsLiveConnected){
        this.windowsLiveConnected = windowsLiveConnected;
    }
    public boolean getWindowsLiveConnected(){
        return this.windowsLiveConnected;
    }
    public void setFacebookConnected(boolean facebookConnected){
        this.facebookConnected = facebookConnected;
    }
    public boolean getFacebookConnected(){
        return this.facebookConnected;
    }
    public void setWordPressConnected(boolean wordPressConnected){
        this.wordPressConnected = wordPressConnected;
    }
    public boolean getWordPressConnected(){
        return this.wordPressConnected;
    }
    public void setLocale(String locale){
        this.locale = locale;
    }
    public String getLocale(){
        return this.locale;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getFullName(){
        return this.fullName;
    }

}
