package com.example.achristians.gpproject;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by AChristians on 2018-05-14.
 */

public class weeklySchedule extends AppCompatActivity {

    String[] days = {"Mon","Tue","Wed","Thu","Fri"};
    ArrayList<TextView> daysNames=new ArrayList<>();
    ArrayList<LinearLayout> daysColumns = new ArrayList<>();
    LinearLayout daysLayout;
    ArrayList<Listing> listingList = new ArrayList<>();
    ArrayList<ArrayList<Listing>> listingsByDays = new ArrayList();
    //Match days letters like M, T, W with the arralist indexes
    HashMap<Character, Integer> daysAbbr= new HashMap<>();
    //display width
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_sched);

        daysAbbr.put('M',0); //monday
        daysAbbr.put('T',1); //tuesday
        daysAbbr.put('W',2); //wednesday
        daysAbbr.put('R',3); //thursday
        daysAbbr.put('F',4); //friday

        //https://stackoverflow.com/questions/17481341/how-to-get-android-screen-size-programmatically-once-and-for-all
        //to create columns with even size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        daysLayout = findViewById(R.id.horizontalLayout);
        for (int i = 0;i<days.length;i++) {
            LinearLayout daysColumn = new LinearLayout(this);
            daysColumn.setOrientation(LinearLayout.VERTICAL);

            daysColumn.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            daysColumn.setHorizontalGravity(1);

            daysLayout.addView(daysColumn);
            View blackLine = new View(this);
            daysLayout.addView(blackLine);
            blackLine.setLayoutParams(new LinearLayout.LayoutParams(3, LinearLayout.LayoutParams.MATCH_PARENT));
            blackLine.setBackgroundColor(Color.parseColor("#000000"));

            daysColumns.add(daysColumn);

            TextView day=new TextView(this);
            day.setWidth(width/days.length);
            day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            day.setText(days[i]);
            daysColumn.addView(day);
            blackLine = new View(this);
            daysColumn.addView(blackLine);
            blackLine.setLayoutParams(new LinearLayout.LayoutParams(width/days.length,2 ));
            blackLine.setBackgroundColor(Color.parseColor("#000000"));

            daysNames.add(day);
        }

        Database.dbInterface = new Database(getApplicationContext());


        fetchListings();

    }


    void fetchListings() {
        DatabaseReference listingsDataReference = Database.rootDataReference.child("Listings/");

        listingsDataReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                        for (DataSnapshot dsListing: dataSnapshots) {
                            //add listings only for the current user
                            Listing l = dsListing.getValue(Listing.class);
                            //Log.d("LISTING:",String.valueOf(l.CRN));
                            if (User.getUser().getRegistered().containsKey(l.Key) && User.getUser().getRegistered().get(l.Key).equals(String.valueOf(l.CRN))) {
                                listingList.add(l);
                                //Log.d("RESULT_LOG","Match found. add listing to the list");
                                //Log.d("CRN is ", String.valueOf(l.CRN));
                            }
                            //if (User.getUser().getRegistered().containsKey(l.Key)) Log.d("FOUND MATCH KEY", l.Key);

                        }
                        Log.d("RESULT_LOG: ", String.valueOf(listingList.size()));
                        Log.d("RESULT_LOG: ",String.valueOf(User.getUser().getRegistered().size()));

                        //populate timetable with listings
                        PopulateTimetable(listingList);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("Error",databaseError.toString());
                    }
                }
        );

    }

    void PopulateTimetable(ArrayList<Listing> listingList) {

        //create 5 lists for 5 days
        for (int i=0;i<5; i++) {
            listingsByDays.add(new ArrayList<Listing>());
        }


        //put listings to the days array list accourding to the 'Days' key. For example if Days are "TR" we need to put this listing
        //both in the tuesday list and to the thursday list.
        for (Listing l: listingList) {
            String days = l.Days;
            for (int i = 0; i < days.length(); i++) {
                int index = daysAbbr.get(days.charAt(i));
                listingsByDays.get(index).add(l);
                Log.d("RESULT_LOG: ", "Add listing CRN " + l.CRN + " to the day " + String.valueOf((index)));
            }
        }

        //now we need to sort each day by the starting time.
        //We need Comparator to sort listings using their Listing.Time string field
        //https://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
        class TimeComparator implements Comparator<Listing> {
            @Override
            public int compare(Listing a, Listing b) {
                return a.Time.compareToIgnoreCase(b.Time);
                }
        }

        for (ArrayList<Listing> day: listingsByDays) {
            Collections.sort(day, new TimeComparator());
        }

        //now we need just iterate through all days and add all listings to the corresponding timetable layouts
        int index=0;
        for (ArrayList<Listing> day: listingsByDays) {
            for (Listing l: day) {
                TextView listingView=new TextView(this);
                listingView.setWidth(width/days.length);
                //To make text like "CSCI 1105"
                String text="";
                text+="CRN: "+l.CRN;
                text+= "\n"+l.Key.substring(0,9);
                text+="\n"+l.Time;
                listingView.setText(text);
                LinearLayout column = daysColumns.get(index);
                column.addView(listingView);

                // add black line separator between coures
                View blackLine = new View(this);
                column.addView(blackLine);
                blackLine.setLayoutParams(new LinearLayout.LayoutParams(width/days.length, 3));
                blackLine.setBackgroundColor(Color.parseColor("#000000"));

            }

            //we need to count index to know to what layout to add listing
            index++;
        }

    }

}

