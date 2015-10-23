package com.example.kevin.tempappp;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;


public class MainActivity extends Activity {

    // Our Phone Number!
    private String phoneNumber;

    private EditText edtMessage;
    /*public static ArrayList<TextMessage> chatMessageList;
    public static ArrayList<TextMessage> numbersOnly;*/
    ListView lv;
    TextView temptxtview;
    MenuAdapter adapter;

    ArrayList<Conversation> smsConversationList = new ArrayList<Conversation>();

    // Database Object
    public DBAdapter db;
    // Encryption Object
    private Encryption encryption;

    // Public and Private Keys
    Key my_public;
    Key my_private;

    // Globals
    public static nCryptApplication globals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globals = ((nCryptApplication)(this.getApplication()));
        encryption = globals.getEncryption();

        setupDatabase();
        setupPhoneNumber();
        //setupKeys();

        temptxtview = new TextView(this);

        adapter = new MenuAdapter(this, smsConversationList);

        lv = (ListView) findViewById(R.id.msgListView);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //String phoneNo = numbersOnly.get(position).getNumber();
                String phoneNo = smsConversationList.get(position).getPhoneNumber();
                int threadid = smsConversationList.get(position).getThreadId();
                //show what number was selected
                Toast.makeText(getBaseContext(), phoneNo, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("MyPhoneno", phoneNumber);
                intent.putExtra("threadid", threadid);
                //intent.putExtra("UserPrivateKey" , encryption.privateKey);
                //DataWrapper dw = new DataWrapper(chatMessageList);
                //intent.putExtra("data", dw);
                startActivity(intent);


            }
        });

        //conversationArrayAdapter = new ArrayAdapter<Conversation>(this, android.R.layout.simple_list_item_1, smsConversationList);
        LoadConversations();
    }

    public void LoadConversations()
    {
        Integer continueCount = 0;

        ContentResolver contentResolver = getContentResolver();
        Cursor smsConversationCursor = contentResolver.query(Telephony.Sms.Conversations.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Conversations.DEFAULT_SORT_ORDER);
        int indexThreadId = smsConversationCursor.getColumnIndex(Telephony.Sms.Conversations.THREAD_ID);
        int indexSnippet = smsConversationCursor.getColumnIndex(Telephony.Sms.Conversations.SNIPPET);
        int indexMessageCount = smsConversationCursor.getColumnIndex(Telephony.Sms.Conversations.MESSAGE_COUNT);

        if ((indexThreadId*indexSnippet*indexMessageCount) < 0 || !smsConversationCursor.moveToFirst()) return;

        smsConversationList.clear();
        do {
            Integer thread_id = -1;
            thread_id = smsConversationCursor.getInt(indexThreadId);
            if (thread_id < 0) return;
            String where = Telephony.Sms.Conversations.THREAD_ID + "=" + thread_id.toString();
            Cursor smsInboxCursor = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                    new String[]{Telephony.Sms.Inbox.ADDRESS,
                            Telephony.Sms.Inbox.PERSON,
                            Telephony.Sms.Inbox.DATE,},
                    where,
                    null,
                    Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);

            int indexAddress = smsInboxCursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS);
            int indexPerson = smsInboxCursor.getColumnIndex(Telephony.Sms.Inbox.PERSON);
            int indexDate = smsInboxCursor.getColumnIndex(Telephony.Sms.Inbox.DATE);

            if ((indexAddress*indexPerson*indexDate) < 0 || !smsInboxCursor.moveToFirst())
            {
                continueCount++;
                continue;
            };

            smsConversationList.add(new Conversation("",
                    smsInboxCursor.getString(indexAddress),
                    thread_id,
                    smsConversationCursor.getString(indexSnippet),
                    Resources.FormattedDate(smsInboxCursor.getInt(indexDate)),
                            smsConversationCursor.getInt(indexMessageCount)));
        } while (smsConversationCursor.moveToNext());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void DisplayContact(Cursor c)
    {
        encryption.privateKey = (Key)DBAdapter.Deserialize(c.getBlob(3));
        encryption.publicKey = (Key)DBAdapter.Deserialize(c.getBlob(4));

        String message = "Certified";
        encryption.Encrypt(message);

        message = encryption.Decrypt(encryption.EncodedMessage());

        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Phone Number: " + c.getString(1) + "\n" +
                        "Name:   " + c.getString(2) + "\n" +
                        "RSA Key Status: " + message,
                Toast.LENGTH_LONG).show();

    }


    private void setupPhoneNumber(){

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if(!tm.getLine1Number().isEmpty())
            phoneNumber = (String)tm.getLine1Number();
        else
            phoneNumber = "5194945387";

    }

    private void setupDatabase()
    {
        db = new DBAdapter(this);

        try {
            //String destPath = "/data/data/" + getPackageName() +
            //        "/databases";
            String destPath = "/storage/emulated/0/";

            File f = new File(destPath);
            if (!f.exists()) {
                f.mkdirs();
                f.createNewFile();

                //---copy the db from the assets folder into
                // the databases folder---

                CopyDB(getBaseContext().getAssets().open("contacts"),
                        new FileOutputStream(destPath + "/MyDB"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        db.open();
        long id = db.insertContact("5555555555", "Stan March", encryption.privateKey, encryption.publicKey);
        id = db.insertContact("0123456789", "Kevin Douag", encryption.privateKey, encryption.publicKey);

        Cursor c;
        c = db.getAllContacts();
        if(c == null)
            return;

        if (c.moveToFirst())
        {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.deleteContact(1);
        db.deleteContact(2);
        db.close();
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


    private void setupKeys() {
        Cursor c;
        // Setup and generate new encryption keys
        encryption = new Encryption(this.getBaseContext());
        encryption.GenerateKey();

        db.open();
        c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                if(c.getString(1) == this.phoneNumber) {
                    this.my_private = this.encryption.privateKey;
                    //this.my_private = c.getString(3);
                    //this.my_public = c.getString(2);
                }
            } while (c.moveToNext());
        }
        db.close();

    }
}
