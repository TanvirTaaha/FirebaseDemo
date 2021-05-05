package com.tangent.firebasedemo.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

import timber.log.Timber;

public class VerifyEmailActivityViewModel extends ViewModel {
    private Integer number;
    private LiveData<Integer> integerLiveData;

    public Integer getNumber() {
        if (number == null) {
            createNumber();
        }
        return number;
    }

    public void createNumber() {
        number = new Random().nextInt(10);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.v("VerifyEmailActivityViewModel cleared");
    }


    public LiveData<Integer> getIntegerLiveData() {
        if (integerLiveData == null) {
            createIntegerLiveData();
        }
        return integerLiveData;
    }

    private void createIntegerLiveData() {
        integerLiveData = new LiveData<Integer>(number) {
            @Override
            protected void postValue(Integer value) {
                super.postValue(value);
            }

            @Override
            protected void setValue(Integer value) {
                super.setValue(value);
            }
        };
    }

}
