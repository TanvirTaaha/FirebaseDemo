package com.example.firebasedemo.presenter;

import androidx.annotation.NonNull;

public enum Presenter {
    BASE_PRESENTER("basePresenter"),
    LOG_IN_PRESENTER("logInPresenter"),
    SIGN_UP_PRESENTER("signUpPresenter"),
    TUITION_INFO_LIST_PRESENTER("tuitionInfoListFullDataPresenter"),

    ;

    private final String name;

    Presenter(final String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
