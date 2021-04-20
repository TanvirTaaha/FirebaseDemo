package com.example.firebasedemo.model;

public class SignUpModel extends BaseModel {

    private String successful;
    private String userId;

    public SignUpModel(String successful, String userId) {
        this.successful = successful;
        this.userId = userId;
    }

    public String getSuccessful() {
        return successful;
    }

    public void setSuccessful(String successful) {
        this.successful = successful;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
