package com.example.kevin.tempappp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnLongClickListener;

import org.w3c.dom.Text;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nickademus on 10/21/15.
 * This is a place to stuff a bunch of the extras, mostly Listeners and things that we could take out of other code areas
 */

enum SkullColour{
    Pueple,
    DarkRed,
    Pink,
    Red,
    Orange,
    Yellow,
    Green,
    Cyan,
    Aqua,
    Blue
}

enum SkullType{
    Eyes,
    Glow,
    Flat
}

final class Resources {

    private Resources(){
    };


    public static String FormattedDate(long timeMillis)
    {
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        return format.format(date);
    }

    public static Bitmap GetSkullFromEnum( SkullColour skullour, SkullType skullType, Context con  ){
        int image = (R.drawable.purplering);
        Bitmap bmp = BitmapFactory.decodeResource( con.getResources(), image  );

        switch ( skullour ) {
            case Pueple:
                default:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.purpleeyes);
                        break;
                    default:
                    case Glow:
                        image = (R.drawable.purplering);
                        break;
                    case Flat:
                        image = (R.drawable.purpleskull);
                        break;
                }
                break;
            case DarkRed:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.darkredeyes);
                        break;
                    case Glow:
                        image = (R.drawable.darkredring);
                        break;
                    case Flat:
                        image = (R.drawable.darkredskull);
                        break;
                }
                break;
            case Pink:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.pinkeyes);
                        break;
                    case Glow:
                        image = (R.drawable.pinkring);
                        break;
                    case Flat:
                        image = (R.drawable.pinkskull);
                        break;
                }
                break;
            case Red:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.redeyes);
                        break;
                    case Glow:
                        image = (R.drawable.redring);
                        break;
                    case Flat:
                        image = (R.drawable.redskull);
                        break;
                }
                break;
            case Orange:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.orangeeyes);
                        break;
                    case Glow:
                        image = (R.drawable.orangering);
                        break;
                    case Flat:
                        image = (R.drawable.orangeskull);
                        break;
                }
                break;
            case Yellow:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.yelloweyes);
                        break;
                    case Glow:
                        image = (R.drawable.yellowring);
                        break;
                    case Flat:
                        image = (R.drawable.yellowskull);
                        break;
                }
                break;
            case Green:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.greeneyes);
                        break;
                    case Glow:
                        image = (R.drawable.greenring);
                        break;
                    case Flat:
                        image = (R.drawable.greenskull);
                        break;
                }
                break;
            case Cyan:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.cyaneyes);
                        break;
                    case Glow:
                        image = (R.drawable.cyanring);
                        break;
                    case Flat:
                        image = (R.drawable.cyanskull);
                        break;
                }
                break;
            case Aqua:
                switch(skullType){
                    case Eyes:
                        image = (R.drawable.aquaeyes);
                        break;
                    case Glow:
                        image = (R.drawable.aquaring);
                        break;
                    case Flat:
                        image = (R.drawable.aquaskull);
                        break;
                }
                break;
            case Blue:
                switch(skullType) {
                    case Eyes:
                        image = (R.drawable.blueeyes);
                        break;
                    case Glow:
                        image = (R.drawable.bluering);
                        break;
                    case Flat:
                        image = (R.drawable.blueskull);
                        break;
                }
                break;
        }//end switch
        return bmp = BitmapFactory.decodeResource( con.getResources(), image  );
    }// end GetSkullFromEnum
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
    private Key public_key;

    public touchListener_Image(Key decrypting_public_key, TextView tv, TextMessage tm, Context con)
    {
        textview = tv;
        textMessage = tm;
        globals = MainActivity.globals;
        context = con;

        // This public key could be your friends or your own
        public_key = decrypting_public_key;

        this.encryption = new Encryption();
        encryption.PrepareKeys();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = false;

        if(!encryption.isEncryptedMessage(textMessage.getText())) return true;

        // Assume all messages are encrypted then
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                // Start Showing the Message
                if(public_key != null) {
                    // If it's yours or your friends message, whatever decrypt anyway
                    encryption.setPublicKey(public_key);
                    String message = encryption.Decrypt(textMessage.getText());
                    Toast.makeText(context, "SMS DECODED", Toast.LENGTH_SHORT).show();
                    textview.setText(message);
                }
                else{
                    Toast.makeText(context, "Encrypted message cannot be decoded\n(You do not have the decryption key)", Toast.LENGTH_SHORT).show();
                }

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

    // Data Members
    public Context context;
    private String name;
    private String friendsNumber;
    private int threadid;
    private String myPhoneNumber;
    private boolean openConversation;


    public touchListener_Contact(String _name, String phoneno, int threadno, String myPhoneNo, Context con) {
        name = _name;
        context = con;
        threadid = threadno;
        friendsNumber = phoneno;
        openConversation = true;
        myPhoneNumber = myPhoneNo;
    }

    final Handler handler = new Handler();

    Runnable openContactManager = new Runnable() {
        public void run() {
            openConversation = false;
            Intent intent = new Intent(context, ContactsActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phoneno", friendsNumber);
            intent.putExtra("threadid", threadid);
            //intent.putExtra("UserPrivateKey" , encryption.privateKey);
            context.startActivity(intent);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = false;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            // Wait one second
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(openContactManager, 1000);
                openConversation = true;
                retVal = true;
                break;

            // Action Cancelled and Action Move
            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(openContactManager);
                openConversation = false;
                retVal = true;
                break;

            // Stop take some time to think,
            // Gotta make annn importanntt decisionnnn!
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(openContactManager);
                if (openConversation) {
                    //show what number was selected
                    Toast.makeText(context, friendsNumber, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("phoneNo", friendsNumber);
                    intent.putExtra("MyPhoneno", myPhoneNumber);
                    intent.putExtra("threadid", threadid);
                    //intent.putExtra("UserPrivateKey" , encryption.privateKey);
                    //DataWrapper dw = new DataWrapper(chatMessageList);
                    //intent.putExtra("data", dw);
                    context.startActivity(intent);
                    retVal = true;
                }
                break;
        }
        return retVal;
    }
}