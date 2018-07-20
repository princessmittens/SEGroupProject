package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

/** Displays information on a course and provides the interface for registering for courses. */
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
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Listing clicked_listing = listingsAdapter.getItem(i);
                updateListing(clicked_listing);
                Log.d("UPDATE_LISTING_STATUS",String.valueOf(clicked_listing.CRN));
                /* Change Listing color. */
                listingsAdapter.notifyDataSetChanged();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateFromCourse(inputCourse);

        auth = FirebaseAuth.getInstance();
    }

    private void updateListing(Listing selected_listing) {
        //If we have exactly the same course key and crn we remove it (DROP COURSE)
        if (User.getUser().getRegistered().containsKey(selected_listing.Key)
                && User.getUser().getRegistered().get(selected_listing.Key).equals(String.valueOf(selected_listing.CRN))) {
            //Otherwise we update the CRN or add new Key/CRN pair (SWITCH SECTION)
            User.getUser().getRegistered().remove(selected_listing.Key);
        } else {
            //Otherwise we update the CRN or add new Key/CRN pair (SWITCH SECTION)
            User.getUser().getRegistered().put(selected_listing.Key,String.valueOf(selected_listing.CRN));
        }
        firebaseDB.dbInterface.getRootDataReference().child("Users").child(User.getUser().getUID()).setValue(User.getUser());
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
    }//End populateFromCourse
}//End Class
