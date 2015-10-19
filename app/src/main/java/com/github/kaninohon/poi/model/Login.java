package com.github.kaninohon.poi.model;

public class Login {
    private String id;

    private String picture;

    private String providerId;

    private String provider;

    private String fullName;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setPicture(String picture){
        this.picture = picture;
    }
    public String getPicture(){
        return this.picture;
    }
    public void setProviderId(String providerId){
        this.providerId = providerId;
    }
    public String getProviderId(){
        return this.providerId;
    }
    public void setProvider(String provider){
        this.provider = provider;
    }
    public String getProvider(){
        return this.provider;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getFullName(){
        return this.fullName;
    }

}