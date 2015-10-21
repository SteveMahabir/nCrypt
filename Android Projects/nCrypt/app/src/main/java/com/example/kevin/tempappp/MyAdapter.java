package com.example.kevin.tempappp;

/**
 * Created by Kevin on 9/28/2015.
 */import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<TextMessage> {

    private final Context context;
    private final ArrayList<TextMessage> msgArrayList;

    public MyAdapter(Context context, ArrayList<TextMessage> msgArrayList) {

        super(context, R.layout.row, msgArrayList);

        this.context = context;
        this.msgArrayList = msgArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        TextView msgView;


        ImageView imgViewIn = (ImageView) rowView.findViewById(R.id.incomingLogo);
        ImageView imgViewOut = (ImageView) rowView.findViewById(R.id.outgoingLogo);

        if(msgArrayList.get(position).getIncoming())
        {
            //incoming


            // 3. Get the text view from the rowView
            imgViewOut.setVisibility(View.GONE);
            msgView = (TextView) rowView.findViewById(R.id.incoming);


            // 4. Set the text for textView
            msgView.setText("");
        }
        else
        {
            //outgoing

            imgViewIn.setVisibility(View.GONE);
            // 3. Get the text view from the rowView
            msgView = (TextView) rowView.findViewById(R.id.outgoing);

            // 4. Set the text for textView
            msgView.setText(msgArrayList.get(position).getText());
        }



        //set incoming or outgoing
        msgView.setGravity(msgArrayList.get(position).getIncoming() ? Gravity.LEFT : Gravity.RIGHT);

        // 5. retrn rowView
        return rowView;
    }
}