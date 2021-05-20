package com.tangent.firebasedemo.utils;

public enum IntentExtraTag {
    PHONE_TO_VERIFY("phoneNumberToVerify");

    private final String tag;

    IntentExtraTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
