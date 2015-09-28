package com.example.kevin.tempappp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private String phoneNumber;
    private EditText edtMessage;
    public static ArrayList<TextMessage> chatMessageList;

    TextView temptxtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatMessageList = new ArrayList();
        phoneNumber = "5194945387";
        edtMessage=(EditText)findViewById(R.id.chatLine);
        temptxtview = new TextView(this);




    }


    public void sendMsg(String msg, String phone) {


        String sent = "SMS_SENT";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(sent), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if(getResultCode() == Activity.RESULT_OK)
                {
                    //This is where we add a sent message!!
                    //temptxtview = new TextView();
                    for(TextMessage T: chatMessageList) {
                        //if incoming left
                        if(T.incoming)
                        {
                            //add text message to list of text messages on screen with left offset



                        }
                        //else right
                        else
                        {
                            //add text message to list of text messages on screen with right offset


                        }


                        //temptxtview.setText(T.text);
                        //temptxtview.setTextSize(40);
                        //setContentView(temptxtview);
                    }


                    Toast.makeText(getBaseContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "SMS could not send",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(sent));

        SmsManager sms = SmsManager.getDefault();
        chatMessageList.add(new TextMessage(false, msg));
        sms.sendTextMessage(phone, null, msg, sentPI, null);
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
