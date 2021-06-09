package com.tangent.firebasedemo.model;

import java.util.ArrayList;

public class PhoneContactModel extends BaseModel {
    private String id;
    private String name;
    private ArrayList<String> phoneNoList;

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

    public ArrayList<String> getPhoneNoList() {
        return phoneNoList;
    }

    public void setPhoneNoList(ArrayList<String> phoneNoList) {
        this.phoneNoList = phoneNoList;
    }
}
