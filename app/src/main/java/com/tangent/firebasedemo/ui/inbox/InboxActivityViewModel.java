package com.tangent.firebasedemo.ui.inbox;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tangent.firebasedemo.model.firebasemodel.ChatBubble;

import java.util.ArrayList;

public class InboxActivityViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ChatBubble>> mChatBubblesLD;

    public InboxActivityViewModel() {

    }

    @NonNull
    public LiveData<ArrayList<ChatBubble>> getChatBubbleList() {
        if (mChatBubblesLD == null) {
            mChatBubblesLD = new MutableLiveData<>();
        }
        return mChatBubblesLD;
    }
}
