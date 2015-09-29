package com.example.kevin.tempappp;

/**
 * Created by katri on 2015-09-27.
 */
public class TextMessage {
    public boolean incoming;
    public String text;

    public TextMessage(boolean incoming, String text) {
        super();
        this.incoming = incoming;
        this.text = text;
    }
    public String getText(){
        return this.text;
    }

    public boolean getIncoming()
    {
        return this.incoming;
    }
}
