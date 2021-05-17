package com.tangent.firebasedemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tangent.firebasedemo.IView;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.model.SignUpModel;
import com.tangent.firebasedemo.presenter.BaseBody;
import com.tangent.firebasedemo.presenter.Presenter;
import com.tangent.firebasedemo.utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements IView<SignUpModel> {

    //widgets
    private ImageView ivLogoImage;
    private Button btnSignUp;
    private Button btnAlreadyRegisteredSignIn;
    private LinearLayout llLogoContainer;
    private LinearLayout llButtonsContainer;
    private RelativeLayout rlProgressbarContainer;
    private TextInputEditText tietName;
    private TextInputEditText tietEmail;
    private TextInputEditText tietPassword;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;

    //vars
    private String mUserName;
    private String mEmail;
    private String mPassword;
    private PreferenceManager preferenceManager;
    private SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        ivLogoImage = findViewById(R.id.ivLogoImage);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnAlreadyRegisteredSignIn = findViewById(R.id.btnAlreadyRegisteredSignIn);
        llButtonsContainer = findViewById(R.id.llButtonsContainer);
        rlProgressbarContainer = findViewById(R.id.rlProgressbarContainer);
        tietName = findViewById(R.id.tietName);
        tietEmail = findViewById(R.id.tietEmail);
        tietPassword = findViewById(R.id.tietPassword);
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);


        preferenceManager = new PreferenceManager(this);
        signUpPresenter = new SignUpPresenter(this, this);


        btnAlreadyRegisteredSignIn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LogInActivity.class)));


        llLogoContainer = findViewById(R.id.llLogoContainer);
        llButtonsContainer = findViewById(R.id.llButtonsContainer);
        rlProgressbarContainer = findViewById(R.id.rlProgressbarContainer);
        tietName = findViewById(R.id.tietName);
        tietEmail = findViewById(R.id.tietEmail);
        tietPassword = findViewById(R.id.tietPassword);
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);


        preferenceManager = new PreferenceManager(this);
        signUpPresenter = new SignUpPresenter(this, this);


        btnAlreadyRegisteredSignIn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LogInActivity.class)));

        btnSignUp.setOnClickListener(v -> {
            v.setEnabled(false);
            mUserName = Objects.requireNonNull(tietName.getText()).toString().trim();
            mEmail = Objects.requireNonNull(tietEmail.getText()).toString().trim();
            mPassword = Objects.requireNonNull(tietPassword.getText()).toString().trim();

            boolean dataValid = true;
            if (TextUtils.isEmpty(mUserName)) {
                dataValid = false;
                tilName.setError("Name can't be blank");
            }
            if (TextUtils.isEmpty(mPassword)) {
                dataValid = false;
                tilPassword.setError("Password can't be blank");
            }
            if (TextUtils.isEmpty(mEmail)) {
                dataValid = false;
                tilEmail.setError("Email can't be blank");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                dataValid = false;
                tilEmail.setError("Please enter a valid e-mail");
            }
            if (dataValid) {
                sendToSignUp();
            }
        });
    }


    private void sendToSignUp() {
        rlProgressbarContainer.setVisibility(View.VISIBLE);
        signUpPresenter.sendRequest(new SignUpPresenter.SignUpBody(mUserName, mEmail, mPassword));
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                user.sendEmailVerification().addOnCompleteListener(task -> {

                });
            }
        });
    }

    @Override
    public void onResponseSuccess(SignUpModel signUpModel, Presenter name) {
        rlProgressbarContainer.setVisibility(View.GONE);
//        Timber.d("Successful:%s, userId:%s", signUpModel.getSuccessful(), signUpModel.getUserId());
        Snackbar.make(rlProgressbarContainer, "Sign up success", Snackbar.LENGTH_SHORT).show();


        preferenceManager.setUsername(mUserName);
        preferenceManager.setEmail(mEmail);
        preferenceManager.setPassword(mPassword);
        preferenceManager.setUserId(signUpModel.getUserId());
//        goToMainActivity();
    }

    @Override
    public void onResponseFailure() {
        rlProgressbarContainer.setVisibility(View.GONE);
        Snackbar.make(rlProgressbarContainer, "Something went wrong", Snackbar.LENGTH_SHORT).show();

        btnSignUp.setEnabled(true);
    }

    private void goToMainActivity() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    private static class SignUpPresenter{
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
            mFirebaseAuth.createUserWithEmailAndPassword(signUpBody.email, signUpBody.password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        iView.onResponseSuccess(new SignUpModel("Success", Objects.requireNonNull(task.getResult().getUser()).getUid()), Presenter.SIGN_UP_PRESENTER);
                        Toast.makeText(activity, "Registration Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(activity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    iView.onResponseFailure();
                }
            });
        }
    }
}