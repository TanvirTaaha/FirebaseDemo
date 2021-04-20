package com.example.firebasedemo.presenter;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public abstract class BaseBody {
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
