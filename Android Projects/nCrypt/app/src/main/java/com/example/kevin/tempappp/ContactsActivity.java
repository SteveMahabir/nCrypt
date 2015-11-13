package com.example.kevin.tempappp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ContactsActivity extends Activity {

    // Contact Info
    String phoneNumber;
    String name;
    int Threadid;

    // Database Object
    public DatabaseHelper db;

    // Encryption Object
    Encryption encryption;

    // Edit Text View
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        db = new DatabaseHelper(this);

        // Encryption Object
        encryption = new Encryption();
        encryption.PrepareKeys();

        // Get Contact Name
        name = String.valueOf(getIntent().getExtras().getString("name"));
        if(name == null)
            name = "";

        Threadid = Integer.valueOf(getIntent().getExtras().getInt("threadid"));


        // Get Contact Phone Number
        phoneNumber = String.valueOf(getIntent().getExtras().getString("phoneno"));
        if(phoneNumber == null)
            phoneNumber = "";
        else
        {

            Cursor c = db.getContactByPhoneNumber(phoneNumber);
            db.close();
            Encryption friends_encryption = new Encryption();

            CheckBox cb = (CheckBox)findViewById(R.id.checkPublicKey);
            if (c.moveToFirst())
                cb.setChecked(true);
            else
                cb.setChecked(false);

        }

        // Set Contact Name
        et = (EditText)findViewById(R.id.editTextName);
        if(name == "null") {
            et.setText("");
            et.setHint("Enter Name Here");
        }
        else
            et.setText(name);

        // Set Contact Phone Number
        et = (EditText)findViewById(R.id.editTextPhone);
        et.setText(phoneNumber);

        // If friend, set false
        if(!phoneNumber.equals(""))
            et.setEnabled(false);

        if(phoneNumber == "null") {
            et.setHint("Enter Phone Number Here");
            et.setText("");
            et.setEnabled(true);
            Button b = (Button) findViewById(R.id.buttonUpdate);
            b.setVisibility(View.INVISIBLE);
        }

    }


    public void onClickEvent(View view) {
        Intent mainmenuint;
        switch(view.getId())
        {
            case(R.id.buttonUpdate):
                if(infoValidated()) {

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

                mainmenuint = new Intent(ContactsActivity.this, MainActivity.class);

                startActivity(mainmenuint);


                break;
            case(R.id.buttonChat):
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String myphoneNumber;
                if(!tm.getLine1Number().isEmpty())
                    myphoneNumber = (String)tm.getLine1Number();
                else
                    myphoneNumber = "5195202520";

                EditText et = (EditText) findViewById(R.id.editTextPhone);
                if(et.getText().toString() == "" ||
                        et.getText().toString() == "null" ||
                        et.getText().length() == 0)
                    break;
                else
                    phoneNumber = et.getText().toString();

                Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
                intent.putExtra("phoneNo", phoneNumber);
                intent.putExtra("MyPhoneno", myphoneNumber);
                intent.putExtra("threadid", Threadid);
                startActivity(intent);
                break;
            case(R.id.buttonDelete):
                if (DeleteThread(Threadid)) {
                    Toast.makeText(this, "Conversation successfully deleted.", Toast.LENGTH_LONG).show();
                    mainmenuint = new Intent(ContactsActivity.this, MainActivity.class);
                    startActivity(mainmenuint);
                }
                else
                {
                    Toast.makeText(this, "Something went wrong, conversation not deleted.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /*
     * returns true if delete was successful,
     * LoadConversations should be called after this function
     */
    public boolean DeleteThread(Integer thread_id)
    {
        long rowsDeleted = db.insertDeletedThread(thread_id);

        db.close();

        return rowsDeleted > 0;
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
