package com.example.achristians.gpproject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class Menu extends AppCompatActivity{

    private Button navCourseDes, navListView;
    public static String courseIDstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        navCourseDes=findViewById(R.id.navigateCourseDescription);
        navListView = findViewById(R.id.navListView);
        courseIDstring="Course";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),registration.class);
                startActivity(intent);
            }
        });

        navCourseDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),courseDetails.class);
                intent.putExtra(courseIDstring, Course.exampleCourse);
                Listing l = Listing.exampleListing;
                ArrayList<Listing> listings = new ArrayList<>();
                listings.add(l);
                intent.putExtra("Listings", listings);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    }
