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
	private String toPhoneNo;
    
    public TextMessage(boolean in, String t , String n, String d, Integer tid, Integer i)  {
        super();
        this.isIncoming = in;
        this.text = t;
        this.number = n;
        this.date = d;
        this.threadId = tid;
        this.id = i;
    }

	public TextMessage(boolean in, String t , String n, String ton, String d, Integer tid, Integer i)  {
        super();
        this.isIncoming = in;
        this.text = t;
        this.number = n;
        this.date = d;
        this.threadId = tid;
        this.id = i;
		this.toPhoneNo = ton;
    }
    
	public String getToPhoneno(){
    	return this.toPhoneNo;
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
