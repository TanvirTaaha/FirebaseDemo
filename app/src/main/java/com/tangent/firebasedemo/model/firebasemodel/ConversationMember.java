package com.tangent.firebasedemo.model.firebasemodel;

import com.tangent.firebasedemo.model.BaseModel;

public class ConversationMember extends BaseModel {
    private String user_id;
    private Boolean is_blocked;

    public ConversationMember(String userId, boolean isBlocked) {
        this.user_id = userId;
        this.is_blocked = isBlocked;
    }

    public ConversationMember() {
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public Boolean isBlocked() {
        return is_blocked;
    }

    public void setBlocked(boolean isBlocked) {
        this.is_blocked = isBlocked;
    }
}
