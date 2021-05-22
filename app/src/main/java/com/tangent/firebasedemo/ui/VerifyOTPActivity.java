package com.tangent.firebasedemo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ActivityVerifyOtpBinding;
import com.tangent.firebasedemo.utils.IntentExtraTag;
import com.tangent.firebasedemo.utils.PreferenceManager;
import com.tangent.firebasedemo.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class VerifyOTPActivity extends AppCompatActivity {

    //vars
    private ActivityVerifyOtpBinding binding;
    private String phoneNoToVerify;
    private String mVerificationId;
    private String mOTP;
    private PhoneAuthProvider.ForceResendingToken token;
    private static final long CODE_TIMEOUT = 60L;
    private FirebaseAuth mAuth;
    private PreferenceManager preferenceManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        if (getIntent().hasExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag())) {
            phoneNoToVerify = getIntent().getStringExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag());
            binding.tvTopText.setText(Html.fromHtml(getString(R.string.enter_otp_sent_to_phone_no, phoneNoToVerify)));
            sendOTP(false);
        } else {
            phoneNoToVerify = "";
            binding.tvTopText.setText(R.string.couldn_t_retrieve_the_phone_no);
        }
        Timber.i("phoneNoToVerify %s", phoneNoToVerify);

        binding.ibBackPress.setOnClickListener(v -> onBackPressed());

        setupPinEditTextArray();

        binding.btnVerify.setOnClickListener(v -> {
            if (getPinDigits().length() != 6) {
                binding.tvOTPError.setText(R.string.fill_up_the_code);
                binding.tvOTPError.setVisibility(View.VISIBLE);
                return;
            }
            binding.tvOTPError.setVisibility(View.GONE);
            mOTP = getPinDigits();
            authenticateUser(PhoneAuthProvider.getCredential(mVerificationId, mOTP));
        });
    }

    /**
     * https://stackoverflow.com/q/65561826/8928251
     * it sucks, the activity is needed because of the reCAPTCHA thing
     */
    private void sendOTP(boolean resend) {
        PhoneAuthOptions.Builder optionsBuilder = PhoneAuthOptions.newBuilder(mAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNoToVerify)
                .setTimeout(CODE_TIMEOUT, TimeUnit.SECONDS)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                        authenticateUser(phoneAuthCredential);
                        Timber.v("Verification auto completed:%s", phoneAuthCredential.toString());
                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Snackbar.make(binding.tvTopText, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                        Timber.e("Verification failed %s", e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        mVerificationId = s;
                        token = forceResendingToken;
                        Timber.v("Code sent to %s: verificationId:%s, token:%s", phoneNoToVerify, s, forceResendingToken.toString());
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Timber.v("Code auto retrieval time out: %s", s);
                    }
                });
        if (resend) {
            if (token == null) {
                Timber.e("resend token is null");
                return;
            }
            optionsBuilder.setForceResendingToken(token);
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build());
    }

    private void authenticateUser(@NonNull PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            Timber.i("task is successful, result:%s", authResult.toString());
            updateUi(authResult.getUser());
        }).addOnFailureListener(exception -> {
            Timber.i("task is failed, message:%s", exception.getMessage());
            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                binding.tvOTPError.setText(R.string.wrong_otp_error_message);
            }
        });
    }

    private void updateUi(FirebaseUser user) {

    }

    private void setupPinEditTextArray() {
        for (int i = 0; i < binding.llOTPEditText.getChildCount(); i++) {
            int finalI = i;
            TextInputEditText tiet = binding.llOTPEditText.getChildAt(i).findViewById(R.id.edittext);
            tiet.addTextChangedListener(new OTPTextWatcher(i + 1));
            tiet.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false; //Don't get confused by this, it is because onKeyListener is called twice and this condition is to avoid it.
                }
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && TextUtils.isEmpty(tiet.getText())
                        && finalI != 0) { //this condition is to handle the delete input by users.
                    //delete the digit
                    ((EditText) binding.llOTPEditText.getChildAt(finalI - 1).findViewById(R.id.edittext)).setText("");
                    //and set focus to previous edittext
                    binding.llOTPEditText.getChildAt(finalI - 1).requestFocus();
                }
                if (keyCode == KeyEvent.KEYCODE_DEL && Util.isNotEmpty(tiet.getText()) && finalI == binding.llOTPEditText.getChildCount() - 1) {
                    tiet.setSelection(1);
                }
                return false;
            });
            tiet.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus && finalI > 0) {
                    EditText prevET = binding.llOTPEditText.getChildAt(finalI - 1).findViewById(R.id.edittext);
                    if (prevET.getText().toString().isEmpty()) {
                        Util.showKeyboard(prevET);
                    }
                    if (Util.isNotEmpty(tiet.getText()) && finalI < binding.llOTPEditText.getChildCount() - 1) {
                        Util.showKeyboard(binding.llOTPEditText.getChildAt(finalI + 1).findViewById(R.id.edittext));
                    }
                }
            });
        }
    }

    private void setPinDigits(String s) {
        if (s.length() == 6) {
            for (int i = 0; i < 6; i++) {
                ((EditText) binding.llOTPEditText.getChildAt(i).findViewById(R.id.edittext)).setText(s.substring(i, i + 1));
            }
        }
    }

    @NonNull
    private String getPinDigits() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            s.append(((EditText) binding.llOTPEditText.getChildAt(i).findViewById(R.id.edittext)).getText());
        }
        return s.toString();
    }

    private void showProgressDialog() {
//        mProgressDialog = new ProgressDialog()
    }

    private void hideProgressDialog() {

    }

    class OTPTextWatcher implements TextWatcher {
        int position;

        private OTPTextWatcher(int position) {
            this.position = position;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (position) {
                case 1:
                    if (text.length() == 1)
                        binding.otpDigit2.edittext.requestFocus();
                    break;
                case 2:
                    if (text.length() == 1)
                        binding.otpDigit3.edittext.requestFocus();
                    else if (text.length() == 0)
                        binding.otpDigit1.edittext.requestFocus();
                    break;
                case 3:
                    if (text.length() == 1)
                        binding.otpDigit4.edittext.requestFocus();
                    else if (text.length() == 0)
                        binding.otpDigit2.edittext.requestFocus();
                    break;
                case 4:
                    if (text.length() == 1)
                        binding.otpDigit5.edittext.requestFocus();
                    else if (text.length() == 0)
                        binding.otpDigit3.edittext.requestFocus();
                    break;
                case 5:
                    if (text.length() == 1)
                        binding.otpDigit6.edittext.requestFocus();
                    else if (text.length() == 0)
                        binding.otpDigit4.edittext.requestFocus();
                    break;
                case 6:
                    if (text.length() == 0)
                        binding.otpDigit5.edittext.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }
}