package com.tangent.firebasedemo.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.repository.FirebaseDatabaseRepo;

import java.io.Serializable;

public class HomeActivityViewModel extends ViewModel implements Serializable {
    private FirebaseDatabaseRepo db = FirebaseDatabaseRepo.getInstance();

    private LiveData<UserModel> mUserModelLD;

    public void setUid(String uid) {
        mUserModelLD = db.getUserFromInternet(uid);
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return mUserModelLD;
    }
}