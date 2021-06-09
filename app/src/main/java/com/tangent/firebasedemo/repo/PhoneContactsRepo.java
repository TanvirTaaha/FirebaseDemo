package com.tangent.firebasedemo.repo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tangent.firebasedemo.model.PhoneContactModel;
import com.tangent.firebasedemo.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import timber.log.Timber;

public class PhoneContactsRepo {
    private final Executor executor;
    private final MutableLiveData<Integer> contactCountLD;
    private final MutableLiveData<ArrayList<PhoneContactModel>> contactListLD;
    private final Context context;
    private final PreferenceManager preferenceManager;

    public PhoneContactsRepo(Context context, Executor executor) {
        this.context = context;
        this.executor = executor;
        contactListLD = new MutableLiveData<>();
        contactCountLD = new MutableLiveData<>();
        preferenceManager = new PreferenceManager(context);
    }

    public void loadContacts(boolean refresh) {
        if (preferenceManager.getContactArrayList() == null || refresh) {
            refreshContent();
        } else {
            Timber.i("Reading contacts from SharedPreferences");
            ArrayList<PhoneContactModel> contactList = preferenceManager.getContactArrayList();
            contactCountLD.setValue(contactList.size());
            contactListLD.setValue(contactList);
        }
    }

    private void refreshContent() {
        Timber.i("Reading contacts from OS");
        executor.execute(() -> {
            ContentResolver cr = getContext().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                contactCountLD.postValue(cur.getCount());
                ArrayList<PhoneContactModel> contactModelList = new ArrayList<>();
                if (cur.moveToFirst()) {
                    do {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        PhoneContactModel model = new PhoneContactModel();
                        model.setId(id);
                        model.setName(name);
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
                contactListLD.postValue(contactModelList);
            } else {
                contactCountLD.postValue(0);
                contactListLD.postValue(new ArrayList<>());
            }

            if (cur != null) {
                cur.close();
            }

            preferenceManager.setContactArrayList(contactListLD.getValue());
        });

    }

    public Context getContext() {
        return context;
    }

    public LiveData<Integer> getContactCount() {
        return contactCountLD;
    }

    public LiveData<ArrayList<PhoneContactModel>> getContactList() {
        return contactListLD;
    }
}
