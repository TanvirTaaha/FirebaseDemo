package com.tangent.firebasedemo.view_model;

import androidx.lifecycle.ViewModel;

import java.util.Random;

public class VerifyEmailActivityViewModel extends ViewModel {
    private Integer number;

    public Integer getNumber() {
        if (number == null) {
            createNumber();
        }
        return number;
    }

    public void createNumber() {
        number = new Random().nextInt(10);
    }
}
