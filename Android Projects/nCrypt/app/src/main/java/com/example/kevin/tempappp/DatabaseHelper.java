package com.example.kevin.tempappp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    static final String DATABASE_NAME = "MyDB";

    // Table Names
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_DELETED_THREADS = "deletedThreads";
    private static final String TABLE_DELETED_MESSAGES = "deletedMessages";


    // Common column names
    static final String KEY_THREAD_ID = "thread_id";
    static final String KEY_ROWID = "_id";

    // CONTACTS Table - column names
    static final String KEY_PHONENO = "phoneno";
    static final String KEY_NAME = "name";
    static final String KEY_PUBLIC = "public_key";
    static final String KEY_PRIORITY = "priority";

    // DELTED MESSAGES Table - column names
    static final String KEY_MESSAGE_ID = "message_id";


    static final String TAG = "DBAdapter";

    // Table Create Statements
    // CONTACTS table create statement
    static final String CREATE_TABLE_CONTACTS = "create table "
            + TABLE_CONTACTS + "(" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_PHONENO + " text, "
            + KEY_NAME + " text, "
            + KEY_PUBLIC + " blob, "
            + KEY_PRIORITY + " integer);";

    // DELETED THREADS table create statement
    static final String CREATE_TABLE_DELETED_THREADS = "create table "
            + TABLE_DELETED_THREADS + "(" + KEY_THREAD_ID + " integer primary key);";

    // DELETED MESSAGES table create statement
    static final String CREATE_TABLE_DELETED_MESSAGES = "create table "
            + TABLE_DELETED_MESSAGES + "(" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_MESSAGE_ID + " integer, "
            + KEY_THREAD_ID + " integer);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //---closes the database---
    public void close()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_CONTACTS);
            db.execSQL(CREATE_TABLE_DELETED_THREADS);
            db.execSQL(CREATE_TABLE_DELETED_MESSAGES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELETED_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELETED_THREADS);
        onCreate(db);
    }

    //---insert a contact into the database---
    public long insertContact(String phone, String name, Key public_key) {
        SQLiteDatabase db = this.getWritableDatabase();

        byte[] serialized_public_key;
        try {
            serialized_public_key = Serialize(public_key);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PHONENO, phone);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PUBLIC, serialized_public_key);
        initialValues.put(KEY_PRIORITY, 0); // This is used upon creation, make all contacts 0
        return db.insert(TABLE_CONTACTS, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteContact(String phoneno) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTS, KEY_PHONENO + "=" + phoneno, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_CONTACTS, new String[]{KEY_ROWID, KEY_PHONENO,
                KEY_NAME, KEY_PUBLIC, KEY_PRIORITY}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getContactByPhoneNumber(String phoneno) throws SQLException {

        if(phoneno == null)
            return null;

        if(phoneno.contains("+"))
            phoneno = phoneno.substring(1);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor =
                db.query(true, TABLE_CONTACTS, new String[]{KEY_ROWID,
                                KEY_PHONENO, KEY_NAME, KEY_PUBLIC, KEY_PRIORITY}, KEY_PHONENO + "=" + phoneno, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateContact(String phoneno, String name, Key public_key, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Must have phone number as it is the primary key
        ContentValues args = new ContentValues();
        args.put(KEY_PHONENO, phoneno);

        // Public Key Null Validation
        if (public_key != null) {
            byte[] serialized_public_key;
            try {
                serialized_public_key = Serialize(public_key);
                args.put(KEY_PUBLIC, serialized_public_key);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        if (name != null)
            args.put(KEY_NAME, name);


        args.put(KEY_PRIORITY, priority);

        return db.update(TABLE_CONTACTS, args, KEY_PHONENO + "=" + phoneno, null) > 0;
    }

    //---insert a message into the database---
    public long insertDeletedMessage(Integer thread_id, Integer message_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_THREAD_ID, thread_id);
        initialValues.put(KEY_MESSAGE_ID, message_id);
        return db.insert(TABLE_DELETED_MESSAGES, null, initialValues);
    }

    //---insert a thread into the database---
    public long insertDeletedThread(Integer thread_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_THREAD_ID, thread_id);
        return db.insert(TABLE_DELETED_THREADS, null, initialValues);
    }

    //---retrieves all the threads---
    public Cursor getAllDeletedThreads()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_DELETED_THREADS, new String[]{KEY_THREAD_ID}, null, null, null, null, null);
    }

    //---retrieves all messages in a specified thread---
    public Cursor getDeletedMessagesByThreadId(Integer threadId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(true, TABLE_DELETED_MESSAGES, new String[]{
                KEY_MESSAGE_ID}, KEY_THREAD_ID + "=" + threadId, null, null, null, null, null);
    }

    //---deletes all thread id's from DeletedThreads table---
    public long resetDeletedThreads()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DELETED_THREADS, null, null);
    }

    //---deletes all thread id's from DeletedMessages table---
    public long resetDeletedMessages()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DELETED_MESSAGES, null, null);
    }

    public static byte[] Serialize(Object obj) throws IOException {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(obj);
            return b.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object Deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream b = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(b);
            return o.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
