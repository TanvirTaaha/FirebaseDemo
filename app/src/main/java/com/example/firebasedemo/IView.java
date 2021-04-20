package com.example.firebasedemo;

import com.example.firebasedemo.model.BaseModel;
import com.example.firebasedemo.presenter.Presenter;

public interface IView<M extends BaseModel> {
    void onResponseSuccess(M m, Presenter name);
    void onResponseFailure();
}