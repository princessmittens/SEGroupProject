package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Displays information on a course.
 */
public class courseDetails extends Menu {

    private TextView courseNameView;
    private TextView courseSemesterView;
    private TextView courseCrossListView;
    private TextView courseDescription;
    private FirebaseAuth auth;
    private ListView listView;
    private ListAdapter listingsAdapter;

    private ArrayList<Listing> listings;
    private Course inputCourse;

    /**
     * Basic activity functionality once activity is launched.
     * Shows course details fetched from Firebase
     *
     * @param savedInstanceState: app context passed to activity on creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        courseNameView = findViewById(R.id.courseNameView);
        courseSemesterView = findViewById(R.id.courseSemesterView);
        courseCrossListView = findViewById(R.id.courseCrossListView);
        courseDescription = findViewById(R.id.courseDescriptionView);
        listView=findViewById(R.id.listView);

        Intent intent = getIntent();
        inputCourse = (Course) intent.getSerializableExtra("Course");
        listings = (ArrayList<Listing>) intent.getSerializableExtra("Listings");

        listingsAdapter = new ListAdapter(this, listings);
        listView.setAdapter(listingsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Used to notify Firebase which listing (section) was selected by the user
             *
             * @param adapterView: adapterview used to show the list of listings
             * @param view: current view context
             * @param i: index of listview clicked
             * @param l: course ID
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Listing clicked_listing = listingsAdapter.getItem(i);
                UpdateListing(clicked_listing);


                //https://stackoverflow.com/questions/16976431/change-background-color-of-selected-item-on-a-listview
                //we need to change color of the registered CRN
                listingsAdapter.notifyDataSetChanged();
            }
        });

        //userNameView.setText(User.getUser().getCurrent_Identifier());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateFromCourse(inputCourse);

        auth = FirebaseAuth.getInstance();
    }

    /**
     * Update the database with either the section registered for, or the section that was dropped
     *
     * @param selected_listing: listing selected from the course details
     */
    private void UpdateListing(Listing selected_listing) {
        //If we have exactly the same course key and crn we remove it (DROP COURSE)
        if (User.getUser().getRegistered().containsKey(selected_listing.Key)
                && User.getUser().getRegistered().get(selected_listing.Key).equals(String.valueOf(selected_listing.CRN))) {
            User.getUser().getRegistered().remove(selected_listing.Key);
        }
        //Otherwise we update the CRN or add new Key/CRN pair (SWITCH SECTION)
        else{
            User.getUser().getRegistered().put(selected_listing.Key,String.valueOf(selected_listing.CRN));
        }
        firebaseDB.dbInterface.getRootDataReference().child("Users").child(User.getUser().getCurrent_UID()).setValue(User.getUser());
    }

    /**
     * Populates text fields with course information based on an inputted course
     * @param c The course to populate based on
     */
    private void populateFromCourse(Course c){
        courseNameView.setText(c.Name);
        courseDescription.setText(c.Description);
        courseSemesterView.setText(c.Semester);
        courseCrossListView.setText(c.Cross_Listing);
    }
}
