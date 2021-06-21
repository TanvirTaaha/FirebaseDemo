package com.tangent.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.tangent.firebasedemo.databinding.ActivitySecondSignupBinding;
import com.tangent.firebasedemo.model.firebasemodel.InboxItem;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.repository.FirebaseDatabaseRepo;
import com.tangent.firebasedemo.ui.home.HomeActivity;
import com.tangent.firebasedemo.utils.IntentExtraTag;
import com.tangent.firebasedemo.utils.PreferenceManager;

import java.util.ArrayList;

import timber.log.Timber;

public class SecondSignupActivity extends AppCompatActivity {

    private ActivitySecondSignupBinding binding;
    private UserModel mUserModel;
    private ProgressDialog mProgressDialog;

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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Connecting");
        mProgressDialog.setCanceledOnTouchOutside(false);

        binding.btnNext.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etName.getText())) {
                Snackbar.make(v, "Name can not be empty", Snackbar.LENGTH_SHORT).show();
                return;
            }
            mProgressDialog.show();
            String name = binding.etName.getText().toString().trim();
            mUserModel.setName(name);
            ArrayList<InboxItem> inbox = new ArrayList<>();
            mUserModel.setInbox(inbox); //empty inbox
            FirebaseDatabaseRepo firebaseDBRepo = FirebaseDatabaseRepo.getInstance();
            firebaseDBRepo.createNewUser(mUserModel, task -> {
                if (task.isSuccessful()) {
                    PreferenceManager _pref = new PreferenceManager(SecondSignupActivity.this);
                    _pref.setUserId(mUserModel.getId());
                    _pref.setCurrentUserModel(mUserModel);
                    startActivity(new Intent(SecondSignupActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Snackbar.make(v, "Server error!", Snackbar.LENGTH_SHORT).show();
                    Timber.e(task.getException());
                    if (task.getException() != null) task.getException().printStackTrace();
                }
                mProgressDialog.dismiss();
            });
        });
    }
}