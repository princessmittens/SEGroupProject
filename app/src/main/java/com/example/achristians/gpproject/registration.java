package com.example.achristians.gpproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by AChristians on 2018-05-14.
 */

public class registration extends AppCompatActivity {

    ArrayList<String> filteredCourses = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        final ListView courseListView = findViewById(R.id.courseListView);

        //Populate dropdown menu
        final Spinner dropdown = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dropdownAdapter);

        //Generate sample data
        final ArrayList<String> demoCourses = new ArrayList<String>();
        demoCourses.add("CSCI 1100");
        demoCourses.add("CSCI 2110");
        demoCourses.add("CSCI 3130");
        demoCourses.add("CSCI 3132");
        demoCourses.add("CSCI 2121");
        demoCourses.add("CSCI 3120");
        demoCourses.add("CSCI 1107");
        demoCourses.add("MATH 1000");
        demoCourses.add("MATH 1010");
        demoCourses.add("MATH 2030");

        //Add sample data to courseListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demoCourses);
        courseListView.setAdapter(arrayAdapter);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Navigate to course information activity when user story #1 complete
                Log.i("Course selected: ", demoCourses.get(position));
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get selected text and scream it into the void
                String selected = parent.getItemAtPosition(position).toString();
                Log.i("Filter selected: ", selected);
                filteredCourses = sortList(dropdown, demoCourses);
                //MARLEE: testing
                for(int i= 0; i<filteredCourses.size();i++){
                    Log.i("Item ", filteredCourses.get(i));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("BEEP BOOP ", "BEEEEEP");
            }
        });
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

