package com.example.firebasedemo;

import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasedemo.model.LogInModel;
import com.example.firebasedemo.presenter.BaseBody;
import com.example.firebasedemo.presenter.Presenter;
import com.example.firebasedemo.utils.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity implements IView<LogInModel> {
    //widgets
    private ImageView ivLogoImage;
    private Button btnSignIn;
    private Button btnForgotPassword;
    private Button btnNewUserSignUp;
    private LinearLayout llLogoContainer;
    private LinearLayout llButtonsContainer;
    private RelativeLayout rlProgressbarContainer;
    private TextInputEditText tietEmail;
    private TextInputEditText tietPassword;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;

    //vars
    private String mEmail;
    private String mPassword;
    private LogInPresenter logInPresenter;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        ivLogoImage = findViewById(R.id.ivLogoImage);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnNewUserSignUp = findViewById(R.id.btnNewUserSignUp);
        tietEmail = findViewById(R.id.tietEmail);
        tietPassword = findViewById(R.id.tietPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        llLogoContainer = findViewById(R.id.llLogoContainer);
        llButtonsContainer = findViewById(R.id.llButtonsContainer);
        rlProgressbarContainer = findViewById(R.id.rlProgressbarContainer);



        logInPresenter = new LogInPresenter(this, this);
        preferenceManager = new PreferenceManager(this);


//        btnNewUserSignUp.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, SignUpActivity.class)));

        btnSignIn.setOnClickListener(v -> {
            v.setEnabled(false);
            mEmail = Objects.requireNonNull(tietEmail.getText()).toString().trim();
            mPassword = Objects.requireNonNull(tietPassword.getText()).toString().trim();

            boolean dataValid = true;
            if (TextUtils.isEmpty(mPassword)) {
                dataValid = false;
                tilPassword.setError("Password can't be blank");
            }
            if (TextUtils.isEmpty(mEmail)) {
                dataValid = false;
                tilEmail.setError("Please enter your e-mail");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                dataValid = false;
                tilEmail.setError("Please enter a valid e-mail");
            }
            if (dataValid) {
                sendToLogIn();
            }
        });


//        setSlideSharedAnimation();
    }

//    private void setSlideSharedAnimation() {
//        Slide slide = new Slide(Gravity.BOTTOM);
//        slide.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_linear_in));
//        slide.excludeTarget(android.R.id.statusBarBackground, true);
//        getWindow().setEnterTransition(slide);
//    }

    private void sendToLogIn() {
        rlProgressbarContainer.setVisibility(View.VISIBLE);
        logInPresenter.sendRequest(new LogInPresenter.LogInBody(mEmail, mPassword));
    }

    @Override
    public void onResponseSuccess(LogInModel logInModel, Presenter name) {
        rlProgressbarContainer.setVisibility(View.GONE);
//        Timber.d(logInModel.getAccess_token());
        Snackbar.make(rlProgressbarContainer, "Log in success", Snackbar.LENGTH_SHORT).show();


//        preferenceManager.setLoggedIn(true);
//        preferenceManager.setAccessToken(logInModel.getAccess_token());
//        preferenceManager.setEmail(mEmail);
//        preferenceManager.setPassword(mPassword);
        goToMainActivity();
    }

    @Override
    public void onResponseFailure() {
        rlProgressbarContainer.setVisibility(View.GONE);
        Snackbar.make(rlProgressbarContainer, "Something went wrong", Snackbar.LENGTH_SHORT).show();

        btnSignIn.setEnabled(true);
    }

    private void goToMainActivity() {
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
    }

    private static class LogInPresenter{

        public LogInPresenter(Context context, IView<LogInModel> view) {
//            super(context, view, Presenter.LOG_IN_PRESENTER);
        }

        public static class LogInBody extends BaseBody {
            String email;
            String password;

            public LogInBody(String email, String password) {
                this.email = email;
                this.password = password;
            }
        }


        public void sendRequest(LogInBody logInBody){

        }
    }
}