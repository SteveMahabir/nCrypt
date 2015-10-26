package com.example.kevin.tempappp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ContactsActivity extends Activity {

    // Contact Info
    String phoneNumber;
    String name;

    // Database Object
    public DBAdapter db;

    // Edit Text View
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Get Contact Information
        name = String.valueOf(getIntent().getExtras().getString("name"));
        if(name == null)
            name = "";

        phoneNumber = String.valueOf(getIntent().getExtras().getString("phoneno"));
        if(phoneNumber == null)
            phoneNumber = "";

        et = (EditText)findViewById(R.id.editTextName);
        et.setText(name);

        et = (EditText)findViewById(R.id.editTextPhone);
        et.setText(phoneNumber);

        if(!phoneNumber.equals(""))
            et.setEnabled(false);

        // Database Setup
        db = new DBAdapter(this);
    }


    public void onClickEvent(View view) {
        switch(view.getId())
        {
            case(R.id.buttonUpdate):
                if(infoValidated()) {
                    db.open();
                    if(db.updateContact(phoneNumber, name, null)) {
                        db.close();
                        Toast.makeText(this, "Success, " + name +  " Updated!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else {
                        db.close();
                        Toast.makeText(this, "Failed to update contact", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "Please fill in Name and Number", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
    }

    private boolean infoValidated() {
        boolean retVal;

        et = (EditText)findViewById(R.id.editTextName);
        name = et.getText().toString();
        if(name.equals("") || name == null)
            return false;

        et = (EditText)findViewById(R.id.editTextPhone);
        phoneNumber = et.getText().toString();
        if(name.equals("") || name == null)
            return false;

        return true;
    }
}
