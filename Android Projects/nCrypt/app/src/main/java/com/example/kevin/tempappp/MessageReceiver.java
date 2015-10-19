package com.example.kevin.tempappp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {

    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create service Intent
        Intent serviceIntent = new Intent(context, nCryptService.class);
        // Start service
        context.startService(serviceIntent);

        //Start App On Boot Start Up
        Intent App = new Intent(context, MainActivity.class);
        App.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(App);

        Bundle bundle = intent.getExtras();
        SmsMessage[] recievedMsgs = null;
        String str = "";
        if (bundle != null)
        {

            Object[] pdus = (Object[]) bundle.get("pdus");
            recievedMsgs = new SmsMessage[pdus.length];
            for (int i=0; i < pdus.length; ++i)
            {
                recievedMsgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += recievedMsgs[i].getMessageBody().toString();
                //pulled ""SMS from " + recievedMsgs[i].getOriginatingAddress()+ " :" + " out of the str but will need for the phone number!!
            }
            MainActivity.chatMessageList.add(new TextMessage(true, str));

            MainActivity.showNotification(context);

            //Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        }
    }
}
