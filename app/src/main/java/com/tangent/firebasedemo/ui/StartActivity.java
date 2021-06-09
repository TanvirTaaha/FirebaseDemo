package com.tangent.firebasedemo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tangent.firebasedemo.app.App;
import com.tangent.firebasedemo.databinding.ActivityStartBinding;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.repo.MessagesDatabase;
import com.tangent.firebasedemo.repo.PhoneContactsRepo;
import com.tangent.firebasedemo.ui.main.HomeActivity;
import com.tangent.firebasedemo.utils.IntentExtraTag;

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

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
            if (currentUser != null && !TextUtils.isEmpty(currentUser.getPhoneNumber())) {
                UserModel userModel = MessagesDatabase.getInstance().getUserFromInternet(currentUser.getUid()).getValue();
                Intent i = new Intent(StartActivity.this, HomeActivity.class);
                i.putExtra(IntentExtraTag.PREVIOUSLY_LOGGED_IN_USER.getTag(), userModel);
                startActivity(i);
            } else {
                startActivity(new Intent(StartActivity.this, FirstSignupActivity.class));
            }
            finish();
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (permissionGranted()) {
            try {
                Timber.i("Loading Contacts");
                new PhoneContactsRepo(this, ((App) getApplication()).getExecutorService())
                        .loadContacts(true);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean permissionGranted() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
}