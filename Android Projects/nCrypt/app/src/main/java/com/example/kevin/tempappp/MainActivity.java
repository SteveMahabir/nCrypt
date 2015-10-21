package com.example.kevin.tempappp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup and generate new encryption keys
        encryption = new Encryption(this.getBaseContext());
        encryption.GenerateKey();

        chatMessageList = new ArrayList<TextMessage>();
        numbersOnly = new ArrayList<TextMessage>();
        phoneNumber = "5194945387";

        chatMessageList.add(new TextMessage(true, "In" ,"4865154865"));
        chatMessageList.add(new TextMessage(false,"Out", phoneNumber));
        chatMessageList.add(new TextMessage(true ,"In", "4865154865"));
        chatMessageList.add(new TextMessage(false, "Out",phoneNumber));
        chatMessageList.add(new TextMessage(true, "In", "1234567891"));
        chatMessageList.add(new TextMessage(false,"Out", phoneNumber));
        chatMessageList.add(new TextMessage(true, "In", "1234567891"));
        chatMessageList.add(new TextMessage(false,"Out", phoneNumber));

        temptxtview = new TextView(this);

        boolean swtch = false;
        for(int i = 0 ; i < chatMessageList.size();i++) {
            if(numbersOnly.size()==0)
            {
                if(chatMessageList.get(i).incoming == true) {
                    numbersOnly.add(chatMessageList.get(i));
                }
            }
            else {
                for (int idx = 0; idx < numbersOnly.size(); idx++) {
                    if (numbersOnly.get(idx).number.equalsIgnoreCase(chatMessageList.get(i).number))  {
                        swtch = true;
                    }
                }
                if (swtch == true) {
                    swtch = false;
                }
                else {
                    if(chatMessageList.get(i).incoming == true) {
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
                String phoneNo = numbersOnly.get(position).number;
                //show what number was selected
                Toast.makeText(getBaseContext(),phoneNo, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("phoneNo", phoneNo);
                //intent.putExtra("UserPrivateKey" , encryption.privateKey);
                //DataWrapper dw = new DataWrapper(chatMessageList);
                //intent.putExtra("data", dw);
                startActivity(intent);


            }
        });



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
