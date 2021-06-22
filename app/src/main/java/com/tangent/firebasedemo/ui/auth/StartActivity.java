package com.tangent.firebasedemo.ui.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tangent.firebasedemo.databinding.ActivityStartBinding;
import com.tangent.firebasedemo.repository.PhoneContactsRepo;
import com.tangent.firebasedemo.ui.home.HomeActivity;
import com.tangent.firebasedemo.utils.PreferenceManager;
import com.tangent.firebasedemo.utils.Util;

import timber.log.Timber;

public class StartActivity extends AppCompatActivity {

    //vars
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStartBinding binding;
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFirebaseAuth = FirebaseAuth.getInstance();

        //load contacts in the background
        if (permissionGranted()) {
            try {
                Timber.i("Loading Contacts");
                PhoneContactsRepo.getInstance().refreshContent();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            FirebaseUser authCurrentUser = mFirebaseAuth.getCurrentUser();
            String _uid = new PreferenceManager(this).getUserId();
            if (authCurrentUser != null && Util.isNotEmpty(_uid) && _uid.equals(authCurrentUser.getUid())) {
                startActivity(new Intent(StartActivity.this, HomeActivity.class));
            } else {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StartActivity.this, FirstSignupActivity.class));
            }
            finish();
        }, 1000);
    }

    public boolean permissionGranted() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
}