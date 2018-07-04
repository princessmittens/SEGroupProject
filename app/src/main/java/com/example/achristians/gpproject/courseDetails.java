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

import java.util.Date;
import java.util.HashMap;

/**
 * Displays information on a course.
 */
public class courseDetails extends Menu {

    private String courseID= "";
    private TextView courseCodeView;
    private TextView courseNameView;
    private TextView userNameView;
    private TextView courseSemesterView;
    private TextView courseCrossListView;
    private TextView registrationPossibility;
    private Button btnRegistration;
    private Button btnDropCourse;
    private FirebaseAuth auth;
    private HashMap<String, String> courses_registered;
    private String courseIDInfo="";
    private String  id_string="";
    private DatabaseReference userRef;
    private User u;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        courseCodeView = findViewById(R.id.courseCodeView);
        courseNameView = findViewById(R.id.courseNameView);
        courseSemesterView = findViewById(R.id.courseSemesterView);
        courseCrossListView = findViewById(R.id.courseCrossListView);
        btnRegistration = findViewById(R.id.registerButton);
        userNameView = findViewById(R.id.userNameView);
        registrationPossibility = findViewById(R.id.registrationView);
        btnDropCourse = findViewById(R.id.dropCourseButton);

        Intent intent = getIntent();
        courseID = intent.getStringExtra(Menu.courseIDstring);
        courseCodeView.setText(courseID);
        id = intent.getIntExtra("id", 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Here we need to register user
                u.Courses_Registered.put(id_string,new Date().toString());
                userRef.setValue(u);
                btnRegistration.setEnabled(false);
                btnDropCourse.setEnabled(true);
                registrationPossibility.setText("You registered successfully!");
            }
        });


        //Drop course button
        btnDropCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we need to register user
                u.Courses_Registered.remove(id_string);
                userRef.setValue(u);
                btnRegistration.setEnabled(true);
                btnDropCourse.setEnabled(false);
                registrationPossibility.setText("You dropped the course successfully!");
            }
        });

        auth = FirebaseAuth.getInstance();

        String uid="Not signed id";
        if(auth.getCurrentUser()!=null) {
            uid = auth.getCurrentUser().getUid().toString();
        }

        userNameView.setText(uid);
        populateCourseInformation(id, uid);
    }

    /**
     * Populates the course information fields.
     * @param id The id of the course.
     */
    private void populateCourseInformation(final int id, final String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("Courses/" + id);
        userRef = db.getReference().child("Users/"+uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                Log.d("USER_QUERY", u.Identifier.toString());
                courses_registered = u.Courses_Registered;
                //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
                id_string = Integer.toString(id);
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


               // Log.d("USER_QUERY",u.UID.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course c = dataSnapshot.getValue(Course.class);
                courseCodeView.setText(c.Course_Code);
                courseNameView.setText(c.Name);
                courseSemesterView.setText(c.Semester);

                if (c.Cross_Listing.equals("")) {
                    courseCrossListView.setText("N/A");
                } else {
                    courseCrossListView.setText(c.Cross_Listing);
                }

                setTitle(c.Course_Code);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("FB", "Listener cancelled for Courses");
            }
        });
    }

}
