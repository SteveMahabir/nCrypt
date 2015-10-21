package com.example.kevin.tempappp;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

    // Main Encryption Object
    Encryption encryption;
    private String IncomingPhoneNumber;
    private String phoneNumber;
    private EditText edtMessage;
    public static ArrayList<TextMessage> chatMessageList;
    ListView lv;

    TextView temptxtview;

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        //DataWrapper dw =  (DataWrapper) getIntent().getSerializableExtra("data");
        //chatMessageList = dw.getArray();
        IncomingPhoneNumber = String.valueOf(getIntent().getExtras().getString("phoneNo"));
        phoneNumber = "5194945387";
        edtMessage=(EditText)findViewById(R.id.chatLine);


        chatMessageList = new ArrayList<TextMessage>();
        chatMessageList.add(new TextMessage(true, "In" ,"4865154865"));
        chatMessageList.add(new TextMessage(false,"Out", phoneNumber));
        chatMessageList.add(new TextMessage(true ,"In", "4865154865"));
        chatMessageList.add(new TextMessage(false, "Out",phoneNumber));
        chatMessageList.add(new TextMessage(true, "In", "1234567891"));
        chatMessageList.add(new TextMessage(false,"Out", phoneNumber));
        chatMessageList.add(new TextMessage(true, "In", "1234567891"));
        chatMessageList.add(new TextMessage(false,"Out", phoneNumber));

        temptxtview = new TextView(this);

        adapter = new MyAdapter(this, chatMessageList);

        lv = (ListView) findViewById(R.id.listView);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setAdapter(adapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                //chatMessageList.get(position)


            }
        });
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
                    //  singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);

                    //This is where we add a sent message!!
                    lv = (ListView) findViewById(R.id.listView);
                    lv.setAdapter(adapter);
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
        chatMessageList.add(new TextMessage(false, msg, phoneNumber));
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

                    encryption.Encrypt(message);
                    Toast.makeText(getApplicationContext(), "ENCODED : " + encryption.EncodedMessage(),Toast.LENGTH_SHORT).show();

                    encryption.Decrypt();
                    Toast.makeText(getApplicationContext(), "DECODED : " + encryption.DecodedMessage(),Toast.LENGTH_SHORT).show();

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
