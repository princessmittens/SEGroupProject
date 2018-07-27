package com.example.achristians.gpproject;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
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

public class WeeklySchedule extends AppCompatActivity {

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
    int height;
    int activityTopHeight=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        height=size.y;

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
            day.setTypeface(null, Typeface.BOLD);
            day.setBackgroundColor(Color.parseColor("#E0E0E0"));
            daysColumn.addView(day);
            blackLine = new View(this);
            daysColumn.addView(blackLine);
            blackLine.setLayoutParams(new LinearLayout.LayoutParams(width/days.length,8 ));
            blackLine.setBackgroundColor(Color.parseColor("#000000"));

            daysNames.add(day);
            //remember height of this view, for the future calculations
        }




        fetchListings();

    }

    /**
     * Retrieve the list of user lectures from the firebase database.
     * Does not take any params
     */
    void fetchListings() {
        DatabaseReference listingsDataReference = Firebase.getRootDataReference().child("Listings/");

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
    /**
     * Put text views with the lectures to the timetable layout by days and time.
     * Each lecture box height depends on the duration of the lecture
     * @param listingList stores all the lectures of the student that should be displayed
     */
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

        String day_start_end_string="0835-2100";
        double dayStart=StartTime(day_start_end_string);
        double dayEnd=EndTime(day_start_end_string);
        double dayDuration=dayEnd-dayStart;
        Log.d("RESULT_LOG:","Day duration is "+String.valueOf(dayDuration));
        //will figure out activityTopHeight later
        activityTopHeight=50;
        double windowHeight = height - activityTopHeight;
        Log.d("RESULT_LOG:","Screen height is "+String.valueOf(height));
        Log.d("RESULT_LOG:","Timetable height is "+String.valueOf(windowHeight));

        Log.d("RESULT_LOG:","Size of the listingsByDays is "+String.valueOf(listingsByDays.size()));
        for (ArrayList<Listing> day: listingsByDays) {
            LinearLayout column = daysColumns.get(index);
            //in the beggining of the day previous course end is actually start of the day
            double endOfPreviousCourse=StartTime(day_start_end_string);
            for (Listing l: day) {

                //add empty box before the course

                TextView listingView = new TextView(this);

                TextView emptyView = new TextView(this);
                listingView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                listingView.setWidth(width / days.length);
                //To make text like "CSCI 1105"
                String text = "";
                text += "CRN: " + l.CRN;
                text += "\n" + l.Key.substring(0, 9);
                text += "\n" + l.Time;

                //lets try to make it height depending on the time
                double courseStart = 0;
                double courseEnd = 0;
                courseStart = StartTime(l.Time);
                courseEnd = EndTime(l.Time);
                double courseDuration = 0;
                double emptyDuration = 0;
                courseDuration = courseEnd - courseStart;
                emptyDuration = courseStart - endOfPreviousCourse;
                Log.d("RESULT_LOG:", "Course " + l.CRN + " duration is " + String.valueOf(courseDuration));

                int courseHeight = (int) ((courseDuration) * windowHeight / dayDuration);
                int emptyHeight = (int) ((emptyDuration) * windowHeight / dayDuration);
                Log.d("RESULT_LOG:", "Course " + l.CRN + " height is " + String.valueOf(courseHeight));
                listingView.setHeight(courseHeight);
                emptyView.setHeight(emptyHeight);
                //update end of PreviouseCourse for the next course;
                endOfPreviousCourse = courseEnd;

                emptyView.setBackgroundColor(Color.parseColor("#E0E0E0"));
                listingView.setText(text);

                if (emptyHeight>0)  {
                    column.addView(emptyView);
                    View blackLine = new View(this);
                    column.addView(blackLine);
                    blackLine.setLayoutParams(new LinearLayout.LayoutParams(width / days.length, 3));
                    blackLine.setBackgroundColor(Color.parseColor("#000000"));
                }
                column.addView(listingView);


                // add black line separator between coures

                    View lineAfterEmptyBox = new View(this);
                    column.addView(lineAfterEmptyBox);
                    lineAfterEmptyBox.setLayoutParams(new LinearLayout.LayoutParams(width / days.length, 3));
                    lineAfterEmptyBox.setBackgroundColor(Color.parseColor("#000000"));

            }
            View endBox=new TextView(this);
            endBox.setBackgroundColor(Color.parseColor("#E0E0E0"));



            column.addView(endBox);
            endBox.setLayoutParams(new LinearLayout.LayoutParams(width/days.length, height-column.getTop()));

            //we need to count index to know to what layout to add listing
            index++;
        }

    }

    /**
     * Extract and convert lecture start time from the string
     * @param time stores the string with the start and end time of the lecture
     */
    public static double StartTime(String time) {
        double start=0;
        if (time.length()<4) return 0;
        start = Double.valueOf(time.substring(0,2))*100;
        start += Double.valueOf(time.substring(2,4))*100/60;
        return start;
    }

    /**
     * Extract and convert lecture end time from the string
     * @param time stores the string with the start and end time of the lecture
     */
    public static double EndTime(String time) {
        double end=0;
        if (time.length()<6) return 0;
        end = Double.valueOf(time.substring(5, 7)) * 100;
        end += Double.valueOf(time.substring(7, 9)) * 100 / 60;
        return end;
    }
}

