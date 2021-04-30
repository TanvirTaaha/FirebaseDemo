package com.tangent.firebasedemo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.tangent.firebasedemo.view_model.VerifyEmailActivityViewModel;

import timber.log.Timber;

public class VerifyEmailActivity extends AppCompatActivity {

    //widgets
    private Button btnVerify;
    private TextView tvVerifyEmail;
    private TextView tvVerified;
    private ProgressBar pbVerifying;

    //vars
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        Timber.i(getLifecycle().getCurrentState().name());

        VerifyEmailActivityViewModel viewModel = new ViewModelProvider(this).get(VerifyEmailActivityViewModel.class);
        TextView tvrandom = findViewById(R.id.tvrandom);
        tvrandom.setText("RandNumber:" + viewModel.getNumber());
    }
}