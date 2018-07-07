package com.example.achristians.gpproject;

import android.content.Context;
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

        TextView crnView = convertView.findViewById(R.id.crnView);
        TextView instructorView = convertView.findViewById(R.id.instructorView);
        TextView daysView = convertView.findViewById(R.id.daysView);
        TextView locationView = convertView.findViewById(R.id.locationView);

        crnView.setText(String.valueOf(l.CRN));
        instructorView.setText(l.Instructor);
        daysView.setText(l.Days);
        locationView.setText(l.Location);
        return convertView;
    }
}
