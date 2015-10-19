package com.github.kaninohon.poi.model;

import java.util.List;

public class FeedlyStream {
    private List<Alternate> alternates;

    private List<Item> items;

    private String continuation;

    private List<Self> selfs ;

    private long updated;

    private String direction;

    private String title;

    private String id;

    public void setAlternates(List<Alternate> alternate){
        this.alternates = alternate;
    }
    public List<Alternate> getAlternates(){
        return this.alternates;
    }
    public void setItems(List<Item> items){
        this.items = items;
    }
    public List<Item> getItems(){
        return this.items;
    }
    public void setContinuation(String continuation){
        this.continuation = continuation;
    }
    public String getContinuation(){
        return this.continuation;
    }
    public void setSelfs(List<Self> self){
        this.selfs = self;
    }
    public List<Self> getSelfs(){
        return this.selfs;
    }
    public void setUpdated(long updated){
        this.updated = updated;
    }
    public long getUpdated(){
        return this.updated;
    }
    public void setDirection(String direction){
        this.direction = direction;
    }
    public String getDirection(){
        return this.direction;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }

}
