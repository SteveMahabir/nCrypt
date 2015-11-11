package com.example.kevin.tempappp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;

/**
 * Created by Nickademus on 10/27/15.
 */

public class IconPopup extends Activity{

    private SkullColour skullour;
    private SkullType skullType;
    private int spinnerSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_icon_popup);

        //change the widith and height using the phone's resolution size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * 0.8);
        int heigh = (int)(dm.heightPixels * 0.8 );
        getWindow().setLayout( width,heigh  );

        //if there is a saved skull, get it, if not, purple
        SharedPreferences sharedpref = getSharedPreferences("state", Context.MODE_PRIVATE);
        //So enums are classes in java, not ints, so have to do some stupd string crap to get this to work
        skullour =  SkullColour.valueOf( sharedpref.getString("skullour", SkullColour.Pueple.toString() ));
        skullType = SkullType.valueOf( sharedpref.getString("skullType", SkullType.Glow.toString() ));
        spinnerSelect = sharedpref.getInt("spinnerSelect",0 );

        //set the spinner setOnItemSelectedListener
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_colourPicker);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                skullour = SkullColour.values()[position];
                spinnerSelect = position;
                spinner.setSelection( position );
                loadRadioIcons(skullour);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //set the spinner to the right colour
        spinner.setSelection( spinnerSelect );
        //load the right set of radio buttons
        loadRadioIcons(skullour);
        //select the right radio button

    }//end oncreate

    private void loadRadioIcons( SkullColour skullour ) {
        RadioButton rbEyes = (RadioButton)findViewById( R.id.rb_eyes );
        RadioButton rbGlow = (RadioButton)findViewById( R.id.rb_glow );
        RadioButton rbFlat = (RadioButton)findViewById( R.id.rb_flat );
        switch ( skullour ) {
            case Pueple:
                rbEyes.setButtonDrawable(R.drawable.purpleeyes);
                rbGlow.setButtonDrawable(R.drawable.purplering);
                rbFlat.setButtonDrawable(R.drawable.purpleskull);
                break;
            case DarkRed:
                rbEyes.setButtonDrawable(R.drawable.darkredeyes);
                rbGlow.setButtonDrawable(R.drawable.darkredring);
                rbFlat.setButtonDrawable(R.drawable.darkredskull);
                break;
            case Pink:
                rbEyes.setButtonDrawable(R.drawable.pinkeyes);
                rbGlow.setButtonDrawable(R.drawable.pinkring);
                rbFlat.setButtonDrawable(R.drawable.pinkskull);
                break;
            case Red:
                rbEyes.setButtonDrawable(R.drawable.redeyes);
                rbGlow.setButtonDrawable(R.drawable.redring);
                rbFlat.setButtonDrawable(R.drawable.redskull);
                break;
            case Orange:
                rbEyes.setButtonDrawable(R.drawable.orangeeyes);
                rbGlow.setButtonDrawable(R.drawable.orangering);
                rbFlat.setButtonDrawable(R.drawable.orangeskull);
                break;
            case Yellow:
                rbEyes.setButtonDrawable(R.drawable.yelloweyes);
                rbGlow.setButtonDrawable(R.drawable.yellowring);
                rbFlat.setButtonDrawable(R.drawable.yellowskull);
                break;
            case Green:
                rbEyes.setButtonDrawable(R.drawable.greeneyes);
                rbGlow.setButtonDrawable(R.drawable.greenring);
                rbFlat.setButtonDrawable(R.drawable.greenskull);
                break;
            case Cyan:
                rbEyes.setButtonDrawable(R.drawable.cyaneyes);
                rbGlow.setButtonDrawable(R.drawable.cyanring);
                rbFlat.setButtonDrawable(R.drawable.cyanskull);
                break;
            case Aqua:
                rbEyes.setButtonDrawable(R.drawable.aquaeyes);
                rbGlow.setButtonDrawable(R.drawable.aquaring);
                rbFlat.setButtonDrawable(R.drawable.aquaskull);
                break;
            case Blue:
                rbEyes.setButtonDrawable(R.drawable.blueeyes);
                rbGlow.setButtonDrawable(R.drawable.bluering);
                rbFlat.setButtonDrawable(R.drawable.blueskull);
                break;
        }//end switch

    }// end setRadioIcons

    public void SaveButtonSave(View view) {
        SharedPreferences sharedpref = getSharedPreferences("state", Context.MODE_PRIVATE);
        //So enums are classes in java, not ints, so have to do some stupd string crap to get this to work
        //skullour =  SkullColour.valueOf( sharedpref.getString("skullour", SkullColour.Pueple.toString() ));
        //skullType = SkullType.valueOf( sharedpref.getString("skullType", SkullType.Glow.toString() ));



        SharedPreferences.Editor editor = sharedpref.edit();

        editor.putString("skullour", skullour.toString());
        editor.putString("skullType", skullType.toString());
        editor.putInt("spinnerSelect", ((Spinner)findViewById(R.id.spinner_colourPicker)).getSelectedItemPosition() );

        editor.commit();


    }

    public void RbuttonOnclick(View view) {
        switch ( view.getId() ){
            case R.id.rb_eyes:
                skullType = SkullType.Eyes;
                break;
            case R.id.rb_glow:
                skullType = SkullType.Glow;
                break;
            case R.id.rb_flat:
                skullType = SkullType.Flat;
                break;

        }//end switch

    }


    /*
    * TODO button backgrounds
    *
    *
        <RadioButton
        android:id="@+id/radio0"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/yourbuttonbackground"
        android:button="@android:color/transparent"
        android:checked="true"
        android:text="RadioButton1" />

    res/drawable/yourbuttonbackground.xml

        <selector xmlns:android="http://schemas.android.com/apk/res/android" >
            <item
                android:drawable="@drawable/b"
                android:state_checked="true"
                android:state_pressed="true" />
            <item
                android:drawable="@drawable/a"
                android:state_pressed="true" />
            <item
                android:drawable="@drawable/a"
                android:state_checked="true" />
            <item
                android:drawable="@drawable/b" />
        </selector>



    res/drawable/a.xml - Selected State

        <shape
            xmlns:android="http://schemas.android.com/apk/res/android"
            android:shape="rectangle" >
            <corners
                android:radius="5dp" />
            <solid
                android:color="#fff" />
            <stroke
                android:width="2dp"
                android:color="#53aade" />
        </shape>



    res/drawable/b.xml - Regular State

        <shape
            xmlns:android="http://schemas.android.com/apk/res/android"
            android:shape="rectangle" >
            <corners
                android:radius="5dp" />
            <solid
                android:color="#fff" />
            <stroke
                android:width="2dp"
                android:color="#555555" />
        </shape>


    *
    *
    * */

}


