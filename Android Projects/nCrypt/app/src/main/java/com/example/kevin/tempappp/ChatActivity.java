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
    private String IncomingPhoneNumber;
    private String phoneNumber;
    private int threadid;
    private EditText edtMessage;
    public static ArrayList<TextMessage> chatMsgs;
    ListView lv;

    TextView temptxtview;

    MyAdapter adapter;

    // Globals
    nCryptApplication globals;

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


    public void sendMsg(String msg, String phone) {


        String sent = "SMS_SENT";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(sent), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (getResultCode() == Activity.RESULT_OK) {
                    //  singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);

                    //This is where we add a sent message!!
                    lv = (ListView) findViewById(R.id.listView);
                    lv.setAdapter(adapter);
                    Toast.makeText(getBaseContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "SMS could not send",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(sent));

        SmsManager sms = SmsManager.getDefault();
        //chatMsgs.add(new TextMessage(false, msg, IncomingPhoneNumber, "", 0, 0));
        //String nCryptmsg = encryption.Encrypt(msg);
        sms.sendTextMessage(IncomingPhoneNumber, null, msg, sentPI, null);
    }

    public String FormattedDate(long timeMillis)
    {
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        return format.format(date);
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
                    FormattedDate(smsInboxCursor.getLong(indexDate)),
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

                    globals.getEncryption().Decrypt();
                    //message = ((nCryptApplication)this.getApplication()).getEncryption().DecodedMessage();
                    //Toast.makeText(getApplicationContext(), "DECODED : " + message,Toast.LENGTH_SHORT).show();

                    sendMsg(message, phoneNumber);
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
