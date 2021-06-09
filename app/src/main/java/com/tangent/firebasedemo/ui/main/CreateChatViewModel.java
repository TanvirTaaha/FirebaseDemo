package com.tangent.firebasedemo.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tangent.firebasedemo.app.App;
import com.tangent.firebasedemo.model.PhoneContactModel;
import com.tangent.firebasedemo.repo.PhoneContactsRepo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateChatViewModel extends AndroidViewModel {
    private final PhoneContactsRepo phoneContactsRepo;

    public CreateChatViewModel(@NonNull @NotNull Application application) {
        super(application);
        if (application instanceof App) {
            phoneContactsRepo = new PhoneContactsRepo(application, ((App) application).getExecutorService());
        } else {
            phoneContactsRepo = new PhoneContactsRepo(application, App.getInstance().getExecutorService());
        }
    }

    public void loadContacts() {
        phoneContactsRepo.loadContacts(false);
    }

    public void refreshContacts() {
        phoneContactsRepo.loadContacts(true);
    }

    public LiveData<Integer> getContactCount() {
        return phoneContactsRepo.getContactCount();
    }

    public LiveData<ArrayList<PhoneContactModel>> getContactList() {
        return phoneContactsRepo.getContactList();
    }
}
