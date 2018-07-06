package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Displays information on a course.
 */
public class courseDetails extends Menu {

    private TextView courseNameView;
    private TextView userNameView;
    private TextView courseSemesterView;
    private TextView courseCrossListView;
    private TextView registrationPossibility;
    private TextView courseDescription;
    private Button btnRegistration;
    private Button btnDropCourse;
    private FirebaseAuth auth;
    private HashMap<String, String> courses_registered;
    private String courseIDInfo="";
    private String  id_string="";
    private DatabaseReference userRef;
    private User u;

    private ArrayList<Listing> listings;
    private Course inputCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        Intent intent = getIntent();
        inputCourse = (Course) intent.getSerializableExtra("Course");
        listings = (ArrayList<Listing>) intent.getSerializableExtra("Listings");

        u = User.getUser();

        userRef = firebaseDB.dbInterface.getRootDataReference().child("Users/" + u.getCurrent_UID());

        courseNameView = findViewById(R.id.courseNameView);
        courseSemesterView = findViewById(R.id.courseSemesterView);
        courseCrossListView = findViewById(R.id.courseCrossListView);
        courseDescription = findViewById(R.id.courseDescriptionView);
        btnRegistration = findViewById(R.id.registerButton);
        userNameView = findViewById(R.id.userNameView);
        registrationPossibility = findViewById(R.id.registrationView);
        btnDropCourse = findViewById(R.id.dropCourseButton);

        userNameView.setText(User.getUser().getCurrent_Identifier());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Here we need to register user
                u.getRegistered().put(id_string,new Date().toString());
                userRef.setValue(u);
                btnRegistration.setEnabled(false);
                btnDropCourse.setEnabled(true);
                registrationPossibility.setText("You registered successfully!");
            }
        });

        populateFromCourse(inputCourse);

        //Drop course button
        btnDropCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we need to register user
                u.getRegistered().remove(id_string);
                //Update the database to reflect dropping the course

                userRef.setValue(u);
                btnRegistration.setEnabled(true);
                btnDropCourse.setEnabled(false);
                registrationPossibility.setText("You dropped the course successfully!");
            }
        });

        auth = FirebaseAuth.getInstance();
    }

    /**
     * Populates text fields with course information based on an inputted course
     * @param c The course to populate based on
     */
    private void populateFromCourse(Course c){
        courseNameView.setText(c.Name);
        courseDescription.setText(c.Description);
        courseSemesterView.setText(c.Semester);

        courses_registered = u.getRegistered();

        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap

        id_string = Long.toString(listings.get(0).CRN);

        if (courses_registered.containsKey(id_string)) {
            registrationPossibility.setText("You already registered, you can drop");
            btnRegistration.setEnabled(false);
            btnDropCourse.setEnabled(true);
        }
        else {
            registrationPossibility.setText("You can register, you cannot drop");
            btnRegistration.setEnabled(true);
            btnDropCourse.setEnabled(false);
        }

        for (String key : courses_registered.keySet()) {
            Log.d("COURSES_REGISTERED",key);
            Log.d("COURSES_REGISTERED",courses_registered.get(key));
        }

    }//End populateFromCourse
}//End Class
