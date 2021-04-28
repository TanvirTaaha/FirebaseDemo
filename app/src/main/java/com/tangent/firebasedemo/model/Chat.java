package com.tangent.firebasedemo.model;

public class Chat extends BaseModel{
    private String text;
    private String photo;
    private String time;
    private String userName;
    private String userId;
    private boolean mine;

    public Chat(String text, String photo, String time, String userName, String userId, boolean mine) {
        this.text = text;
        this.photo = photo;
        this.time = time;
        this.userName = userName;
        this.userId = userId;
        this.mine = mine;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
}
