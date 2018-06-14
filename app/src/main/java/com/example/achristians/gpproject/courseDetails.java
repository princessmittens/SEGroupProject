package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by AChristians on 2018-05-14.
 */

public class courseDetails extends AppCompatActivity {

    public static class Course {

        public String Course_Code;
        public String Cross_Listing;
        public String Cross_Listing_URL;
        public String Description_URL;
        public String Key;
        public String Name;
        public String Semester;

        public Course(String Course_Code, String Cross_Listing, String Cross_Listing_URL,
                      String Description_URL,  String Key, String Name, String Semester)
        {
            // ...
        }

    }

    String courseID="";
    TextView courseIDview;
    TextView courseDescriptionView;
    TextView userName;

    private Button btnRegistration;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        courseIDview = findViewById(R.id.courseID);
        courseDescriptionView = findViewById(R.id.courseDescription);
        btnRegistration = findViewById(R.id.registerButton);
        userName=findViewById(R.id.userName);

        if (MainActivity.firebaseAuth.getCurrentUser()==null) userName.setText("Unanimous user");
        else {
            Log.d("USER_INFO", MainActivity.firebaseAuth.getCurrentUser().getEmail().toString());
            userName.setText(MainActivity.firebaseAuth.getCurrentUser().getEmail().toString());
        }

       // DatabaseReference ref = MainActivity


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

        //Here...
        firebaseDB db = new firebaseDB(getApplicationContext());
        db.addObjectListener("Courses/0");
    }
}
