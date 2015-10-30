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

public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_PHONENO = "phoneno";
    static final String KEY_NAME = "name";
    static final String KEY_PUBLIC = "public_key";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "phoneno text, name text, public_key blob);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);

    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertContact(String phone, String name, Key public_key)
    {
        byte[] serialized_public_key;
        try {
            serialized_public_key = Serialize(public_key);
        }catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PHONENO, phone);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PUBLIC, serialized_public_key);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteContact(String phoneno)
    {
        return db.delete(DATABASE_TABLE, KEY_PHONENO + "=" + phoneno, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_PHONENO,
                KEY_NAME, KEY_PUBLIC}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getContactByPhoneNumber(String phoneno) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_PHONENO, KEY_NAME, KEY_PUBLIC}, KEY_PHONENO + "=" + phoneno, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateContact(String phoneno, String name, Key public_key)
    {
        // Must have phone number as it is the primary key
        ContentValues args = new ContentValues();
        args.put(KEY_PHONENO, phoneno);

        // Public Key Null Validation
        if(public_key != null) {
            byte[] serialized_public_key;
            try {
                serialized_public_key = Serialize(public_key);
                args.put(KEY_PUBLIC, serialized_public_key);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        if(name != null)
            args.put(KEY_NAME, name);

        return db.update(DATABASE_TABLE, args, KEY_PHONENO + "=" + phoneno, null) > 0;
    }


    public static byte[] Serialize(Object obj) throws IOException {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(obj);
            return b.toByteArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Object Deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream b = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(b);
            return o.readObject();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

}
