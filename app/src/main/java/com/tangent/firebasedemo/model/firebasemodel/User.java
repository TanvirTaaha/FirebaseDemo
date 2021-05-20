package com.tangent.firebasedemo.model.firebasemodel;

import com.tangent.firebasedemo.model.BaseModel;

import java.util.ArrayList;

public class User extends BaseModel {
    private String id;
    private String name;
    private String phoneNumber;
    private String profilePicture;
    private String bio;
    private ArrayList<InboxItem> inbox;

    public User() {
    }

    public User(String id, String name, String phoneNumber, String profilePicture, String bio, ArrayList<InboxItem> inbox) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.inbox = inbox;
    }

    public void setInbox(ArrayList<InboxItem> inbox) {
        this.inbox = inbox;
    }

    public void addInboxItem(InboxItem item) {
        if (inbox == null) {
            inbox = new ArrayList<>();
        }
        inbox.add(item);
    }

    public ArrayList<InboxItem> getInbox() {
        return inbox;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
