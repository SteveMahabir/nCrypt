package com.example.kevin.tempappp;

import android.app.Application;

/**
 * Created by Nickademus on 10/21/15.
 *
 * So apparently this is how to make global vars in Android.
 * The nCryptApplication class extends Applications, and is now defined in the manifest as the name of the application
 * we can now call this thing by going ((nCryptApplication)this.getApplication()) and then you can access the methods in here
 *
 * Anyway, as you can see in the CTOR im making the Encryption object and setting it up here
 */
public class nCryptApplication extends Application {

//Internals
    private Encryption encryption;

//CTOR
    public nCryptApplication(){
        super();
        // Setup and generate new encryption keys
        //encryption = new Encryption();

    }

// Accessors
    //public Encryption getEncryption() {
    //    return encryption;
   // }


}