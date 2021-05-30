package com.tangent.firebasedemo.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tangent.firebasedemo.databinding.ActivityHomeBinding;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.utils.IntentExtraTag;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra(IntentExtraTag.PREVIOUSLY_LOGGED_IN_USER.getTag())) {
            mUserModel = (UserModel) getIntent().getSerializableExtra(IntentExtraTag.PREVIOUSLY_LOGGED_IN_USER.getTag());
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            finish();
        }

        HomeActPagerAdapter homeActPagerAdapter = new HomeActPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(homeActPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}