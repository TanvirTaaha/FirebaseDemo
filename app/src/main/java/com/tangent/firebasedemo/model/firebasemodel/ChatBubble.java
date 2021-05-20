package com.tangent.firebasedemo.model.firebasemodel;

import com.tangent.firebasedemo.model.BaseModel;

public class ChatBubble extends BaseModel {
    private String text;
    private String time;
    private String sender_id;
    private String id;
    private Media media;
    private Boolean is_starred;

    public ChatBubble(String text, String time, String sender_id, String id, boolean starred, Media media) {
        this.text = text;
        this.time = time;
        this.sender_id = sender_id;
        this.id = id;
        this.is_starred = starred;
        this.media = media;
    }

    public ChatBubble() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Boolean isStarred() {
        return is_starred;
    }

    public void setStarred(boolean starred) {
        this.is_starred = starred;
    }
}
