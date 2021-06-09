package com.tangent.firebasedemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangent.firebasedemo.model.PhoneContactModel;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PreferenceManager {

    private enum PreferenceKey {
        LOGGED_IN_ALREADY("loggedInAlready"),
        ACCESS_TOKEN("access_token"),
        USER_NAME("userName"),
        USER_EMAIL("userEmail"),
        USER_PASSWORD("userPassword"),
        USER_ID("userId"),
        PHONE_NO_ENTERED_BEFORE("phoneNoEnteredBeforeForSignup"),
        PHONE_CONTACT_ARRAYLIST("phoneContactArrayList"),
        PREVIOUSLY_LOGGED_IN_USER("previouslyLoggedInUser"),

        ;

        private String key;

        PreferenceKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private final Context context;
    private final SharedPreferences sharedPreferences;
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

    public void setPreviouslyEnteredPhoneNo(String previouslyEnteredPhoneNo) {
        sharedPreferences.edit().putString(PreferenceKey.PHONE_NO_ENTERED_BEFORE.getKey(), previouslyEnteredPhoneNo).apply();
    }

    public String getPreviouslyEnteredPhoneNo() {
        return sharedPreferences.getString(PreferenceKey.PHONE_NO_ENTERED_BEFORE.getKey(), "");
    }

    /**
     * https://stackoverflow.com/a/22985657/8928251
     *
     * @param arrayList Module.getContactArrayList
     */
    public void setContactArrayList(@NonNull ArrayList<PhoneContactModel> arrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        sharedPreferences.edit().putString(PreferenceKey.PHONE_CONTACT_ARRAYLIST.getKey(), json).apply();
    }

    /**
     * https://stackoverflow.com/a/22985657/8928251
     *
     * @return Module.getContactArrayList
     */
    @Nullable
    public ArrayList<PhoneContactModel> getContactArrayList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PreferenceKey.PHONE_CONTACT_ARRAYLIST.getKey(), "");
        Type type = new TypeToken<ArrayList<PhoneContactModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void setCurrentUserModel(@NonNull UserModel userModel) {
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        sharedPreferences.edit().putString(PreferenceKey.PREVIOUSLY_LOGGED_IN_USER.getKey(), json).apply();
    }

    @Nullable
    public UserModel getCurrentUserModel() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PreferenceKey.PREVIOUSLY_LOGGED_IN_USER.getKey(), "");
        Type type = new TypeToken<UserModel>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
