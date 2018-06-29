package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AChristians on 2018-05-14.
 */

public class registration extends AppCompatActivity {

    //ArrayList<String> filteredCourses = new ArrayList<String>();
    //Full list of courses from DB
    ArrayList<Course> courseList = new ArrayList<>();
    ArrayAdapter<Course> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        final ListView courseListView = findViewById(R.id.courseListView);

        Database.dbInterface = new Database(getApplicationContext());
        fetchCourses();

        //Populate dropdown menu
        final Spinner dropdown = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dropdownAdapter);

        //Add data to courseListView
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseList);
        courseListView.setAdapter(arrayAdapter);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Navigate to course information activity when user story #1 complete
                Intent intent = new Intent(registration.this, courseDetails.class);
                intent.putExtra("Course", courseList.get(position));
                startActivity(intent);
            }
        });

//        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //Get selected text and scream it into the void
//                String selected = parent.getItemAtPosition(position).toString();
//                Log.i("Filter selected: ", selected);
//                filteredCourses = sortList(dropdown, demoCourses);
//                //MARLEE: testing
//                for(int i= 0; i<filteredCourses.size();i++){
//                    Log.i("Item ", filteredCourses.get(i));
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.i("BEEP BOOP ", "BEEEEEP");
//            }
//        });
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
        courseList.clear();
        courseList.addAll(courseListNew);
        arrayAdapter.notifyDataSetChanged();
        Log.i("Test", courseList.size()+"");
    }

    //Sort list when dropdown category clicked
    public static ArrayList<String> sortList(Spinner dropdown, ArrayList<String> original) {
        //Create new ArrayList filteredCourses
        ArrayList<String> filteredCourses = new ArrayList<String>();
        //Get item in spinner that was selected
        String selected = dropdown.getSelectedItem().toString();

        //For each item in demoCourses, get first 4 chars
        //Should we make demoCourses a singleton???
        for(int i=0; i<original.size(); i++) {
            String compareStr = original.get(i).substring(0, 3);
            if (compareStr.equalsIgnoreCase(selected)) {
                filteredCourses.add(original.get(i));
            }
        }
        //Reload listView using fiteredCourses as a data source
        return filteredCourses;
    }
}

