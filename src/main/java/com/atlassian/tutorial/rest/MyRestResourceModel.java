package com.atlassian.tutorial.rest;


import org.codehaus.jackson.annotate.JsonProperty;

public class MyRestResourceModel {

    @JsonProperty
    private String path;

    @JsonProperty
    private String pageID;

    @JsonProperty
    private String userID;

    public MyRestResourceModel() {
    }

    public MyRestResourceModel(String path, String pageID, String userID) {
        this.userID = userID;
        this.pageID = pageID;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setMessage(String path) {
        this.path = path;
    }


    public String getpageID() {
        return pageID;
    }

    public void setpageID(String pageID) {
        this.pageID = pageID;
    }

    public String getuserID() {
        return userID;
    }

    public void setuserID(String userID) {
        this.userID = userID;
    }
}