package com.example.achristians.gpproject;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView

public class ListAdapter extends ArrayAdapter<Listing> {

    public ListAdapter(Context context, ArrayList<Listing> listing) {
        super(context, 0, listing);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Listing l = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listing_layout, parent, false);
        }
        boolean match=false;

        //check if the user registered to that course
        //check if the user registered for the course and CRN, then we need to display it differently
        if (User.getUser().getRegistered().containsKey(l.Key) && User.getUser().getRegistered().get(l.Key).equals(String.valueOf(l.CRN))) {
            match = true;
            Log.d("Match found","Match found");
        }
        Log.d("USER_DATA_IN_ADAPTER",User.getUser().getIdentifier());
        Log.d("l.key is ",l.Key);


        TextView crnView = convertView.findViewById(R.id.crnView);
        TextView instructorView = convertView.findViewById(R.id.instructorView);
        TextView daysView = convertView.findViewById(R.id.daysView);
        TextView locationView = convertView.findViewById(R.id.locationView);
        TextView statusView = convertView.findViewById(R.id.statusView);

        crnView.setText(String.valueOf(l.CRN));
        instructorView.setText(l.Instructor);
        daysView.setText(l.Days);
        locationView.setText(l.Location);

        if (match) {
            crnView.setTypeface(null, Typeface.BOLD);
            instructorView.setTypeface(null, Typeface.BOLD);
            daysView.setTypeface(null, Typeface.BOLD);
            locationView.setTypeface(null, Typeface.BOLD);
            statusView.setText("Registered");
            statusView.setTypeface(null, Typeface.BOLD);
        } else
        {
            crnView.setTypeface(null, Typeface.NORMAL);
            instructorView.setTypeface(null, Typeface.NORMAL);
            daysView.setTypeface(null, Typeface.NORMAL);
            locationView.setTypeface(null, Typeface.NORMAL);
            statusView.setText("Not registered");
            statusView.setTypeface(null, Typeface.NORMAL);
        }
        return convertView;
    }
}
