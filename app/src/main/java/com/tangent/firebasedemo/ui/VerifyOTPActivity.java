package com.tangent.firebasedemo.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ActivityVerifyOtpBinding;
import com.tangent.firebasedemo.utils.IntentExtraTag;
import com.tangent.firebasedemo.utils.Util;

import timber.log.Timber;

public class VerifyOTPActivity extends AppCompatActivity {

    //vars
    private FirebaseAuth mAuth;
    private ActivityVerifyOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Timber.i(getLifecycle().getCurrentState().name());
        String phoneNoToVerify = "";
        if (getIntent().hasExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag())) {
            phoneNoToVerify = getIntent().getStringExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag());
        }
        if (Util.isNotEmpty(phoneNoToVerify)) {
            binding.tvTopText.setText(Html.fromHtml(getString(R.string.enter_otp_sent_to_phone_no, phoneNoToVerify)));
        } else {
            binding.tvTopText.setText(R.string.couldn_t_retrieve_the_phone_no);
        }

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
                    Timber.i("got here %s %s", finalI, keyCode);
                }
                return false;
            });
            tiet.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus && finalI > 0) {
                    EditText prevET = binding.llOTPEditText.getChildAt(finalI - 1).findViewById(R.id.edittext);
                    if (prevET.getText().toString().isEmpty()) {
                        Util.showKeyboard(prevET);
                    }
                    if (Util.isNotEmpty(tiet.getText()) && finalI < binding.llOTPEditText.getChildCount()) {
                        Util.showKeyboard(binding.llOTPEditText.getChildAt(finalI + 1).findViewById(R.id.edittext));
                    }
                }
            });
        }

        SignUpViewModel viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        viewModel.getCodeFromSMS().observe(this, this::setPinDigits);
    }

    private void setPinDigits(String s) {
        if (s.length() == 6) {
            for (int i = 0; i < 6; i++) {
                ((EditText) binding.llOTPEditText.getChildAt(i).findViewById(R.id.edittext)).setText(s.charAt(i));
            }
        }
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
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }
    }

}