package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AChristians on 2018-05-14.
 */

public class registration extends Menu {

    //Full list of courses from DB
    ArrayAdapter<Course> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView courseListView = findViewById(R.id.courseListView);

        Listing.listings = new ArrayList<>();
        Course.courses = new ArrayList<>();

        Database.dbInterface = new Database(getApplicationContext());
        fetchCourses();

        //Add data to courseListView
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Course.courses);
        courseListView.setAdapter(arrayAdapter);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(registration.this, courseDetails.class);

                Course clicked = Course.courses.get(position);
                ArrayList<Listing> availableListings = new ArrayList<Listing>();

                for(Listing L : Listing.listings){
                    if(L.Key.equals(clicked.Key)){
                        availableListings.add(L);
                    }
                }

                intent.putExtra("Course", clicked);
                intent.putExtra("Listings", availableListings);
                startActivity(intent);
            }
        });
    }

    /**
     * Fetches course information from backing db on startup, no filtering/searching
     */
    public void fetchCourses(){
        DatabaseReference coursesDataReference = Database.rootDataReference.child("Courses/");

        coursesDataReference.addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Course> inputCourses = new ArrayList<>();
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot dsCourse: dataSnapshots) {
                        inputCourses.add(dsCourse.getValue(Course.class));
                    }
                    courseChangeHandler(inputCourses);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("Error",databaseError.toString());
                }
            }
        );

        DatabaseReference listingsDataReference = Database.rootDataReference.child("Listings/");

        listingsDataReference.addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    Listing.listings.clear();
                    for (DataSnapshot dsListing: dataSnapshots) {
                        Listing.listings.add(dsListing.getValue(Listing.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("Error",databaseError.toString());
                }
            }
        );
    }

    /**
     * Handles the event of course data being pushed to the application from an external source
     * (The DB). Extracted from the value event listener so functionality/inputs can be mocked for
     * testing.
     * @param courseListNew A new arraylist of courses to display
     */
    public void courseChangeHandler(ArrayList<Course> courseListNew){
        //Emptying the course list, as anytime data is changed db side this method will
        //be called, and add all elements to the end of the list
        Course.courses.clear();
        Course.courses.addAll(courseListNew);
        arrayAdapter.notifyDataSetChanged();
    }
}