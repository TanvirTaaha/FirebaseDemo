package com.tangent.firebasedemo.model.firebasemodel;

import com.tangent.firebasedemo.model.BaseModel;

public class InboxItem extends BaseModel {
    private String conversation_id;
    private String time;
    private String read_status;

    public String getConversationId() {
        return conversation_id;
    }

    public void setConversationId(String conversationId) {
        this.conversation_id = conversationId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReadStatus() {
        return read_status;
    }

    public void setReadStatus(String readStatus) {
        this.read_status = readStatus;
    }
}
