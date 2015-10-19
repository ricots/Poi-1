package com.github.kaninohon.poi.model;

import java.util.List;

public class Item {
    private long published;

    private List<Alternate> alternates ;

    private int engagement;

    private List<Tag> tags;

    private long crawled;

    private long updated;

    private Content content;
    private Summary summary;
    private String title;

    private String author;

    private List<Category> categories;

    private boolean unread;

    private Origin origin;

    private String id;

    public boolean isStar() {
        return isStar;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }

    private boolean isStar;

    public void setPublished(long published){
        this.published = published;
    }
    public long getPublished(){
        return this.published;
    }
    public void setAlternates(List<Alternate> alternate){
        this.alternates = alternate;
    }
    public List<Alternate> getAlternates(){
        return this.alternates;
    }
    public void setEngagement(int engagement){
        this.engagement = engagement;
    }
    public int getEngagement(){
        return this.engagement;
    }
    public void setTags(List<Tag> tags){
        this.tags = tags;
    }
    public List<Tag> getTags(){
        return this.tags;
    }
    public void setCrawled(long crawled){
        this.crawled = crawled;
    }
    public long getCrawled(){
        return this.crawled;
    }
    public void setUpdated(long updated){
        this.updated = updated;
    }
    public long getUpdated(){
        return this.updated;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setCategories(List<Category> categories){
        this.categories = categories;
    }
    public List<Category> getCategories(){
        return this.categories;
    }
    public void setUnread(boolean unread){
        this.unread = unread;
    }
    public boolean getUnread(){
        return this.unread;
    }
    public void setOrigin(Origin origin){
        this.origin = origin;
    }
    public Origin getOrigin(){
        return this.origin;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }


    public Summary getSummary() {
        if(summary != null)
            return summary;

        if(content != null){
            Summary tmp = new Summary();
            tmp.setContent(content.getContent());
            tmp.setDirection(content.getDirection());
            return tmp;
        }
        return null;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Content getContent() {
        if(content != null)
            return content;

        if(summary != null){
            Content tmp = new Content();
            tmp.setContent(summary.getContent());
            tmp.setDirection(summary.getDirection());
            return tmp;
        }
        return null;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
