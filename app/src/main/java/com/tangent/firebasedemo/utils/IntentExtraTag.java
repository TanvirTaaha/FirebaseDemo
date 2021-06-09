package com.tangent.firebasedemo.utils;

public enum IntentExtraTag {
    PHONE_TO_VERIFY("phoneNumberToVerify"),
    NEW_AUTHENTICATED_USER("newAuthenticatedUser"),
    PREVIOUSLY_LOGGED_IN_USER("previouslyLoggedInUser"),
    PERMISSION_CONTACT_READ("permissionForContactRead"),

    ;

    private final String tag;

    IntentExtraTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
