package com.tangent.firebasedemo.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tangent.firebasedemo.app.App;
import com.tangent.firebasedemo.data.ContactsDao;
import com.tangent.firebasedemo.data.ContactsRoomDatabase;
import com.tangent.firebasedemo.model.PhoneContactModel;
import com.tangent.firebasedemo.model.firebasemodel.UserModel;

import java.util.List;
import java.util.concurrent.Executor;

import timber.log.Timber;

public class PhoneContactsRepo {
    private static volatile PhoneContactsRepo mInstance;
    private final Executor executor;
    private final ContactsDao contactsDao;

    public static PhoneContactsRepo getInstance() {
        if (mInstance == null)
            mInstance = new PhoneContactsRepo(App.getInstance().getExecutorService());
        return mInstance;
    }

    private PhoneContactsRepo(Executor executor) {
        this.executor = executor;
        ContactsRoomDatabase contactsDB = ContactsRoomDatabase.getInstance(App.getAppContext());
        contactsDao = contactsDB.contactsDao();
    }

    public synchronized void refreshContent() {
        Timber.v("Reading contacts from OS");
        executor.execute(() -> {
            ContentResolver cr = App.getAppContext().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                if (contactsDao.getCountStatic() > 0) {
                    Timber.v("PhoneContactModel Table NUKED");
                    contactsDao.nukeTable();
                }
                if (cur.moveToFirst()) {
                    do {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null
                                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id},
                                    null);

                            if (pCur.moveToFirst()) {
                                do {
                                    String _phoneNoStr = handleNationalPhoneNoForBD(pCur.getString(
                                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                                    Timber.d("PHONE:%s", _phoneNoStr); //AHA..gonna flood
                                    FirebaseDatabaseRepo.getInstance()
                                            .fetchUserModelFromInternet(_phoneNoStr, (Task<QuerySnapshot> task) -> {
                                                if (task.isSuccessful() && task.getResult() != null) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        UserModel userModel;
                                                        userModel = document.toObject(UserModel.class);
                                                        Timber.d("Found user%s", _phoneNoStr);
                                                        Timber.d("%s => %s", document.getId(), userModel);

                                                        PhoneContactModel model = new PhoneContactModel();
                                                        model.setUid(userModel.getId());
                                                        model.setName(TextUtils.isEmpty(name) ? _phoneNoStr : name);
                                                        model.setBio(userModel.getBio());
                                                        model.setPhoneNumber(userModel.getPhoneNumber());
                                                        model.setProfilePicture(userModel.getProfilePicture());

                                                        App.getInstance()
                                                                .getExecutorService()
                                                                .execute(() -> contactsDao.insertAll(model));
                                                    }
                                                } else {
                                                    Timber.e("Error getting user model for %s", _phoneNoStr);
                                                }
                                            });
                                } while (pCur.moveToNext());
                            }
                            pCur.close();
                        }
                    } while (cur.moveToNext());
                }
            }
            if (cur != null) {
                cur.close();
            }
        });
    }

    public synchronized String handleNationalPhoneNoForBD(String phoneNoStr) {
        phoneNoStr = phoneNoStr.replace(" ", "");
        phoneNoStr = phoneNoStr.replace("-", "");
        if (phoneNoStr.startsWith("+")) return phoneNoStr;
        if (phoneNoStr.startsWith("0")) return "+88" + phoneNoStr;
        if (phoneNoStr.startsWith("1")) return "+880" + phoneNoStr;
        else return phoneNoStr;
    }

    public LiveData<Integer> getContactCount() {
        Timber.v("Reading contacts from RoomDB");
        return contactsDao.getCount();
    }

    public LiveData<List<PhoneContactModel>> getContactList() {
        Timber.v("Reading contacts from RoomDB");
        return contactsDao.getAll();
    }
}
