package com.example.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btngotosignUp = findViewById(R.id.btngotosignUp);

        btngotosignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                finish();
            }
        });

        btngotologIn = findViewById(R.id.btngotologIn);
        btngotologIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();
            }
        });

        tvLoggedIn = findViewById(R.id.tvLoggedIn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if signed in already;
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null && !TextUtils.isEmpty(currentUser.getEmail())) {
//            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            tvLoggedIn.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }
}