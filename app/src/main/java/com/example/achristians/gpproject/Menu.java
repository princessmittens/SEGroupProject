package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


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
                intent.putExtra(courseIDstring,Course.exampleCourse);
                startActivity(intent);
            }
        });

    }

    }
