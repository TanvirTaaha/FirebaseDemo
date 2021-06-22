package com.tangent.firebasedemo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.databinding.ActivityFirstSignupBinding;
import com.tangent.firebasedemo.utils.IntentExtraTag;
import com.tangent.firebasedemo.utils.PreferenceManager;
import com.tangent.firebasedemo.utils.Util;

public class FirstSignupActivity extends AppCompatActivity {
    //vars
    private ActivityFirstSignupBinding binding;
    private FirebaseAuth mAuth;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        binding.tvAppNameMessage.setText(getString(R.string.app_name_will_send_an_sms_message_to_verify_your_phone_number, getString(R.string.app_name)));

        preferenceManager = new PreferenceManager(this);

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

        if (Util.isNotEmpty(preferenceManager.getPreviouslyEnteredPhoneNo())) {
            binding.tietPhone.setText(preferenceManager.getPreviouslyEnteredPhoneNo());
            Util.showKeyboard(binding.tietPhone);
            if (binding.tietPhone.getText() != null) {
                binding.tietPhone.setSelection(binding.tietPhone.getText().length()); //because there is a hyphen
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showDialog(String phoneNoStr) {
        new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml(getString(R.string.sure_to_send_an_sms, phoneNoStr)))
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            preferenceManager.setPreviouslyEnteredPhoneNo(phoneNoStr);
                            Intent i = new Intent(FirstSignupActivity.this, VerifyOTPActivity.class);
                            i.putExtra(IntentExtraTag.PHONE_TO_VERIFY.getTag(), phoneNoStr);
                            startActivity(i);
                        })
                .setNegativeButton(R.string.edit, (dialog, which) -> dialog.dismiss())
                .show();
    }
}