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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private ArrayList<Integer> listingNum;

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
        listingNum = (ArrayList<Integer>) intent.getSerializableExtra("Listings index");

        listingsAdapter = new ListAdapter(this, listings);
        listView.setAdapter(listingsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Listing clicked_listing = listingsAdapter.getItem(i);
                UpdateListing(clicked_listing, i);
                Log.d("UPDATE_LISTING_STATUS",String.valueOf(clicked_listing.CRN));
                Log.d("KEY:", clicked_listing.Key);
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

    private void UpdateListing(Listing selected_listing, int index) {

        //If we have exactly the same course key and crn we remove it (DROP COURSE)
        if (User.getUser().getRegistered().containsKey(selected_listing.Key)
                && User.getUser().getRegistered().get(selected_listing.Key).equals(String.valueOf(selected_listing.CRN))) {
            User.getUser().getRegistered().remove(selected_listing.Key);
            Log.d("UPDATE_LISTING_STATUS",String.valueOf(selected_listing.CRN));
            Log.d("KEY:", selected_listing.Key);
//
            selected_listing.setCurrent_Enrollment(selected_listing.getCurrent_Enrollment() - 1);
            firebaseDB.dbInterface.getRootDataReference().child("Listings").child(String.valueOf(listingNum.get(index))).child("Current_Enrollment").setValue(selected_listing.getCurrent_Enrollment());
        }
        //Otherwise we update the CRN or add new Key/CRN pair (SWITCH SECTION)
        else{
            String registered_CRN;
            if((registered_CRN = User.getUser().getRegistered().get(selected_listing.Key)) != null){
                long CRN = Long.parseLong(registered_CRN);
                Listing l;
                for(int i = 0; i<listings.size(); i++){
                    l = listings.get(i);
                    if(l.CRN == CRN){
                        l.setCurrent_Enrollment(l.getCurrent_Enrollment() - 1);
                        firebaseDB.dbInterface.getRootDataReference().child("Listings").child(String.valueOf(listingNum.get(i))).child("Current_Enrollment").setValue(l.getCurrent_Enrollment());
                    }
                }
            }

            User.getUser().getRegistered().put(selected_listing.Key,String.valueOf(selected_listing.CRN));
            Log.d("UPDATE_LISTING_STATUS",String.valueOf(selected_listing.CRN));
            Log.d("KEY:", selected_listing.Key);
//
            selected_listing.setCurrent_Enrollment(selected_listing.getCurrent_Enrollment() + 1);
            firebaseDB.dbInterface.getRootDataReference().child("Listings").child(String.valueOf(listingNum.get(index))).child("Current_Enrollment").setValue(selected_listing.getCurrent_Enrollment());
        }
        firebaseDB.dbInterface.getRootDataReference().child("Users").child(User.getUser().getUID()).setValue(User.getUser());
//
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
