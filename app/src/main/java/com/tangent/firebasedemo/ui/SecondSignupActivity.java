package com.tangent.firebasedemo.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tangent.firebasedemo.databinding.ActivitySecondSignupBinding;

public class SecondSignupActivity extends AppCompatActivity {

    private ActivitySecondSignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}