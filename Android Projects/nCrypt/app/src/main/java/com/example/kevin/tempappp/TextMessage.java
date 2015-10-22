package com.example.kevin.tempappp;

import android.os.Parcelable;

/**
 * Created by katri on 2015-09-27.
 */
public class TextMessage  {
    public boolean incoming;
    public String text;
    public String number;
    public String toPhoneNo;

    public TextMessage(boolean incoming, String text , String phoneno)  {
        super();
        this.incoming = incoming;
        this.text = text;
        this.number = phoneno;
        this.toPhoneNo = "";
    }
    public TextMessage(boolean incoming, String text , String phoneno , String ToPhoneNumber)  {
        super();
        this.incoming = incoming;
        this.text = text;
        this.number = phoneno;
        this.toPhoneNo = ToPhoneNumber;
    }
    public String getText(){
        return this.text;
    }

    public boolean getIncoming()
    {
        return this.incoming;
    }
}
