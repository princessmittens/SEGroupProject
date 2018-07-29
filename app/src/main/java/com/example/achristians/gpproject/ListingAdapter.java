package com.example.achristians.gpproject;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView

/**
 * List adapter to display listings of the CourseDetails page
 */
public class ListingAdapter extends ArrayAdapter<Listing> {

    /**
     * Constructor, calls the super
     * @param context AppContext to run with
     * @param listing Input list of Listings
     */
    public ListingAdapter(Context context, ArrayList<Listing> listing) {
        super(context, 0, listing);
    }

    /**
     * Creates the view for a listing (Called for each input listing)
     * @param position Position within the list
     * @param convertView
     * @param parent Parent view item
     * @return A view to display the listing within a list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Listing l = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listing_layout, parent, false);
        }
        boolean match=false;

        User.getUser().getRegistered();

        //check if the user registered for the course and CRN, then we need to display it differently
        if (User.getUser().getRegistered().containsKey(l.Key) && User.getUser().getRegistered().get(l.Key).equals(String.valueOf(l.CRN))) {
            match = true;
        }


        TextView crnView = convertView.findViewById(R.id.crnView);
        TextView instructorView = convertView.findViewById(R.id.instructorView);
        TextView daysView = convertView.findViewById(R.id.daysView);
        TextView locationView = convertView.findViewById(R.id.locationView);
        TextView statusView = convertView.findViewById(R.id.statusView);
        TextView timeView = convertView.findViewById(R.id.TimeView);
        TextView formatView = convertView.findViewById(R.id.formatView);

        timeView.setText(l.Time);
        crnView.setText(String.valueOf(l.CRN));
        instructorView.setText(l.Instructor);
        daysView.setText(l.Days);
        locationView.setText(l.Location);
        formatView.setText(l.Format);

        //If the user is registered for the course, indicate with bold text
        if (match) {
            crnView.setTypeface(null, Typeface.BOLD);
            instructorView.setTypeface(null, Typeface.BOLD);
            daysView.setTypeface(null, Typeface.BOLD);
            locationView.setTypeface(null, Typeface.BOLD);
            statusView.setText("Registered");
            statusView.setTypeface(null, Typeface.BOLD);
            timeView.setTypeface(null, Typeface.BOLD);
            formatView.setTypeface(null, Typeface.BOLD);
        } else
        {
            crnView.setTypeface(null, Typeface.NORMAL);
            instructorView.setTypeface(null, Typeface.NORMAL);
            daysView.setTypeface(null, Typeface.NORMAL);
            locationView.setTypeface(null, Typeface.NORMAL);
            statusView.setText("Not registered");
            statusView.setTypeface(null, Typeface.NORMAL);
            timeView.setTypeface(null, Typeface.NORMAL);
            formatView.setTypeface(null, Typeface.NORMAL);
        }
        return convertView;
    }
}
