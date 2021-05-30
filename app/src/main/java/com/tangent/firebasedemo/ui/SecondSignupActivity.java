package com.tangent.firebasedemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.tangent.firebasedemo.data.MessagesDatabase;
import com.tangent.firebasedemo.databinding.ActivitySecondSignupBinding;
import com.tangent.firebasedemo.model.firebasemodel.InboxItem;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.ui.main.HomeActivity;
import com.tangent.firebasedemo.utils.IntentExtraTag;

import java.util.ArrayList;

import timber.log.Timber;

public class SecondSignupActivity extends AppCompatActivity {

    private ActivitySecondSignupBinding binding;
    private UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra(IntentExtraTag.NEW_AUTHENTICATED_USER.getTag())) {
            mUserModel = (UserModel) getIntent().getSerializableExtra(IntentExtraTag.NEW_AUTHENTICATED_USER.getTag());
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.btnNext.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etName.getText())) {
                Snackbar.make(v, "Name can not be empty", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String name = binding.etName.getText().toString().trim();
            mUserModel.setName(name);
            ArrayList<InboxItem> inbox = new ArrayList<>();
            mUserModel.setInbox(inbox); //empty inbox
            MessagesDatabase database = MessagesDatabase.getInstance();
            database.createUser(mUserModel)
                    .addOnSuccessListener(unused -> {
                        Intent i = new Intent(SecondSignupActivity.this, HomeActivity.class);
                        i.putExtra(IntentExtraTag.PREVIOUSLY_LOGGED_IN_USER.getTag(), mUserModel);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Snackbar.make(v, "Server error!", Snackbar.LENGTH_SHORT).show();
                        Timber.e(e);
                        e.printStackTrace();
                    });
        });
    }
}