package com.example.firebasedemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private enum PreferenceKey {
        LOGGED_IN_ALREADY("loggedInAlready"),
        ACCESS_TOKEN("access_token"),
        USER_NAME("userName"),
        USER_EMAIL("userEmail"),
        USER_PASSWORD("userPassword"),
        USER_ID("userId"),

        ;

        private String key;

        PreferenceKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCES_FILE = "selfishhStorage";

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE,
                Context.MODE_PRIVATE);
    }

    public void deleteAllSharedPreferences() {
        sharedPreferences.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(PreferenceKey.LOGGED_IN_ALREADY.getKey(), false);
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(PreferenceKey.LOGGED_IN_ALREADY.getKey(), loggedIn).apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(PreferenceKey.ACCESS_TOKEN.getKey(), "");
    }

    public void setAccessToken(String accessToken) {
        sharedPreferences.edit().putString(PreferenceKey.ACCESS_TOKEN.getKey(), accessToken).apply();
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(PreferenceKey.USER_NAME.getKey(), username).apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(PreferenceKey.USER_NAME.getKey(), "");
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(PreferenceKey.USER_EMAIL.getKey(), email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(PreferenceKey.USER_EMAIL.getKey(), "");
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString(PreferenceKey.USER_PASSWORD.getKey(), password).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PreferenceKey.USER_PASSWORD.getKey(), "");
    }

    public void setUserId(String userId) {
        sharedPreferences.edit().putString(PreferenceKey.USER_ID.getKey(), userId).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(PreferenceKey.USER_ID.getKey(), "");
    }
}
