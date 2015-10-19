package com.github.kaninohon.poi.model;

public class FeedlyToken{
    private int expires_in;

    private String token_type;

    private String access_token;

    private String plan;

    private String refresh_token;

    private String provider;

    private String id;

    public void setExpires_in(int expires_in){
        this.expires_in = expires_in;
    }
    public int getExpires_in(){
        return this.expires_in;
    }
    public void setToken_type(String token_type){
        this.token_type = token_type;
    }
    public String getToken_type(){
        return this.token_type;
    }
    public void setAccess_token(String access_token){
        this.access_token = access_token;
    }
    public String getAccess_token(){
        return this.access_token;
    }
    public void setPlan(String plan){
        this.plan = plan;
    }
    public String getPlan(){
        return this.plan;
    }
    public void setRefresh_token(String refresh_token){
        this.refresh_token = refresh_token;
    }
    public String getRefresh_token(){
        return this.refresh_token;
    }
    public void setProvider(String provider){
        this.provider = provider;
    }
    public String getProvider(){
        return this.provider;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }

}