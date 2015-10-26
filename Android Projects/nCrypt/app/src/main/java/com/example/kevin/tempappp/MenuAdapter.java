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

public class MenuAdapter extends ArrayAdapter<Conversation> {

    private final Context context;
    private final ArrayList<Conversation> phoneNoArrayList;
    private final String myPhoneNumber;

    public MenuAdapter(Context context, ArrayList<Conversation> phoneNoArrayList, String myPhone) {

        super(context, R.layout.row, phoneNoArrayList);

        this.context = context;
        this.phoneNoArrayList = phoneNoArrayList;
        this.myPhoneNumber = myPhone;

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

        // 3. Check for name and  set the text for textView
        if(phoneNoArrayList.get(position).getName().equals(""))
            msgView.setText(phoneNoArrayList.get(position).getPhoneNumber());
        else
            msgView.setText(phoneNoArrayList.get(position).getName());

        phoneNoArrayList.get(position).getThreadId();

        // 4. Set onTouch Listener
        msgView.setOnTouchListener(
                new touchListener_Contact(
                        phoneNoArrayList.get(position).getName(),
                        phoneNoArrayList.get(position).getPhoneNumber(),
                        phoneNoArrayList.get(position).getThreadId(),
                        myPhoneNumber,
                        context));

        //set incoming or outgoing
        msgView.setGravity(Gravity.LEFT);

        // 5. return rowView
        return rowView;


    }

}