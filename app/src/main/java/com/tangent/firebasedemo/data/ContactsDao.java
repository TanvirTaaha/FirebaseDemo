package com.tangent.firebasedemo.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tangent.firebasedemo.model.PhoneContactModel;

import java.util.List;

@Dao
public interface ContactsDao {
    @NonNull
    @Query("SELECT * FROM phonecontactmodel ORDER BY LOWER(display_name) ASC")
    LiveData<List<PhoneContactModel>> getAll();

    @NonNull
    @Query("SELECT COUNT(display_name) FROM phonecontactmodel")
    LiveData<Integer> getCount();

    @NonNull
    @Query("SELECT COUNT(display_name) FROM phonecontactmodel")
    int getCountStatic();

    @Insert
    void insertAll(@NonNull PhoneContactModel... models);

    @Delete
    void delete(PhoneContactModel model);

    @Query("DELETE FROM phonecontactmodel")
    void nukeTable();
}
