package com.tangent.firebasedemo.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;

import com.tangent.firebasedemo.app.App;
import com.tangent.firebasedemo.data.ContactsDao;
import com.tangent.firebasedemo.data.ContactsRoomDatabase;
import com.tangent.firebasedemo.model.PhoneContactModel;
import com.tangent.firebasedemo.utils.Util;

import java.util.ArrayList;
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
        Timber.i("Reading contacts from OS");
        executor.execute(() -> {
            ContentResolver cr = App.getAppContext().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                if (contactsDao.getCountStatic() > 0) {
                    Timber.v("PhoneContactModel Table nuked");
                    contactsDao.nukeTable();
                }
                ArrayList<PhoneContactModel> contactModelList = new ArrayList<>();
                if (cur.moveToFirst()) {
                    do {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        PhoneContactModel model = new PhoneContactModel();
                        model.setName(Util.isNotEmpty(name) ? name : "");
                        if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            ArrayList<String> phoneNoList = new ArrayList<>();
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null
                                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id},
                                    null);
                            if (pCur.moveToFirst()) {
                                do {
                                    phoneNoList.add(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                                } while (pCur.moveToNext());
                            }
                            pCur.close();
                            model.setPhoneNoList(phoneNoList);
                        }
                        contactModelList.add(model);
                    } while (cur.moveToNext());
                }
                contactsDao.insertAll(contactModelList.toArray(new PhoneContactModel[0]));
            }

            if (cur != null) {
                cur.close();
            }
        });
    }

    public LiveData<Integer> getContactCount() {
        return contactsDao.getCount();
    }

    public LiveData<List<PhoneContactModel>> getContactList() {
        return contactsDao.getAll();
    }
}
