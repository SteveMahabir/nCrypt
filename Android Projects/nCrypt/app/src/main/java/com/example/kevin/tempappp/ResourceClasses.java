package com.example.kevin.tempappp;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Nickademus on 10/21/15.
 * This is a place to stuff a bunch of the extras, mostly Listeners and things that we could take out of other code areas
 */

//Class added by Nick
// Touch listener class for the Press and hold picture event in chat area
class touchListener implements View.OnTouchListener{
    private TextView textview;

    public touchListener( TextView tv )
    {
        textview = tv;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean retVal = false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                // Start action ...
                textview.setVisibility(View.VISIBLE);
                retVal = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                v.setPressed(false);
                // Stop action ...
                textview.setVisibility(View.INVISIBLE);
                retVal = true;
                break;
            case  MotionEvent.ACTION_UP:
                v.setPressed(false);
                // Stop action ...
                textview.setVisibility(View.INVISIBLE);
                retVal = true;
                break;
        }
        return retVal;
    }
}
