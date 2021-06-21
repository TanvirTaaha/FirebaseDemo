package com.tangent.firebasedemo.ui.create_chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tangent.firebasedemo.model.PhoneContactModel;
import com.tangent.firebasedemo.repository.PhoneContactsRepo;

import java.util.List;

public class CreateChatViewModel extends ViewModel {
    private final PhoneContactsRepo phoneContactsRepo;

    public CreateChatViewModel() {
        phoneContactsRepo = PhoneContactsRepo.getInstance();
    }

    public void refreshContacts() {
        phoneContactsRepo.refreshContent();
    }

    public LiveData<Integer> getContactCount() {
        return phoneContactsRepo.getContactCount();
    }

    public LiveData<List<PhoneContactModel>> getContactList() {
        return phoneContactsRepo.getContactList();
    }
}
