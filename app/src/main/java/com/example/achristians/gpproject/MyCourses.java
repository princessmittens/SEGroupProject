package com.example.achristians.gpproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCourses extends AppCompatActivity {
    ArrayList<Course> userCourses = new ArrayList<>();
    ArrayList<Listing> listingList = new ArrayList<>();
    ArrayAdapter<Course> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_courses);

        final ListView listView = findViewById(R.id.myCourseListView);
        Database.dbInterface = new Database(getApplicationContext());
        fetchUserCourses();

        //Add data to courseListView
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userCourses);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyCourses.this, courseDetails.class);

                Course clicked = userCourses.get(position);
                ArrayList<Listing> availableListings = new ArrayList<Listing>();

                for (Listing L : listingList) {
                    if (L.Key.equals(clicked.Key)) {
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
     * Fetches list of courses that the logged in user has registered for
     */
    public void fetchUserCourses(){
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
                        for (DataSnapshot dsListing: dataSnapshots) {
                            listingList.add(dsListing.getValue(Listing.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("Error",databaseError.toString());
                    }
                }
        );
    }

    public void courseChangeHandler(ArrayList<Course> courseListNew){
        //Emptying the course list, as anytime data is changed db side this method will
        //be called, and add all elements to the end of the list
        userCourses.clear();
        userCourses.addAll(courseListNew);
        arrayAdapter.notifyDataSetChanged();
    }
}
