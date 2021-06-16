package com.tangent.firebasedemo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tangent.firebasedemo.model.PhoneContactModel;

@Database(entities = {PhoneContactModel.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class ContactsRoomDatabase extends RoomDatabase {
    public abstract ContactsDao contactsDao();
    private static volatile ContactsRoomDatabase contactsRoomDBInstance;

    public static ContactsRoomDatabase getInstance(final Context context) {
        if (contactsRoomDBInstance == null) {
            synchronized (ContactsRoomDatabase.class) {
                if (contactsRoomDBInstance == null) {
                    contactsRoomDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                            ContactsRoomDatabase.class,
                            "contacts_room_database")
                            .build();
                }
            }
        }
        return contactsRoomDBInstance;
    }
}
