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

/**
 * Created by AChristians on 2018-05-14.
 */

public class WeeklySchedule extends AppCompatActivity {

    String[] days = {"Mon","Tue","Wed","Thu","Fri"};
    ArrayList<TextView> daysNames=new ArrayList<>();
    ArrayList<LinearLayout> daysColumns = new ArrayList<>();
    LinearLayout daysLayout;
    ArrayList<Listing> listingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_sched);


        //https://stackoverflow.com/questions/17481341/how-to-get-android-screen-size-programmatically-once-and-for-all
        //to create columns with even size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

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
        Log.d("LISTINGS ADDED: ", String.valueOf(listingList.size()));
        Log.d("USER HASH MAP SIZE",String.valueOf(User.getUser().getRegistered().size()));
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
                                listingList.add(dsListing.getValue(Listing.class));
                                Log.d("Match found","Match found. add listing to the list");
                                Log.d("CRN is ", String.valueOf(l.CRN));
                            }
                            if (User.getUser().getRegistered().containsKey(l.Key)) Log.d("FOUND MATCH KEY", l.Key);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("Error",databaseError.toString());
                    }
                }
        );

    }
}

