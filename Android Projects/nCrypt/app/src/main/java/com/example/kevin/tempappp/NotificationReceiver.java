package com.example.kevin.tempappp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class NotificationReceiver extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //setContentView(R.layout.activity_main);
    }
}