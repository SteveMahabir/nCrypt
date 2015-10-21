package com.example.kevin.tempappp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kevin on 10/17/2015.
 */
public class DataWrapper implements Serializable {

    private ArrayList<TextMessage> textMessages;

    public DataWrapper(ArrayList<TextMessage> data) {
        this.textMessages = data;
    }

    public ArrayList<TextMessage> getArray() {
        return this.textMessages;
    }

}