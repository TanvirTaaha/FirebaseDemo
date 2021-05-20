package com.tangent.firebasedemo.model.firebasemodel;

import com.tangent.firebasedemo.model.BaseModel;

public class Media extends BaseModel {
    private String id;
    private String url;
    private String time;
    private Integer type;

    public Media(String id, String url, String time, int type) {
        this.id = id;
        this.url = url;
        this.time = time;
        this.type = type;
    }

    public Media() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
