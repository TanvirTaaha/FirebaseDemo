package com.tangent.firebasedemo.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tangent.firebasedemo.model.firebasemodel.Conversation;
import com.tangent.firebasedemo.model.firebasemodel.InboxItem;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;
import com.tangent.firebasedemo.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import timber.log.Timber;

public class FirebaseDatabaseRepo {
    private final DatabaseReference messagesBranch;
    private final DatabaseReference usersBranch;
    private DatabaseReference currentUserRef;
    private static FirebaseDatabaseRepo mInstance;
    private final CollectionReference usersCollectionRef;
    private final CollectionReference messagesCollectionRef;

    public static synchronized FirebaseDatabaseRepo getInstance() {
        if (mInstance == null) mInstance = new FirebaseDatabaseRepo();
        return mInstance;
    }

    private FirebaseDatabaseRepo() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        messagesBranch = db.getReference().child(Util.BRANCH_MESSAGES);
        usersBranch = db.getReference().child(Util.BRANCH_USERS);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        usersCollectionRef = firestore.collection("users_db");
        messagesCollectionRef = firestore.collection("messages_db");
//        setListenerUsers();
//        setListenerMessages();
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
                /*  The uid has been taken from FirebaseUser#getUid() and put in the UserModel
                    No need to worry.
                 */
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

//    public Task<Void> createUserRealtime(UserModel model) {
//        if (model.getInbox() == null) {
//            model.setInbox(new ArrayList<>());
//        }
//        return usersBranch.child(model.getId()).setValue(model);
//    }
//
//    public Task<DocumentReference> createUserFirestore(UserModel model) {
//        if (model.getInbox() == null) {
//            model.setInbox(new ArrayList<>());
//        }
//        return users_db.add(model);
//    }

    public void createNewUser(UserModel model, OnCompleteListener<Void> listener) {
        if (model.getInbox() == null) {
            model.setInbox(new ArrayList<>());
        }
        usersBranch.child(model.getId()).setValue(model).addOnCompleteListener(listener);
        usersCollectionRef.document(model.getId()).set(model).addOnCompleteListener(listener);
    }

    public UserModel getUserFromInternet(@NonNull String id) {
        AtomicReference<UserModel> model = new AtomicReference<>();
        usersCollectionRef.document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                model.set(documentSnapshot.toObject(UserModel.class));
            }
        }).addOnFailureListener(Throwable::printStackTrace);
        return model.get();
    }

    @Nullable
    public synchronized void fetchUserModelFromInternet(@NonNull String phoneNoWithCountryCode,
                                                        @NonNull OnCompleteListener<QuerySnapshot> listener) {
        usersCollectionRef.whereEqualTo("phoneNumber", phoneNoWithCountryCode)
                .get()
                .addOnCompleteListener(listener);
    }

    public ArrayList<Conversation> getAllConversationForUID(@NonNull String uid) {
        UserModel user = getUserFromInternet(uid);
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
