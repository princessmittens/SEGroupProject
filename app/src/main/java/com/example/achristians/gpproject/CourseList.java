package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AChristians on 2018-05-14.
 */

public class CourseList extends Menu {

    ArrayAdapter<Course> arrayAdapter;

    /**
     *Basic activity functionality once the activity is instantiated.
     * Generates ListView of courses, fetched from Firebase
     *
     * @param savedInstanceState: App context passed into activity on creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView courseListView = findViewById(R.id.courseListView);

        Listing.listings = new ArrayList<>();
        Course.courses = new ArrayList<>();

        //Retrieve the courses from the DB
        fetchCourses();

        //Add data to courseListView
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Course.courses);
        courseListView.setAdapter(arrayAdapter);

        //Register the click listener for a course in the list
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Fetch the listings (i.e. sections) available for each section, so that a
             * particular section can be selected in the course details page.
             *
             * @param parent: AdapterView being clicked on
             * @param view: current view context
             * @param position: position of the clicked item
             * @param id: course identifier for Firebase integration
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(CourseList.this, CourseDetails.class);

            //Get the clicked course
            Course clicked = Course.courses.get(position);
            ArrayList<Listing> availableListings = new ArrayList<Listing>();
            ArrayList<Integer> listingNum = new ArrayList<>();

            //Index variable tracks the listing number, this is used to help with Firebase queries for course capacity
            int index = 0;

            //Find the listings associated with the course
            for(Listing L : Listing.listings){
                if(L.Key.equals(clicked.Key)){
                    availableListings.add(L);
                    listingNum.add(index);
                }
                index++;
            }

            //Putting parameters in the intent
            intent.putExtra("Course", clicked);
            intent.putExtra("Listings", availableListings);
            intent.putExtra("Listings index", listingNum);
            startActivity(intent);
            }
        });
    }

    /**
     * Fetches course information from backing db on startup, no filtering/searching
     */
    public void fetchCourses(){
        //Courses subsection of Database
        DatabaseReference coursesDataReference = Firebase.getRootDataReference().child("Courses/");

        coursesDataReference.addValueEventListener(
            new ValueEventListener() {
                /**
                 * Used to refresh the list of courses from Firebase
                 * @param dataSnapshot: returned data segment from Firebase
                 */
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Course> inputCourses = new ArrayList<>();
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot dsCourse: dataSnapshots) {
                        inputCourses.add(dsCourse.getValue(Course.class));
                    }
                    courseChangeHandler(inputCourses);
                }

                /**
                 * Database error handling
                 * @param databaseError: database error context
                 */
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("Error",databaseError.toString());
                }
            }
        );

        //Listings subsection of Database
        DatabaseReference listingsDataReference = Firebase.getRootDataReference().child("Listings/");

        listingsDataReference.addValueEventListener(
            new ValueEventListener() {
                /**
                 * Refresh again based on data context
                 * @param dataSnapshot: data fetched from Firebase
                 */
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    ArrayList<Listing> listingList = new ArrayList<>();
                    for (DataSnapshot dsListing: dataSnapshots) {
                        listingList.add(dsListing.getValue(Listing.class));
                    }
                    Listing.listings.clear();
                    Listing.listings.addAll(listingList);
                }

                /**
                 * Database error catching
                 * @param databaseError: error from Firebase
                 */
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
     * @param courseListNew A new arrayList of courses to display
     */
    public void courseChangeHandler(ArrayList<Course> courseListNew){
        //Emptying the course list, as anytime data is changed db side this method will
        //be called, and add all elements to the end of the list
        Course.courses.clear();
        Course.courses.addAll(courseListNew);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){
        childBackPressed();
    }
}