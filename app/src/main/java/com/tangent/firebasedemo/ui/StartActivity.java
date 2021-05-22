package com.tangent.firebasedemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tangent.firebasedemo.R;

public class StartActivity extends AppCompatActivity {

    //vars
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mFirebaseAuth;

    //widgets
    private Button btngotosignUp;
    private Button btngotologIn;
    private TextView tvLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btngotosignUp = findViewById(R.id.btngotosignUp);

        btngotosignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, SignUpActivity.class));
                finish();
            }
        });

        btngotologIn = findViewById(R.id.btngotologIn);
        btngotologIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LogInActivity.class));
                finish();
            }
        });

        tvLoggedIn = findViewById(R.id.tvLoggedIn);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
            if (currentUser != null && !TextUtils.isEmpty(currentUser.getPhoneNumber())) {
                tvLoggedIn.setVisibility(View.VISIBLE);
                startActivity(new Intent(StartActivity.this, HomeActivity2.class));
            } else {
                startActivity(new Intent(StartActivity.this, SignupFirstActivity.class));
            }
            finish();
        }, 1000);
    }

}