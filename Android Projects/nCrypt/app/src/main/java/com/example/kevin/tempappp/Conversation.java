package com.example.kevin.tempappp;

import java.util.Date;

/**
 * Created by Student on 10/21/2015.
 */
public class Conversation {
    private String name;
    private String phoneNumber;
    private Integer threadId;
    private String lastMessageText;
    private String lastMessageDate;
    private Integer messageCount;

    public Conversation(String n, String p , Integer id, String t, String d, Integer c)  {
        super();
        this.name = n;
        this.phoneNumber = p;
        this.threadId = id;
        this.lastMessageText = t;
        this.lastMessageDate = d;
        this.messageCount = c;
    }

    public String getName(){
        return this.name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public String getLastMessageText(){
        return this.lastMessageText;
    }

    public String getLastMessageDate(){

        return this.lastMessageDate;
    }

    public Integer getThreadId(){

        return this.threadId;
    }

    public Integer getMessageCount(){

        return this.messageCount;
    }
}
