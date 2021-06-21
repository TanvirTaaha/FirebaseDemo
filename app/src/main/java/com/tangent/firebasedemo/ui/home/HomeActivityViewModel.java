package com.tangent.firebasedemo.ui.home;

import androidx.lifecycle.ViewModel;

import com.tangent.firebasedemo.app.App;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.repository.FirebaseDatabaseRepo;
import com.tangent.firebasedemo.repository.PhoneContactsRepo;
import com.tangent.firebasedemo.utils.PreferenceManager;

import java.io.Serializable;

public class HomeActivityViewModel extends ViewModel implements Serializable {
    private final FirebaseDatabaseRepo db = FirebaseDatabaseRepo.getInstance();
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserModel getUserModel() {
        UserModel _userModel = db.getUserFromInternet(userId);
        new PreferenceManager(App.getAppContext()).setCurrentUserModel(_userModel);
        return _userModel;
    }

    public void refreshContacts() {
        PhoneContactsRepo.getInstance().refreshContent();
    }
}