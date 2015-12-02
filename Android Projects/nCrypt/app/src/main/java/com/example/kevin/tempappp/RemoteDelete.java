package com.example.kevin.tempappp;

import java.io.Serializable;

/**
 * Created by Steve on 2015-12-01.
 */

// Used to hold a remote conversation delete object
public class RemoteDelete implements Serializable {
    private String my_phone_number;
    RemoteDelete(String phone_number){
        my_phone_number = phone_number;
    }
}