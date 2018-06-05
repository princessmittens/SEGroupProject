package com.example.achristians.gpproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// assumes user open to availibility of course with list view
public class MainActivity extends AppCompatActivity {

    private Button btnNavigateCourseDescription;
    public static String courseIDstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        courseIDstring="CourseID";


        btnNavigateCourseDescription=findViewById(R.id.navigateCourseDescription);

        btnNavigateCourseDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),courseDetails.class);
                intent.putExtra(courseIDstring,"CSCI3130");
                startActivity(intent);
            }
        });


    }
}
