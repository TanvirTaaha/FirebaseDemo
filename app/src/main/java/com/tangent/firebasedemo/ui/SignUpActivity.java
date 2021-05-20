package com.tangent.firebasedemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.tangent.firebasedemo.IView;
import com.tangent.firebasedemo.databinding.ActivitySignUpBinding;
import com.tangent.firebasedemo.model.SignUpModel;
import com.tangent.firebasedemo.presenter.BaseBody;
import com.tangent.firebasedemo.presenter.Presenter;
import com.tangent.firebasedemo.utils.PreferenceManager;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements IView<SignUpModel> {

    //widgets
    private ActivitySignUpBinding binding;

    //vars
    private String mUserName;
    private String mEmail;
    private String mPassword;
    private PreferenceManager preferenceManager;
    private SignUpPresenter signUpPresenter;
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        preferenceManager = new PreferenceManager(this);
        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);


        binding.btnAlreadyRegisteredSignIn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LogInActivity.class)));

        preferenceManager = new PreferenceManager(this);
        signUpPresenter = new SignUpPresenter(this, this);


        binding.btnAlreadyRegisteredSignIn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LogInActivity.class)));

        binding.btnSignUp.setOnClickListener(v -> {
            v.setEnabled(false);
            mUserName = Objects.requireNonNull(binding.tietName.getText()).toString().trim();
            mEmail = Objects.requireNonNull(binding.tietEmail.getText()).toString().trim();
            mPassword = Objects.requireNonNull(binding.tietPassword.getText()).toString().trim();

            boolean dataValid = true;
            if (TextUtils.isEmpty(mUserName)) {
                dataValid = false;
                binding.tilName.setError("Name can't be blank");
            }
            if (TextUtils.isEmpty(mPassword)) {
                dataValid = false;
                binding.tilPassword.setError("Password can't be blank");
            }
            if (TextUtils.isEmpty(mEmail)) {
                dataValid = false;
                binding.tilEmail.setError("Email can't be blank");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                dataValid = false;
                binding.tilEmail.setError("Please enter a valid e-mail");
            }
            if (dataValid) {
                binding.rlProgressbarContainer.setVisibility(View.VISIBLE);
//                viewModel.sendToSignUp();
            }
        });
    }


//    private void sendToSignUp() {
//        binding.rlProgressbarContainer.setVisibility(View.VISIBLE);
//        signUpPresenter.sendRequest(new SignUpPresenter.SignUpBody(mUserName, mEmail, mPassword));
//        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//            if (user != null) {
//                user.sendEmailVerification().addOnCompleteListener(task -> {
//
//                });
//            }
//        });
//    }

    @Override
    public void onResponseSuccess(SignUpModel signUpModel, Presenter name) {
        binding.rlProgressbarContainer.setVisibility(View.GONE);
//        Timber.d("Successful:%s, userId:%s", signUpModel.getSuccessful(), signUpModel.getUserId());
        Snackbar.make(binding.rlProgressbarContainer, "Sign up success", Snackbar.LENGTH_SHORT).show();


        preferenceManager.setUsername(mUserName);
        preferenceManager.setEmail(mEmail);
        preferenceManager.setPassword(mPassword);
        preferenceManager.setUserId(signUpModel.getUserId());
//        goToMainActivity();
    }

    @Override
    public void onResponseFailure() {
        binding.rlProgressbarContainer.setVisibility(View.GONE);
        Snackbar.make(binding.rlProgressbarContainer, "Something went wrong", Snackbar.LENGTH_SHORT).show();

        binding.btnSignUp.setEnabled(true);
    }

    private void goToMainActivity() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    private static class SignUpPresenter {
        FirebaseAuth mFirebaseAuth;
        Activity activity;
        IView<SignUpModel> iView;

        public SignUpPresenter(Activity activity, IView<SignUpModel> view) {
//            super(context, view, Presenter.SIGN_UP_PRESENTER);
            mFirebaseAuth = FirebaseAuth.getInstance();
            this.activity = activity;
            this.iView = view;
        }

        public static class SignUpBody extends BaseBody {
            String username;
            String email;
            String password;

            public SignUpBody(String username, String email, String password) {
                this.username = username;
                this.email = email;
                this.password = password;
            }
        }

        public void sendRequest(SignUpBody signUpBody) {
            mFirebaseAuth.createUserWithEmailAndPassword(signUpBody.email, signUpBody.password).addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    try {
                        iView.onResponseSuccess(new SignUpModel("Success", Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid()), Presenter.SIGN_UP_PRESENTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(activity, "Registration Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(activity, e -> {
                e.printStackTrace();
                iView.onResponseFailure();
            });
        }
    }
}