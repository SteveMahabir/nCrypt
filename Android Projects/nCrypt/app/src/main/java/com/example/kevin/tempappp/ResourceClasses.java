package com.example.kevin.tempappp;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nickademus on 10/21/15.
 * This is a place to stuff a bunch of the extras, mostly Listeners and things that we could take out of other code areas
 */

final class Resources {

    private Resources(){};
    public static String FormattedDate(long timeMillis)
    {
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        return format.format(date);
    }
}


//Class added by Nick
// Touch listener class for the Press and hold picture event in chat area
class touchListener implements View.OnTouchListener{
    private TextView textview;
    private TextMessage textMessage;
    private nCryptApplication globals;
    private Context context;
    public touchListener( TextView tv, TextMessage tm, Context con)
    {
        textview = tv;
        textMessage = tm;
        globals = MainActivity.globals;
        context = con;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                // Start action ...
                String  message = globals.getEncryption().Decrypt(textMessage.getText());
                Toast.makeText(context, "DECODED : " + message, Toast.LENGTH_SHORT).show();
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
