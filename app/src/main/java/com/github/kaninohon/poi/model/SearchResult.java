package com.github.kaninohon.poi.model;

import java.util.List;

public class SearchResult {
    private String queryType;

    private String hint;


    private List<Result> results ;

    private String scheme;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
    public void setQueryType(String queryType){
        this.queryType = queryType;
    }
    public String getQueryType(){
        return this.queryType;
    }
    public void setHint(String hint){
        this.hint = hint;
    }
    public String getHint(){
        return this.hint;
    }

    public void setScheme(String scheme){
        this.scheme = scheme;
    }
    public String getScheme(){
        return this.scheme;
    }
}
