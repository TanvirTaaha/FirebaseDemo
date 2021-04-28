package com.tangent.firebasedemo;

import com.tangent.firebasedemo.model.BaseModel;
import com.tangent.firebasedemo.presenter.Presenter;

public interface IView<M extends BaseModel> {
    void onResponseSuccess(M m, Presenter name);
    void onResponseFailure();
}