package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by AChristians on 2018-05-14.
 */

public class courseDetails extends AppCompatActivity {

    String courseID="";
    TextView courseIDview;
    TextView courseDescriptionView;

    private Button btnRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        courseIDview = findViewById(R.id.courseID);
        courseDescriptionView = findViewById(R.id.courseDescription);
        btnRegistration = findViewById(R.id.registerButton);


        Intent intent = getIntent();
        courseID = intent.getStringExtra(MainActivity.courseIDstring);
        courseIDview.setText(courseID);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),registration.class);
                // we need to pass course ID here later
                startActivity(intent);
            }
        });

    }
}
