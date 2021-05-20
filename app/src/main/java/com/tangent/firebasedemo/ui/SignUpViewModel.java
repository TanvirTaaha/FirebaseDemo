package com.tangent.firebasedemo.ui;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tangent.firebasedemo.utils.PreferenceManager;

public class SignUpViewModel extends ViewModel {
    private String mUserName;
    private String mEmail;
    private String mPassword;
    private PreferenceManager preferenceManager;
    private MutableLiveData<String> mPhone;
    private MutableLiveData<Boolean> verificationSent;
    private MutableLiveData<String> codeFromSMS;

    public LiveData<Boolean> sendVerificationCodeToPhone(String phone) {
        if (mPhone == null) {
            mPhone = new MutableLiveData<>();
        }
        mPhone.setValue(phone);
        return isVerificationSent();
    }

    public LiveData<String> getPhoneNo() {
        return mPhone;
    }

    public LiveData<Boolean> isVerificationSent() {
        if (verificationSent == null) {
            verificationSent = new MutableLiveData<>();
            verificationSent.setValue(false);
        }
        new Handler().postDelayed(() -> {
            verificationSent.setValue(true);
            codeFromSMS = new MutableLiveData<>();
            codeFromSMS.setValue("123456");
        }, 3000);
        return verificationSent;
    }

    public LiveData<String> getCodeFromSMS() {
        if (codeFromSMS == null) {
            codeFromSMS = new MutableLiveData<>();
            codeFromSMS.setValue("");
        }
        return codeFromSMS;
    }
}
