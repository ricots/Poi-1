package com.github.kaninohon.poi.model;

import java.util.ArrayList;

public class MarkerRequest {
    String action;
    String type;
    ArrayList<String> entryIds;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(ArrayList<String> entryIds) {
        this.entryIds = entryIds;
    }

    public MarkerRequest(String action, String type, ArrayList<String> entryIds){
        this.action = action;
        this.type = type;
        this.entryIds = entryIds;
    }
}