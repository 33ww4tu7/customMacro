package com.atlassian.tutorial.rest;

import org.codehaus.jackson.annotate.JsonProperty;

public class AttachmentsRestResourceModel {

    @JsonProperty
    private String path;
    @JsonProperty
    private String attID;
    @JsonProperty
    private String pageID;
    @JsonProperty
    private String userID;

    public AttachmentsRestResourceModel() {
    }

    public AttachmentsRestResourceModel(String path, String attID, String pageID, String userID) {
        this.userID = userID;
        this.pageID = pageID;
        this.path = path;
        this.attID = attID;
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

    public String getAttID() {
        return attID;
    }

    public void setAttID(String attID) {
        this.attID = attID;
    }
}