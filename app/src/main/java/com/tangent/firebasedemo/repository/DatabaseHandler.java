package com.tangent.firebasedemo.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tangent.firebasedemo.R;
import com.tangent.firebasedemo.app.App;
import com.tangent.firebasedemo.model.Contact;
import com.tangent.firebasedemo.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * create table _name(id, name, phone_number);
         */
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY, "
                + Util.KEY_NAME + " TEXT, "
                + Util.KEY_PHONE_NUMBER + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = App.getAppContext().getResources().getString(R.string.db_drop);
        db.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});
    }

    /*
     * CRUD = Create, Read, Update, Delete
     */
    public void addContact(Contact c) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, c.getName());
        values.put(Util.KEY_PHONE_NUMBER, c.getPhoneNumber());

        //Insert to row
        db.insert(Util.TABLE_NAME, null, values);
        db.close();
    }

    /*
     * Get a contact
     */
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID, Util.KEY_NAME, Util.KEY_PHONE_NUMBER},
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        Contact contact;
        if (cursor != null) {
            cursor.moveToFirst();
            contact = new Contact(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2));
            cursor.close();
        } else {
            contact = null;
        }
        db.close();
        return contact;
    }

    /*
     * Get all contacts
     */
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //Select all contacts
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);
        if (cursor.moveToFirst()) {
            do {
                contactList.add(new Contact(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    /*
     * Update contact
     */
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        //update the row
        //update(tableName, values, where id = 43)
        int rowUpdated = db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
        return rowUpdated;
    }

    /*
     * Delete single contact
     */
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    /*
     * Get contacts count
     */
    public int getCount() {
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
