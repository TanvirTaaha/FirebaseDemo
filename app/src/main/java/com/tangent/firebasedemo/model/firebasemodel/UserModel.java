package com.tangent.firebasedemo.model.firebasemodel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tangent.firebasedemo.model.BaseModel;

import java.util.ArrayList;

@Entity
public class UserModel extends BaseModel {
    @PrimaryKey
    private String id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    @ColumnInfo(name = "profile_picture")
    private String profilePicture;
    @ColumnInfo(name = "bio")
    private String bio;
    @ColumnInfo(name = "inbox")
    private ArrayList<InboxItem> inbox;

    public UserModel() {
    }

    public UserModel(String id, String name, String phoneNumber, String profilePicture, String bio, ArrayList<InboxItem> inbox) {
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
