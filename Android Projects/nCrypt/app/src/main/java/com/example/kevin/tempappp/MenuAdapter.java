package com.example.kevin.tempappp;

/**
 * Created by Kevin on 10/17/2015.
 */
import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<TextMessage> {

    private final Context context;
    private final ArrayList<TextMessage> phoneNoArrayList;

    public MenuAdapter(Context context, ArrayList<TextMessage> phoneNoArrayList) {

        super(context, R.layout.row, phoneNoArrayList);

        this.context = context;
        this.phoneNoArrayList = phoneNoArrayList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row2, parent, false);

        TextView msgView;
        msgView = (TextView) rowView.findViewById(R.id.incoming);


            // 4. Set the text for textView
            msgView.setText(phoneNoArrayList.get(position).getNumber());
            //set incoming or outgoing
            msgView.setGravity(Gravity.LEFT);



        // 5. retrn rowView
        return rowView;


    }

}