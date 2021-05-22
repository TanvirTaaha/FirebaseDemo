package com.tangent.firebasedemo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.tangent.firebasedemo.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private ProgressDialog mProgressDialog;
    private CountDownTimer mCountDownTimer;
    private List<TextInputEditText> mEditTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Connecting");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        mCountDownTimer = new CountDownTimer(TimeUnit.SECONDS.toMillis(CODE_TIMEOUT), TimeUnit.SECONDS.toMillis(1)) {
            @Override
            public void onTick(long millisUntilFinished) {
                String strTime = simpleDateFormat.format(new Date(millisUntilFinished));
                binding.tvTimer.setText(strTime);
            }

            @Override
            public void onFinish() {
                Timber.i("Clock finished");
                binding.tvTimer.setVisibility(View.GONE);
                binding.btnResend.setEnabled(true);
            }
        };

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

        binding.btnResend.setOnClickListener(v -> sendOTP(true));

        if (getIntent().hasExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag())) {
            phoneNoToVerify = getIntent().getStringExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag());
            binding.tvTopText.setText(Html.fromHtml(getString(R.string.enter_otp_sent_to_phone_no, phoneNoToVerify)));
            sendOTP(false);
        } else {
            phoneNoToVerify = "";
            binding.tvTopText.setText(R.string.couldn_t_retrieve_the_phone_no);
        }
        Timber.i("phoneNoToVerify %s", phoneNoToVerify);
    }

    /**
     * https://stackoverflow.com/q/65561826/8928251
     * it sucks, the activity is needed because of the reCAPTCHA thing
     */
    private void sendOTP(boolean resend) {
        binding.btnResend.setEnabled(false);
        PhoneAuthOptions.Builder optionsBuilder = PhoneAuthOptions.newBuilder(mAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNoToVerify)
                .setTimeout(CODE_TIMEOUT, TimeUnit.SECONDS)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                        Timber.v("Verification auto completed:%s", phoneAuthCredential.toString());
                        authenticateUser(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Timber.e("Verification failed %s", e.getMessage());
                        Snackbar.make(binding.tvTopText, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        Timber.v("Code sent to %s: verificationId:%s, token:%s", phoneNoToVerify, s, forceResendingToken.toString());
                        mVerificationId = s;
                        token = forceResendingToken;
                        startTimer();
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Timber.v("Code auto retrieval time out: %s", s);
                        dismissTimer();
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
        mProgressDialog.show();
        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            Timber.i("task is successful, result:%s", authResult.toString());
            updateUi(authResult.getUser());
        }).addOnFailureListener(exception -> {
            mProgressDialog.dismiss();
            Timber.i("task is failed, message:%s", exception.getMessage());
            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                binding.tvOTPError.setText(R.string.wrong_otp_error_message);
            }
        });
    }

    private void startTimer() {
        binding.btnResend.setEnabled(false);
        binding.tvTimer.setVisibility(View.VISIBLE);
        mCountDownTimer.start();
        Timber.v("Timer started called");
    }

    private void dismissTimer() {
        binding.btnResend.setEnabled(true);
        binding.tvTimer.setVisibility(View.GONE);
        mCountDownTimer.cancel();
        Timber.v("Timer dismissed");
    }

    private void updateUi(@Nullable FirebaseUser user) {
        if (user == null) {
            Snackbar.make(binding.tvTopText, "User verification failed!", Snackbar.LENGTH_SHORT).show();
            Timber.e("User verification failed null user returned");
            return;
        }
        Timber.v("updateUi:Phone Sign in success:%s", user.getPhoneNumber());
        mProgressDialog.dismiss();
        Snackbar.make(binding.tvTopText, "User verification Success!", Snackbar.LENGTH_SHORT).show();//todo
    }

    private void setupPinEditTextArray() {
        mEditTextList = new ArrayList<>();
        for (int i = 0; i < binding.llOTPEditText.getChildCount(); i++) {
            int finalI = i;
            TextInputEditText tiet = binding.llOTPEditText.getChildAt(i).findViewById(R.id.edittext);
            mEditTextList.add(tiet);
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
                }
                if (hasFocus && Util.isNotEmpty(tiet.getText()) && finalI < binding.llOTPEditText.getChildCount() - 1) {
                    Util.showKeyboard(binding.llOTPEditText.getChildAt(finalI + 1).findViewById(R.id.edittext));
                }
            });
        }
    }

    private void setPinDigits(CharSequence text) {
        if (text.length() == 6) {
            for (int i = 0; i < 6; i++) {
                ((EditText) binding.llOTPEditText.getChildAt(i).findViewById(R.id.edittext)).setText(text.subSequence(i, i + 1));
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

    class OTPTextWatcher implements TextWatcher {
        int position;

        private OTPTextWatcher(int position) {
            this.position = position;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            Timber.d("afterTextChanged: text:%s, position:%s", text, position);
            switch (position) {
                case 1:
                    if (text.length() == 1)
                        binding.otpDigit2.edittext.requestFocus();
                    break;
                case 6:
                    if (text.length() == 0)
                        binding.otpDigit5.edittext.requestFocus();
                    else if (text.length() == 1)
                        Util.hideKeyboard(binding.otpDigit6.edittext);
                    else
                        editable.delete(1, editable.length()); //never more than one digit
                    break;
                default:
                    if (text.length() == 0)
                        mEditTextList.get(position - 2).requestFocus(); //previous
                    else if (text.length() == 1)
                        mEditTextList.get(position).requestFocus(); //next
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Timber.i("Triggers on paste position:%d, text:%s", position, s);
            if (s.length() > 1) {
                setPinDigits(s);
            }
        }
    }
}