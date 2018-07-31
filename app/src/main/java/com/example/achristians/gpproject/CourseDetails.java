package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * Displays information on a course.
 */
public class CourseDetails extends Menu {

    //Textviews of XML
    private TextView courseNameView;
    private TextView courseSemesterView;
    private TextView courseCrossListView;
    private TextView courseDescription;
    private ListView listView;
    private Button timetableLink;
    private ListingAdapter listingsAdapter;

    //Displayed course, passed from calling activity
    private Course inputCourse;
    //Relevant listings to the opened course, passed from calling activity
    private ArrayList<Listing> listings;
    private Course inputCourse;
    private ArrayList<Integer> listingNum;

    /**
     * Basic activity functionality once activity is launched.
     * Shows course details fetched from Firebase
     * @param savedInstanceState: app context passed to activity on creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        //Connecting view objects with their XML
        courseNameView = findViewById(R.id.courseNameView);
        courseSemesterView = findViewById(R.id.courseSemesterView);
        courseCrossListView = findViewById(R.id.courseCrossListView);
        courseDescription = findViewById(R.id.courseDescriptionView);
        listView=findViewById(R.id.listView);
        timetableLink = findViewById(R.id.timetableButton);

        Intent intent = getIntent();
        //Retrieving passed in information
        try{
            inputCourse = (Course) intent.getSerializableExtra("Course");
            listings = (ArrayList<Listing>) intent.getSerializableExtra("Listings");
        listingNum = (ArrayList<Integer>) intent.getSerializableExtra("Listings index");
        }
        catch(Exception e){
            Log.d("Deserialization Error", "An error occured when deserializing the course and listings:\n" + e.getMessage());
        }

        //Link to timetable
        timetableLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CourseDetails.this, WeeklySchedule.class);
                startActivity(i);
            }
        });

        //we work only with lecture format
        ArrayList<Listing> filteredListings = new ArrayList<>();
        for (Listing l: listings) {
            if (l.Format.equals("Lec")) filteredListings.add(l);
        }
        listings = filteredListings;

        //Populating with dummy information if either is null
        if(inputCourse == null || listings == null){
            inputCourse = Course.exampleCourse;
            listings = Listing.exampleList();
        }

        listingsAdapter = new ListingAdapter(this, listings);
        listView.setAdapter(listingsAdapter);

        //Setting the handler for a Listing click
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
                UpdateListing(clicked_listing, i);
                Log.d("UPDATE_LISTING_STATUS",String.valueOf(clicked_listing.CRN));
                Log.d("KEY:", clicked_listing.Key);
                //https://stackoverflow.com/questions/16976431/change-background-color-of-selected-item-on-a-listview
                //we need to change color of the registered CRN
                listingsAdapter.notifyDataSetChanged();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateFromCourse(inputCourse);
    }

    /**
     * Update the database with either the section registered for, or the section that was dropped
     *
     * @param selected_listing: listing selected from the course details
     */
    private void UpdateListing(Listing selected_listing, int index) {
        //If the user has this CRN in their registered list, remove it (DROP COURSE)
        if (User.getUser().getRegistered().containsKey(selected_listing.Key)
                && User.getUser().getRegistered().get(selected_listing.Key).equals(String.valueOf(selected_listing.CRN))) {
            //Otherwise we update the CRN or add new Key/CRN pair (SWITCH SECTION)
            User.getUser().getRegistered().remove(selected_listing.Key);
            Log.d("UPDATE_LISTING_STATUS",String.valueOf(selected_listing.CRN));
            Log.d("KEY:", selected_listing.Key);
//
            selected_listing.setCurrent_Enrollment(selected_listing.getCurrent_Enrollment() - 1);
            firebaseDB.dbInterface.getRootDataReference().child("Listings").child(String.valueOf(listingNum.get(index))).child("Current_Enrollment").setValue(selected_listing.getCurrent_Enrollment());
        }
        //Otherwise, we add this to the user's registered list with listing.key as it's Key (ADD/SWITCH SECTION)
        //This means that a user can only register for one CRN per course, as it is overwritten
        else{
            if(User.getUser().checkConflict(selected_listing)){
                Toast.makeText(this, "You're registered for a course with a time conflict.",
                        Toast.LENGTH_LONG).show();
                return;
            }
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
        Firebase.getRootDataReference().child("Users").child(User.getUser().getUID()).setValue(User.getUser());
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

    @Override
    public void onBackPressed(){
        childBackPressed();
    }
}
