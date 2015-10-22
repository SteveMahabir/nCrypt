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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {

    // Main Encryption Object
    private static String IncomingPhoneNumber;
    private String phoneNumber;
    private static int threadid;
    private EditText edtMessage;
    public static ArrayList<TextMessage> chatMsgs;
    ListView lv;

    TextView temptxtview;

    MyAdapter adapter;

    // Globals
    nCryptApplication globals;

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

        IncomingPhoneNumber = String.valueOf(getIntent().getExtras().getString("phoneNo"));
        phoneNumber = String.valueOf(getIntent().getExtras().getString("MyPhoneno"));
        threadid = Integer.valueOf(getIntent().getExtras().getInt("threadid"));
        edtMessage=(EditText)findViewById(R.id.chatLine);


        chatMsgs = new ArrayList<TextMessage>();
        /*for(int i = 0 ; i < MainActivity.chatMessageList.size(); i++)
        {
            //is it incoming?
            if(MainActivity.chatMessageList.get(i).getIsIncoming())
            {
                //if incoming then check the number comin in and the chat incoming #
                if(MainActivity.chatMessageList.get(i).getNumber().equalsIgnoreCase(IncomingPhoneNumber))
                {
                    chatMsgs.add(MainActivity.chatMessageList.get(i));
                }
            }
            //outgoing
            else
            {
                //if outgoing check the tophone # to the chat incoming#
                if(MainActivity.chatMessageList.get(i).getToPhoneno().equalsIgnoreCase(IncomingPhoneNumber))
                {
                    chatMsgs.add(MainActivity.chatMessageList.get(i));
                }
            }
        }*/
        temptxtview = new TextView(this);

        LoadConversation(threadid);
        adapter = new MyAdapter(this, chatMsgs, IncomingPhoneNumber);

        lv = (ListView) findViewById(R.id.listView);
       // lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setAdapter(adapter);


    }


    public void sendMsg(final String msg) {


        /*String sent = "SMS_SENT";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(sent), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (getResultCode() == Activity.RESULT_OK) {
                    //  singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);

                    Toast.makeText(getBaseContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "SMS could not send",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(sent));

        SmsManager sms = SmsManager.getDefault();

//        sms.sendTextMessage(IncomingPhoneNumber, null, msg, sentPI, null);
        sms.sendTextMessage(IncomingPhoneNumber, null, "msg", sentPI, null);

        chatMsgs.add(new TextMessage(false, msg, phoneNumber, Resources.FormattedDate((new Date()).getTime()), threadid, -1));
*/
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

        BroadcastReceiver messageSentReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
                        chatMsgs.add(new TextMessage(false, msg, IncomingPhoneNumber, Resources.FormattedDate((new Date()).getTime()), threadid, -1));
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

        sm.sendMultipartTextMessage(IncomingPhoneNumber,null, parts, sentIntents, deliveryIntents);
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

    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.send:

                String message = edtMessage.getText().toString().trim();
                if (message != "")
                {

                    //SID 6 ID 2 Sending an SMS Text needs to be hooked up to the encryption method

                    message = globals.getEncryption().Encrypt(message);
                    Toast.makeText(getApplicationContext(), "ENCODED : " + message,Toast.LENGTH_SHORT).show();

                    sendMsg(message);
                    edtMessage.setText("");

                }

                /* final EditText message =  (EditText) findViewById(R.id.chatLine);


                Intent smsintent = new Intent(Intent.ACTION_VIEW);
                smsintent.putExtra("address", "5194945387");
                smsintent.putExtra("address", "5194945387");
                smsintent.putExtra("sms_body",message.getText().toString() );
                smsintent.setType("vnd.android-dir/mms-sms");
                startActivity(smsintent); */

                break;
        }
    }
}
