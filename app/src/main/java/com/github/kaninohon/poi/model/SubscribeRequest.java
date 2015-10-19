package com.github.kaninohon.poi.model;



public class SubscribeRequest {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public SubscribeRequest(String id){
        this.id = id;
    }

}
