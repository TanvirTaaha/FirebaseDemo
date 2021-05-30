package com.tangent.firebasedemo.ui.main.chats;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.tangent.firebasedemo.data.MessagesDatabase;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;

public class ChatsFragmentViewModel extends ViewModel {
    private MessagesDatabase db = MessagesDatabase.getInstance();
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }


    private LiveData<UserModel> mUserModelLD;

    public void setUid(String uid) {
        mUserModelLD = db.getUserFromInternet(uid);
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return mUserModelLD;
    }
}