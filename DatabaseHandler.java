package com.praveengupta.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Praveen Gupta on 11-11-2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactManager",
            TABLE_CONTACTS = "contacts",
            KEY_NAME = "name",
            KEY_MOB = "mob",
            KEY_MAIL = "mail",
            KEY_ADDRESS = "address",
            KEY_ID = "id",
            KEY_IMAGEURI = "imageUri";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT "
                + "," + KEY_NAME + " TEXT," + KEY_MOB + " TEXT," + KEY_MAIL + " TEXT ," + KEY_ADDRESS + " TEXT, " + KEY_IMAGEURI + " TEXT)";

        sqLiteDatabase.execSQL(sql);
    }

   @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(sqLiteDatabase);
    }

    public void createContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_MOB, contact.getMob());
        values.put(KEY_MAIL, contact.getMail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGEURI, contact.getImageURI().toString());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "Select * from " + TABLE_CONTACTS + " where " + KEY_ID + "=" + id;

        Cursor cursor = db.query(TABLE_CONTACTS,
                new String[]{KEY_ID, KEY_NAME, KEY_MOB, KEY_MAIL, KEY_ADDRESS, KEY_IMAGEURI},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        Contact contact=null;
        if (cursor != null)
        {cursor.moveToFirst();
        contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));}
        db.close();
        cursor.close();
        return contact;
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getContactsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_MOB, contact.getMob());
        values.put(KEY_MAIL, contact.getMail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGEURI, contact.getImageURI().toString());
        int rowsAffected = db.update(TABLE_CONTACTS, values, KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
        return rowsAffected;
    }

    public LinkedList<Contact> getAllContacts() {
        LinkedList<Contact> contacts = new LinkedList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        Contact contact;
        if (cursor.moveToFirst()) {
            do {
                contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));

                Log.i("database", cursor.getString(5));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return contacts;
    }

    public LinkedList<Contact> searchContacts(String input) {
        LinkedList<Contact> contacts = new LinkedList<Contact>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS + " where " + KEY_NAME + " like '%" + input + "%'", null);
        Contact contact;
        if (cursor.moveToFirst()) {
            do {
                contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return contacts;
    }

    public int getsearchContactsCount(String input) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS + " where " + KEY_NAME + " like '%" + input + "%'", null);
        int cursorCount = cursor.getCount();
        db.close();
        cursor.close();
        return cursorCount;
    }
}