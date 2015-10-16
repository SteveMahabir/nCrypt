package com.andriod.steve.textfield;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {

    // Persistence
    Boolean isMale;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText et = (EditText)findViewById(R.id.editText);
        SharedPreferences settings = getSharedPreferences("state", Context.MODE_PRIVATE);
        isMale = settings.getBoolean("gender", false);
        name = settings.getString("name", name);
        et.setText(name);
        if(isMale) {
            RadioButton rd = (RadioButton) findViewById(R.id.radioMale);
            rd.setChecked(true);
        }
        else {
            RadioButton rd = (RadioButton) findViewById(R.id.radioFemale);
            rd.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickEvent(View view) {
        switch(view.getId())
        {
            case R.id.buttonStuff:
                Intent i = new Intent(this, ReportActivity.class);

                EditText et = (EditText)findViewById(R.id.editText);
                name = et.getText().toString();

                RadioButton rd = (RadioButton)findViewById(R.id.radioMale);
                if(rd.isChecked())
                    isMale = true;
                else
                    isMale = false;

                break;
            case R.id.radioMale:
                isMale = true;
                break;
            case R.id.radioFemale:
                isMale = false;
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences settings = getSharedPreferences("state", Context.MODE_PRIVATE);

    }

    public void ReadFromFile(View view) {
        try {

        }
        catch(Exception e){}
    }

    public void WriteToFile(View view) {
        try {
            FileOutputStream ofile = openFileOutput("test.txt", MODE_PRIVATE);
            OutputStreamWriter owriter = new OutputStreamWriter(ofile);
            EditText ed = (EditText)findViewById(R.id.editText);
            owriter.write(ed.getText().toString());
            owriter.flush();
            owriter.close();
        }
        catch(Exception e){e.printStackTrace();}

    }
}
