package com.example.kevin.tempappp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lwong on 9/28/2015.
 */
public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_PHONENO = "phoneno";
    static final String KEY_NAME = "name";
    static final String KEY_PUBLIC_KEY = "public_key";
    static final String KEY_PRIVATE_KEY = "private_key";

    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "contacts";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "phoneno text not null, "
                    + "name text not null, "
                    + "public_key text, "
                    + "private_key text, "
                    + ");";

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
    public long insertContact(String phoneno, String name, String kpublic, String kprivate)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PHONENO, phoneno);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PUBLIC_KEY, kpublic);
        initialValues.put(KEY_PRIVATE_KEY, kprivate);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteContact(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE,
                new String[] {KEY_ROWID, KEY_NAME,KEY_PHONENO, KEY_PUBLIC_KEY, KEY_PRIVATE_KEY},
                null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_PHONENO, KEY_PUBLIC_KEY, KEY_PRIVATE_KEY},
                        KEY_ROWID + "=" + rowId,
                        null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateContact(long rowId, String phoneno, String name, String kpublic, String kprivate)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_PHONENO, phoneno);
        args.put(KEY_PUBLIC_KEY, kpublic);
        args.put(KEY_PRIVATE_KEY, kprivate);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void CopyDB(InputStream inputStream,
                       OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }



}
