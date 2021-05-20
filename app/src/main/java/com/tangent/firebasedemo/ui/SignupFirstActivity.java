package com.tangent.firebasedemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ActivitySignupFirstBinding;
import com.tangent.firebasedemo.utils.IntentExtraTag;

public class SignupFirstActivity extends AppCompatActivity {
    //vars
    private ActivitySignupFirstBinding binding;
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        binding.tvAppNameMessage.setText(getString(R.string.app_name_will_send_an_sms_message_to_verify_your_phone_number, getString(R.string.app_name)));

        binding.ccp.registerCarrierNumberEditText(binding.tietPhone);
        binding.btnContinue.setOnClickListener(v -> {
            if (binding.ccp.isValidFullNumber()) {
                showDialog(binding.ccp.getFullNumberWithPlus());
            } else {
                binding.tilPhone.setError("Country and phone no doesn't match");
            }
        });
        binding.tietPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.ccp.isValidFullNumber()) {
                    binding.tilPhone.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // be very careful, EASILY GOES TO INFINITY LOOP
                if (s.length() > 4) {
                    if (s.toString().indexOf(binding.ccp.getSelectedCountryCodeWithPlus()) == 0) {
                        s.delete(0, binding.ccp.getSelectedCountryCodeWithPlus().length());
                    } else if (s.toString().indexOf(binding.ccp.getSelectedCountryCode()) == 0) {
                        s.delete(0, binding.ccp.getSelectedCountryCode().length());
                    } else if (binding.ccp.getSelectedCountryNameCode().equals("BD") && s.charAt(0) == '0') {
                        s.delete(0, 1);
                    }
                    if (s.length() > 4) { //if still long enough
                        int index = s.toString().indexOf("-");
                        if (index < 0) {
                            s.insert(4, "-");
                        } else if (index != 4) {
                            s.delete(index, index + 1);
                            s.insert(4, "-");
                        }
                    }
                }
            }
        });
    }

    private void showDialog(String phoneNoStr) {
        new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml(getString(R.string.sure_to_send_an_sms, phoneNoStr)))
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> viewModel.sendVerificationCodeToPhone(phoneNoStr)
                                .observe(SignupFirstActivity.this, aBoolean -> {
                                    if (aBoolean) {
                                        Intent i = new Intent(SignupFirstActivity.this,
                                                VerifyOTPActivity.class);
                                        i.putExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag(), phoneNoStr);
                                        startActivity(i);
                                    }
                                }))
                .setNegativeButton(R.string.edit, (dialog, which) -> dialog.dismiss())
                .show();
    }
}