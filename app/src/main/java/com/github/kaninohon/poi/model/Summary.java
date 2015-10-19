package com.github.kaninohon.poi.model;

//这个其实跟Content是一样的
public class Summary {
    private String direction;

    private String content;

    public void setDirection(String direction){
        this.direction = direction;
    }
    public String getDirection(){
        return this.direction;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }

}