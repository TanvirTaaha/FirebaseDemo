package com.tangent.firebasedemo.model.firebasemodel;

import com.tangent.firebasedemo.model.BaseModel;

import java.util.ArrayList;

public class Conversation extends BaseModel {
    private String id;
    private ArrayList<ConversationMember> members;
    private ArrayList<ChatBubble> main_thread;
    private ArrayList<ChatBubble> starred_thread;
    private ArrayList<ChatBubble> media_thread;

    public void setId(String id) {
        this.id = id;
    }

    public void setMembers(ArrayList<ConversationMember> members) {
        this.members = members;
    }

    public void setMainThread(ArrayList<ChatBubble> mainThread) {
        this.main_thread = mainThread;
    }

    public void setStarredThread(ArrayList<ChatBubble> starredThread) {
        this.starred_thread = starredThread;
    }

    public void setMediaThread(ArrayList<ChatBubble> mediaThread) {
        this.media_thread = mediaThread;
    }

    public String getId() {
        return id;
    }

    public ArrayList<ConversationMember> getMembers() {
        return members;
    }

    public ArrayList<ChatBubble> getMainThread() {
        return main_thread;
    }

    public ArrayList<ChatBubble> getStarredThread() {
        return starred_thread;
    }

    public ArrayList<ChatBubble> getMediaThread() {
        return media_thread;
    }
}
