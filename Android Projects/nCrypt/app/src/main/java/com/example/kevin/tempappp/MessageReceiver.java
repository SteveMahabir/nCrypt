package com.example.kevin.tempappp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MessageReceiver extends BroadcastReceiver {
    //MainActivity m;
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
        String nmbr = "";
        if (bundle != null)
        {

            Object[] pdus = (Object[]) bundle.get("pdus");
            recievedMsgs = new SmsMessage[pdus.length];
            int msgIndex = pdus.length - 1;
            TextMessage newMessage = null;

            int threadId = -1;

            if (msgIndex >= 0)
            {

                recievedMsgs[msgIndex] = SmsMessage.createFromPdu((byte[])pdus[msgIndex]);
                String phoneNumber = recievedMsgs[msgIndex].getOriginatingAddress();
                if(ChatActivity.GetConversationPhoneNumber().replaceAll("[()+-]", "").equalsIgnoreCase(phoneNumber.replaceAll("[()+-]", "")))
                {
                    threadId = ChatActivity.GetThreadId();
                }

                newMessage = new TextMessage(true,
                        recievedMsgs[msgIndex].getMessageBody().toString(),
                        phoneNumber,
                        Resources.FormattedDate(recievedMsgs[msgIndex].getTimestampMillis()),
                        threadId,
                        -1
                         );
                //pulled ""SMS from " + recievedMsgs[i].getOriginatingAddress()+ " :" + " out of the str but will need for the phone number!!
            }

            if (newMessage != null) {
                if(threadId >= 0) {
                    //MainActivity.chatMessageList.add(new TextMessage(true, str, nmbr,"", 0, 0));
                    ChatActivity.chatMsgs.add(newMessage);
                }
                showNotification(context, newMessage);

                //Toast.makeText(context, str, Toast.LENGTH_LONG).show();

            }
        }
    }

    public void showNotification(Context context, TextMessage newMessage){

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0

        Notification mNotification = new Notification.Builder(context)

                .setContentTitle(newMessage.getNumber())
                .setContentText(newMessage.getText().length() > 30 ? newMessage.getText().substring(0, 30) + "..." : newMessage.getText())
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
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
