package com.tangent.firebasedemo.utils;

public class Util {

    //Database related items
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact_db";
    public static final String TABLE_NAME = "contacts";

    //Contacts table column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NUMBER = "phone_number";

    //FirebaseDatabase constants
    public static final String BRANCH_MESSAGES = "messages";
    public static final String BRANCH_USERS = "users";
}
