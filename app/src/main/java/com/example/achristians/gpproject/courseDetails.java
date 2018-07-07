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

    private String courseID= "";
    private TextView courseCodeView;
    private TextView courseNameView;
    private TextView courseSemesterView;
    private TextView courseCrossListView;
    private TextView registrationPossibility;
    private TextView courseKey;
    private Button btnRegistration;
    private Button btnDropCourse;
    private FirebaseAuth auth;
    private HashMap<String, String> courses_registered;
    private String courseIDInfo="";
    private String  id_string="";
    private DatabaseReference userRef;
    private DatabaseReference listingsRef;
    public static User u;
    private int id;
    private ListView listView;
    private ArrayList<Listing> listingsArray;
    private ListAdapter listingsAdapter;
    private int selectedListingPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        courseCodeView = findViewById(R.id.courseCodeView);
        courseNameView = findViewById(R.id.courseNameView);
        courseSemesterView = findViewById(R.id.courseSemesterView);
        courseCrossListView = findViewById(R.id.courseCrossListView);
        Intent intent = getIntent();
        courseID = intent.getStringExtra(Menu.courseIDstring);
        courseCodeView.setText(courseID);
        id = intent.getIntExtra("id", 0);
        listView=findViewById(R.id.listView);
        listingsArray=new ArrayList<>();
        listingsAdapter = new ListAdapter(this, listingsArray);
        listView.setAdapter(listingsAdapter);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        auth = FirebaseAuth.getInstance();

        String uid="Not signed id";
        if(auth.getCurrentUser()!=null) {
            uid = auth.getCurrentUser().getUid().toString();
        }
        populateCourseInformation(id, uid);
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
     * Populates the course information fields.
     * @param id The id of the course.
     */
    private void populateCourseInformation(final int id, final String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("Courses/" + id);
        userRef = db.getReference().child("Users/"+uid);
        listingsRef = db.getReference().child("Listings");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
               // Log.d("USER_QUERY", u.Identifier.toString());
                courses_registered = u.Courses_Registered;
                //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
                id_string = Integer.toString(id);
                if (courses_registered.containsKey(id_string)) {
                    //registrationPossibility.setText("You already registered, you can drop");
                   // btnRegistration.setEnabled(false);
                   // btnDropCourse.setEnabled(true);
                }
                else {
                    //registrationPossibility.setText("You can register, you cannot drop");
                    // btnRegistration.setEnabled(true);
                    // btnDropCourse.setEnabled(false);
                }

                for (String key : courses_registered.keySet()) {
                    Log.d("COURSES_REGISTERED",key);
                    Log.d("COURSES_REGISTERED",courses_registered.get(key));
                }


               // Log.d("USER_QUERY",u.UID.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course c = dataSnapshot.getValue(Course.class);
                courseCodeView.setText(c.Course_Code);
                courseNameView.setText(c.Name);
                courseSemesterView.setText(c.Semester);
//                courseKey.setText(c.Key);

                if (c.Cross_Listing.equals("")) {
                    courseCrossListView.setText("N/A");
                } else {
                    courseCrossListView.setText(c.Cross_Listing);
                }

                setTitle(c.Course_Code);
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
