package com.tangent.firebasedemo.model.uimodel;

import com.tangent.firebasedemo.model.BaseModel;

public class ChatThreadModel extends BaseModel {
    private String chatUserName;
    private String firstMessage;
    private String imageUrl;
    private String phoneNo;
    private String lastTime;
    private Boolean lastMessagePhoto;

    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    public String getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public Boolean isLastMessagePhoto() {
        return lastMessagePhoto;
    }

    public void setLastMessagePhoto(boolean lastMessagePhoto) {
        this.lastMessagePhoto = lastMessagePhoto;
    }
}
