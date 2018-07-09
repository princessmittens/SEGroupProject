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
    private TextView registrationPossibility;
    private TextView courseDescription;
    private TextView courseKey;
    private Button btnRegistration;
    private Button btnDropCourse;
    private FirebaseAuth auth;
    private HashMap<String, String> courses_registered;
    private String courseIDInfo="";
    private String  id_string="";
    private DatabaseReference userRef;
    private User u;
    private ListView listView;
    private int selectedListingPosition;
    private ListAdapter listingsAdapter;

    private ArrayList<Listing> listings;
    private Course inputCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        Intent intent = getIntent();
        inputCourse = (Course) intent.getSerializableExtra("Course");
        listings = (ArrayList<Listing>) intent.getSerializableExtra("Listings");

        u = User.getUser();

        userRef = firebaseDB.dbInterface.getRootDataReference().child("Users/" + u.getCurrent_UID());

        courseNameView = findViewById(R.id.courseNameView);
        courseSemesterView = findViewById(R.id.courseSemesterView);
        courseCrossListView = findViewById(R.id.courseCrossListView);
        courseDescription = findViewById(R.id.courseDescriptionView);
        listView=findViewById(R.id.listView);
        userNameView = findViewById(R.id.userNameView);
        listingsArray=new ArrayList<>();
        listingsAdapter = new ListAdapter(this, listingsArray);
        listView.setAdapter(listingsAdapter);
        userNameView.setText(User.getUser().getCurrent_Identifier());
        registrationPossibility = findViewById(R.id.registrationView);
        btnRegistration = findViewById(R.id.registerButton);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnRegistration.setOnClickListener(new View.OnClickListener() {

        populateFromCourse(inputCourse);

        //Drop course button
        btnDropCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we need to register user
                u.getRegistered().remove(id_string);
                //Update the database to reflect dropping the course

                userRef.setValue(u);
                btnRegistration.setEnabled(true);
                btnDropCourse.setEnabled(false);
                registrationPossibility.setText("You dropped the course successfully!");
            }
        });

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null) {
            uid = auth.getCurrentUser().getUid().toString();
        }
    }

    private void UpdateListing(Listing selected_listing) {
        //if we have exactly the same course key and crn we remove it
        if (u.Courses_Registered.containsKey(selected_listing.Key)
                && u.Courses_Registered.get(selected_listing.Key).equals(String.valueOf(selected_listing.CRN))) {
            u.Courses_Registered.remove(selected_listing.Key);
        } else
            //otherwise we update the CRN or add new Key/CRN pair
       {
            u.Courses_Registered.put(selected_listing.Key,String.valueOf(selected_listing.CRN));
        }
        userRef.setValue(u);
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

        courses_registered = u.getRegistered();

        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap

        id_string = Long.toString(listings.get(0).CRN);

        if (courses_registered.containsKey(id_string)) {
            registrationPossibility.setText("You already registered, you can drop");
            btnRegistration.setEnabled(false);
            btnDropCourse.setEnabled(true);
        }
        else {
            registrationPossibility.setText("You can register, you cannot drop");
            btnRegistration.setEnabled(true);
            btnDropCourse.setEnabled(false);
        }

        for (String key : courses_registered.keySet()) {
            Log.d("COURSES_REGISTERED",key);
            Log.d("COURSES_REGISTERED",courses_registered.get(key));
        }

    }//End populateFromCourse
}//End Class

                Query query = listingsRef.orderByChild("Key").equalTo("CSCI 1105 FALL (1) : 04-SEP-2018 - 04-DEC-2018");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("LISTING DATA SIZE",String.valueOf((dataSnapshot.getChildrenCount())));
                       if (dataSnapshot.exists()) {
                           for (DataSnapshot issue : dataSnapshot.getChildren()) {
                               // do something with the individual "issues"
                               Listing l = issue.getValue(Listing.class);
                               if (l.Format.equals("Lec")) {
                                   boolean duplicate=false;
                                   Log.d("listing key ",l.Key);
                                   Log.d("listing CRN ",String.valueOf(l.CRN));
                                   for (int i=0;i<listingsArray.size();i++) {
                                       if (listingsArray.get(i).CRN==l.CRN && listingsArray.get(i).Key.equals(l.Key)) duplicate=true;
                                   }
                                   if (!duplicate) listingsArray.add(l);
                               }
                           }
                           Log.d("listing array size",String.valueOf(listingsArray.size()));
                           //listingsAdapter.addAll(listingsArray);
                           listView.setAdapter(listingsAdapter);
                           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                               @Override
                               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Listing clicked_listing = listingsAdapter.getItem(i);
                                    UpdateListing(clicked_listing);
                                   selectedListingPosition=i;
                                   Log.d("UPDATE_LISTING_STATUS",String.valueOf(clicked_listing.CRN));
                                   //https://stackoverflow.com/questions/16976431/change-background-color-of-selected-item-on-a-listview
                                   //we need to change color of the registered CRN
                                   listingsAdapter.notifyDataSetChanged();
                               }
                           });

                       }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("FB", "Listener cancelled for Courses");
            }
        });
    }

}