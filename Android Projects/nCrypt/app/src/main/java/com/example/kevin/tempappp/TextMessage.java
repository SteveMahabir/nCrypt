package com.example.kevin.tempappp;

import android.os.Parcelable;

/**
 * Created by katri on 2015-09-27.
 */
public class TextMessage  {
    private boolean isIncoming;
    private String text;
    private String number;
    private String date;
    private Integer threadId;
    private Integer id;
    
    public TextMessage(boolean in, String t , String n, String d, Integer tid, Integer i)  {
        super();
        this.isIncoming = in;
        this.text = t;
        this.number = n;
        this.date = d;
        this.threadId = tid;
        this.id = i;
    }

    public String getText(){
        return this.text;
    }

    public boolean getIsIncoming()
    {
        return this.isIncoming;
    }

    public String getNumber()
    {
        return this.number;
    }

    public String getDate()
    {
        return this.date;
    }

    public Integer getThreadId()
    {
        return this.threadId;
    }

    public Integer getId()
    {
        return this.id;
    }

}
