package com.example.achristians.gpproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.ActionBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;


public class Menu extends AppCompatActivity{

    private TextView navListView, myCourses, timetable;
    public static String courseIDstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        navListView = findViewById(R.id.navListView);
        myCourses = findViewById(R.id.myCoursesButton);
        timetable = findViewById(R.id.timetableButton);
        courseIDstring="Course";


        navListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CourseList.class);
                startActivity(intent);
            }
        });

        myCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyCourses.class);
                startActivity(intent);
            }
        });
        //hide back arrow in toolbar, given the logout option in top right corner
        ActionBar action = getActionBar();
        if(getClass()==Menu.class&&action!=null)
            action.setDisplayHomeAsUpEnabled(false);

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WeeklySchedule.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            //toolbar back arrow functionality
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        User.setUser(null);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed(){
        logout();
        super.onBackPressed();
    }

    public void childBackPressed(){
        super.onBackPressed();
    }
    }
