package com.tangent.firebasedemo.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tangent.firebasedemo.model.Conversation;
import com.tangent.firebasedemo.model.InboxItem;
import com.tangent.firebasedemo.model.User;
import com.tangent.firebasedemo.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import timber.log.Timber;

public class MessagesDatabase {
    private final DatabaseReference messagesBranch;
    private final DatabaseReference usersBranch;

    public MessagesDatabase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        messagesBranch = db.getReference().child(Util.BRANCH_MESSAGES);
        usersBranch = db.getReference().child(Util.BRANCH_USERS);
        setListenerUsers();
        setListenerMessages();
//        db.setPersistenceEnabled(true);
    }

    private void setListenerMessages() {
        messagesBranch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                String pushId = snapshot.getKey();
                snapshot.getRef().keepSynced(true);
                snapshot.getRef().child("id").setValue(pushId);
                Timber.i("Message:added called %s", snapshot.toString());
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Timber.i("Message:changed called %s", snapshot.toString());
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                Timber.i("Message:removed called %s", snapshot.toString());
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Timber.i("Message:moved called %s", snapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Timber.i("Message:added called error:%s", error.getMessage());
            }
        });
    }

    private void setListenerUsers() {
        usersBranch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String pushId = snapshot.getKey();
                snapshot.getRef().keepSynced(true);
                snapshot.getRef().child("id").setValue(pushId);
                Timber.i("User:added called %s", snapshot.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Timber.i("User:changed called %s", snapshot.toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Timber.i("User:removed called ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Timber.i("User:moved called %s", snapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Timber.i("User:cancelled called error:%s", error.getMessage());
            }
        });
    }

    public void createUser(User user) {
        usersBranch.push().setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            }
        });
    }

    public User getUserFromInternet(String id) {
        final User[] user = new User[1];
        DatabaseReference reference = usersBranch.child(id);
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                user[0] = task.getResult().getValue(User.class);
                reference.keepSynced(true);
            }
        });
        return user[0];
    }

    public ArrayList<Conversation> getAllConversationForUID(String uid) {
        User user = getUserFromInternet(uid);
        final ArrayList<Conversation> conversations = new ArrayList<>();
        for (InboxItem item : user.getInbox()) {
            final Conversation[] conv = new Conversation[1];
            DatabaseReference convRef = messagesBranch.child(item.getConversationId());
            convRef.get().addOnCompleteListener(task -> {
                if (task.isComplete() && task.getResult() != null) {
                    conv[0] = task.getResult().getValue(Conversation.class);
                    convRef.keepSynced(true);
                }
            });
            conversations.add(conv[0]);
        }
        return conversations;
    }

    public void createConversation(Conversation conversation) {
        messagesBranch.push().setValue(conversation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Timber.v("Conversation created, %s", conversation.toString());
            }
        });
    }
}
