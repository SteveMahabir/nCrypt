package com.example.kevin.tempappp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnLongClickListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nickademus on 10/21/15.
 * This is a place to stuff a bunch of the extras, mostly Listeners and things that we could take out of other code areas
 */

final class Resources {

    private Resources(){
    };


    public static String FormattedDate(long timeMillis)
    {
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        return format.format(date);
    }
}


//Class added by Nick
// Touch listener class for the Press and hold picture event in chat area
class touchListener_Image implements View.OnTouchListener{

    // Data Members
    private TextView textview;
    private TextMessage textMessage;
    private nCryptApplication globals;
    private Context context;
    private Encryption encryption;

    public touchListener_Image( TextView tv, TextMessage tm, Context con)
    {
        textview = tv;
        textMessage = tm;
        globals = MainActivity.globals;
        context = con;


        this.encryption = new Encryption();
        encryption.PrepareKeys();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = false;

        if(!encryption.isEncrypted(textMessage.getText())) return true;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                // Start action ...
                String  message = encryption.Decrypt(textMessage.getText());
                Toast.makeText(context, "SMS DECODED", Toast.LENGTH_SHORT).show();
                textview.setText(message);
                textview.setVisibility(View.VISIBLE);
                retVal = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                v.setPressed(false);
                // Stop action ...
                textview.setText("");
                textview.setVisibility(View.INVISIBLE);
                retVal = true;
                break;
            case  MotionEvent.ACTION_UP:
                v.setPressed(false);
                // Stop action ...
                textview.setText("");
                textview.setVisibility(View.INVISIBLE);
                retVal = true;
                break;
        }
        return retVal;
    }
}

//Class added by Nick
// Touch listener class for the Press and hold picture event in chat area
class touchListener_Conversation implements AdapterView.OnItemClickListener{

    // Data Members
    private Context context;
    private String phoneNumber;
    private Encryption encryption;
    ArrayList<Conversation> smsConversationList;

    public touchListener_Conversation( ArrayList<Conversation> _smsConversationList, String phoneno, Context con)
    {
        smsConversationList = _smsConversationList;
        context = con;
        phoneNumber = phoneno;

        this.encryption = new Encryption();
        encryption.PrepareKeys();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                               long id) {
        //String phoneNo = numbersOnly.get(position).getNumber();
        String phoneNo = smsConversationList.get(position).getPhoneNumber();
        int threadid = smsConversationList.get(position).getThreadId();
        //show what number was selected
        Toast.makeText(context, phoneNo, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("MyPhoneno", phoneNumber);
        intent.putExtra("threadid", threadid);
        //intent.putExtra("UserPrivateKey" , encryption.privateKey);
        //DataWrapper dw = new DataWrapper(chatMessageList);
        //intent.putExtra("data", dw);
        context.startActivity(intent);

    }

}



class touchListener_Contact implements AdapterView.OnTouchListener {

    final Handler handler = new Handler();

    Runnable openContactManager = new Runnable() {
        public void run() {
            String phoneNo = "";
            int threadid = 0;

            Toast.makeText(context, "THIS IS A LONG TEXT LISTENER", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, ContactsActivity.class);
            intent.putExtra("phoneNo", phoneNo);
            intent.putExtra("MyPhoneno", phoneNumber);
            intent.putExtra("threadid", threadid);
            //intent.putExtra("UserPrivateKey" , encryption.privateKey);

            context.startActivity(intent);
        }
    };

    // Data Members
    private Context context;
    private String phoneNumber;
    private Encryption encryption;
    ArrayList<Conversation> smsConversationList;

    public touchListener_Contact( ArrayList<Conversation> _smsConversationList, String phoneno, Context con)
    {
        smsConversationList = _smsConversationList;
        context = con;
        phoneNumber = phoneno;

        this.encryption = new Encryption();
        encryption.PrepareKeys();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // Wait one second
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            handler.postDelayed(openContactManager, 1000);

        // Shit shit something happened! StOP EVERYTHING
        //if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP))
        if((event.getAction() == MotionEvent.ACTION_UP))
            handler.removeCallbacks(openContactManager);

        return false;
    }
}