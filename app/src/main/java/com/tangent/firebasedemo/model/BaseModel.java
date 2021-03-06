package com.tangent.firebasedemo.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;

public abstract class BaseModel implements Cloneable, Serializable {
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @NonNull
    public static String ifNullThenString(@Nullable String s) {
        return s == null ? "null" : s;
    }

    @NonNull
    public static Object ifNullThenString(@Nullable Object o) {
        return o == null ? "null" : o;
    }
}
