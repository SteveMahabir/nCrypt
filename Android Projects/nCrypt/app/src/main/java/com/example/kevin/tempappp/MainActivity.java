package com.example.kevin.tempappp;

import android.app.Activity;
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


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;






public class MainActivity extends Activity {

    // Main Encryption Object
    Encryption encryption;

    private String phoneNumber;
    private EditText edtMessage;
    public static ArrayList<TextMessage> chatMessageList;
    public static ArrayList<TextMessage> numbersOnly;
    ListView lv;
    TextView temptxtview;
    MenuAdapter adapter;

    ArrayAdapter arrayAdapter;
    ArrayAdapter conversationArrayAdapter;
    ListView smsListView;
    ArrayList<TextMessage> smsMessagesList = new ArrayList<TextMessage>();
    ArrayList<Conversation> smsConversationList = new ArrayList<Conversation>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        * This is now done in the nCryptApplication class
        */
        // Setup and generate new encryption keys
        //encryption = new Encryption(this.getBaseContext());
        //encryption.GenerateKey();



        chatMessageList = new ArrayList<TextMessage>();
        numbersOnly = new ArrayList<TextMessage>();



        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if(!tm.getLine1Number().isEmpty())
        phoneNumber = (String)tm.getLine1Number();
        else
            phoneNumber = "5194945387";


        chatMessageList.add(new TextMessage(true, "5195207040 In 1", "5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(false, "5195207040 Out 1"+ phoneNumber.toString(), phoneNumber, "5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "5195207040 In 2", "5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(false, "5195207040 Out 2",phoneNumber,"5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "5195207040 In 3" ,"5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(false,"5195207040 Out 3", phoneNumber,"5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(true ,"5195207040 In 4", "5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(false, "5195207040 Out 4",phoneNumber,"5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "5195207040 In 5" ,"5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(false,"5195207040 Out 5", phoneNumber,"5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(true ,"5195207040 In 6", "5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(false, "5195207040 Out 6",phoneNumber,"5195207040", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "4567891234 In 1", "4567891234", "", 0, 0));
        chatMessageList.add(new TextMessage(false,"4567891234 Out 1", phoneNumber, "4567891234", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "4567891234 In 2", "4567891234", "", 0, 0));
        chatMessageList.add(new TextMessage(false,"4567891234 Out 2", phoneNumber, "4567891234", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "4567891234 In 3", "4567891234", "", 0, 0));
        chatMessageList.add(new TextMessage(false,"4567891234 Out 3", phoneNumber, "4567891234", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "1234567891 In 1", "1234567891", "", 0, 0));
        chatMessageList.add(new TextMessage(false,"1234567891 Out 1", phoneNumber, "1234567891", "", 0, 0));
        chatMessageList.add(new TextMessage(true, "1234567891 In 2", "1234567891", "", 0, 0));
        chatMessageList.add(new TextMessage(false,"1234567891 Out 2", phoneNumber, "1234567891", "", 0, 0));

        temptxtview = new TextView(this);

        boolean swtch = false;
        for(int i = 0 ; i < chatMessageList.size();i++) {
            if(numbersOnly.size()==0)
            {
                if(chatMessageList.get(i).getIsIncoming()) {
                    numbersOnly.add(chatMessageList.get(i));
                }
            }
            else {
                for (int idx = 0; idx < numbersOnly.size(); idx++) {
                    if (numbersOnly.get(idx).getNumber().equalsIgnoreCase(chatMessageList.get(i).getNumber()))  {
                        swtch = true;
                    }
                }
                if (swtch == true) {
                    swtch = false;
                }
                else {
                    if(chatMessageList.get(i).getIsIncoming()) {
                        numbersOnly.add(chatMessageList.get(i));
                    }
                }
            }
        }//end sorting if

        adapter = new MenuAdapter(this, numbersOnly);

        lv = (ListView) findViewById(R.id.msgListView);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String phoneNo = numbersOnly.get(position).getNumber();
                //show what number was selected
                Toast.makeText(getBaseContext(), phoneNo, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("MyPhoneno", phoneNumber);
                //intent.putExtra("UserPrivateKey" , encryption.privateKey);
                //DataWrapper dw = new DataWrapper(chatMessageList);
                //intent.putExtra("data", dw);
                startActivity(intent);


            }
        });

        smsListView = (ListView) findViewById(R.id.msgListView);

        arrayAdapter = new ArrayAdapter<TextMessage>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        conversationArrayAdapter = new ArrayAdapter<Conversation>(this, android.R.layout.simple_list_item_1, smsConversationList);
        LoadConversations();
        LoadConversation(5);
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

        conversationArrayAdapter.clear();
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

            conversationArrayAdapter.add(new Conversation("",
                    smsInboxCursor.getString(indexAddress),
                    thread_id,
                    smsConversationCursor.getString(indexSnippet),
                    smsInboxCursor.getString(indexDate),
                    smsConversationCursor.getInt(indexMessageCount)));
        } while (smsConversationCursor.moveToNext());

    }
    public void LoadConversation(int threadId)
    {
        LoadConversation(threadId, true);
    }

    public String FormattedDate(long timeMillis)
    {
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        return format.format(date);
    }

    public void LoadConversation(int threadId, boolean clear)
    {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER);
        int indexBody = smsInboxCursor.getColumnIndex(Telephony.Sms.BODY);
        int indexAddress = smsInboxCursor.getColumnIndex(Telephony.Sms.ADDRESS);
        int indexDate = smsInboxCursor.getColumnIndex(Telephony.Sms.DATE);
        int indexSentDate = smsInboxCursor.getColumnIndex(Telephony.Sms.DATE_SENT);
        int indexId = smsInboxCursor.getColumnIndex(Telephony.Sms._ID);
        int indexType = smsInboxCursor.getColumnIndex(Telephony.Sms.TYPE);


        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        if(clear)
            arrayAdapter.clear();
        do {
            arrayAdapter.add(new TextMessage(
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

    public static void showNotification(Context context){

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(context)

                .setContentTitle("New Post!")
                .setContentText("Here's a new message for you!")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent)
                .setSound(soundUri)

                .addAction(R.drawable.icon, "View", pIntent)
                .addAction(0, "Remind", pIntent)

                .build();


        int mId = 1;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        // mId allows you to update the notification later on.
                notificationManager.notify(mId, mNotification);
        //notificationManager.notify(0, mNotification);
    }

}
