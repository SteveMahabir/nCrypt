package com.example.kevin.tempappp;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {

    // Data Members
    private static String IncomingPhoneNumber;
    private String phoneNumber;
    private static int threadid;
    private EditText edtMessage;
    public static ArrayList<TextMessage> chatMsgs;
    static ListView lv;
    public TextView temptxtview;

    // Adapter Object
    static MyAdapter adapter;

    // Globals
    public nCryptApplication globals;

    // Main Encryption Object
    private Encryption encryption;
    private Encryption friends_encryption;

    // Friends Public Key
    private Key friends_key;

    public static String GetConversationPhoneNumber()
    {
        return IncomingPhoneNumber;
    }

    public static int GetThreadId()
    {
        return threadid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        globals = ((nCryptApplication)(this.getApplication()));

        // Grab the Keys
        encryption = new Encryption();
        encryption.PrepareKeys();

        // Look for Friends Public Key in the Database
        DBAdapter db = new DBAdapter(getBaseContext());
        db.open();
        Cursor c = db.getContactByPhoneNumber(IncomingPhoneNumber);
        db.close();

        // Instantiate the public key from the loaded database
        friends_encryption = new Encryption();
        if (c.moveToFirst())
        friends_encryption.setPublicKey((Key) DBAdapter.Deserialize(c.getBlob(3)));

        IncomingPhoneNumber = String.valueOf(getIntent().getExtras().getString("phoneNo"));
        phoneNumber = String.valueOf(getIntent().getExtras().getString("MyPhoneno"));
        threadid = Integer.valueOf(getIntent().getExtras().getInt("threadid"));
        final ImageButton button = (ImageButton)findViewById(R.id.send);
        edtMessage=(EditText)findViewById(R.id.chatLine);
        edtMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        chatMsgs = new ArrayList<TextMessage>();
        temptxtview = new TextView(this);

        LoadConversation(threadid);
        adapter = new MyAdapter(friends_encryption.getPublicKey(), this, chatMsgs, IncomingPhoneNumber);

        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setSelection(lv.getAdapter().getCount() - 1);

    }


    public void sendMsg(final String msg) {
        Context ctx = getBaseContext();

        final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
        final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        SmsManager sm = SmsManager.getDefault();

        IntentFilter sendIntentFilter = new IntentFilter(SMS_SEND_ACTION);
        IntentFilter receiveIntentFilter = new IntentFilter(SMS_DELIVERY_ACTION);

        PendingIntent sentPI = PendingIntent.getBroadcast(ctx, 0,new Intent(SMS_SEND_ACTION), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(ctx, 0,new Intent(SMS_DELIVERY_ACTION), 0);
        final boolean[] handled = new boolean[1];
        handled[0] = false;
        BroadcastReceiver messageSentReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        if (handled[0]) break;
                        Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
                        chatMsgs.add(new TextMessage(false, msg, IncomingPhoneNumber, Resources.FormattedDate((new Date()).getTime()), threadid, -1));
                        handled[0] = true;
                        update();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(messageSentReceiver, sendIntentFilter);

        BroadcastReceiver messageReceiveReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Delivered",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS Not Delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(messageReceiveReceiver, receiveIntentFilter);

        ArrayList<String> parts =sm.divideMessage(msg);

        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

        for (int i = 0; i < parts.size(); i++)
        {
            sentIntents.add(PendingIntent.getBroadcast(ctx, 0, new Intent(SMS_SEND_ACTION), 0));
            deliveryIntents.add(PendingIntent.getBroadcast(ctx, 0, new Intent(SMS_DELIVERY_ACTION), 0));
        }

        sm.sendMultipartTextMessage(IncomingPhoneNumber, null, parts, sentIntents, deliveryIntents);
    }

    public static void update()
    {
        adapter.notifyDataSetChanged();
        lv.setSelection(chatMsgs.size() - 1);
    }

    public void LoadConversation(int threadId)
    {
        LoadConversation(threadId, true);
    }

    public void LoadConversation(Integer threadId, boolean clear)
    {
        ContentResolver contentResolver = getContentResolver();
        String where = Telephony.Sms.Conversations.THREAD_ID + "=" + threadId.toString();
        Cursor smsInboxCursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, where, null, "date asc");
        int indexBody = smsInboxCursor.getColumnIndex(Telephony.Sms.BODY);
        int indexAddress = smsInboxCursor.getColumnIndex(Telephony.Sms.ADDRESS);
        int indexDate = smsInboxCursor.getColumnIndex(Telephony.Sms.DATE);
        int indexSentDate = smsInboxCursor.getColumnIndex(Telephony.Sms.DATE_SENT);
        int indexId = smsInboxCursor.getColumnIndex(Telephony.Sms._ID);
        int indexType = smsInboxCursor.getColumnIndex(Telephony.Sms.TYPE);


        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        if(clear)
            chatMsgs.clear();
        do {
            chatMsgs.add(new TextMessage(
                    smsInboxCursor.getInt(indexType) == 1,
                    smsInboxCursor.getString(indexBody),
                    smsInboxCursor.getString(indexAddress),
                    Resources.FormattedDate(smsInboxCursor.getLong(indexDate)),
                    threadId,
                    smsInboxCursor.getInt(indexId)
            ));
        } while (smsInboxCursor.moveToNext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Send the Public Key!
        if (id == R.id.send_settings) {
            Toast.makeText(getApplicationContext(), "Successfully Saved!",Toast.LENGTH_LONG).show();
            String public_key = encryption.sendPublicKey(encryption.getPublicKey());
            sendMsg(public_key);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Send SMS Button
    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.send:
                // Main Sending Logic!
                String raw_message = edtMessage.getText().toString().trim();
                if (raw_message != "")
                {
                    //SID 6 ID 2 Sending an SMS Text needs to be hooked up to the encryption method

                    String encrypted_message = encryption.Encrypt(raw_message);
                    Toast.makeText(getApplicationContext(), "SMS ENCODED",Toast.LENGTH_SHORT).show();

                    sendMsg(encrypted_message);
                    edtMessage.setText("");

                    /* Encryption Testing Area

                    boolean isencrypted;
                    isencrypted = globals.getEncryption().isEncrypted(message);

                    message = globals.getEncryption().Decrypt(message);

                    isencrypted = globals.getEncryption().isEncrypted(message);

                    message = ((nCryptApplication)this.getApplication()).getEncryption().DecodedMessage();
                    Toast.makeText(getApplicationContext(), "DECODED : " + message,Toast.LENGTH_SHORT).show();

                    */
                }
                break;
        }
    }
}
