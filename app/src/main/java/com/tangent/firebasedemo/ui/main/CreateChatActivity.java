package com.tangent.firebasedemo.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tangent.firebasedemo.databinding.ActivityCreateChatBinding;

public class CreateChatActivity extends AppCompatActivity {
    ActivityCreateChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}