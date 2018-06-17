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

/**
 * Displays information on a course.
 */
public class courseDetails extends AppCompatActivity {

    private String courseID="";
    private TextView courseCodeView;
    private TextView courseNameView;
    private TextView userNameView;
    private TextView courseSemesterView;
    private TextView courseCrossListView;
    private Button btnRegistration;
    private FirebaseAuth auth;
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

        Intent intent = getIntent();
        courseID = intent.getStringExtra(MainActivity.courseIDstring);
        courseCodeView.setText(courseID);
        id = intent.getIntExtra("id", 0);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),registration.class);
                startActivity(intent);
            }
        });

        populateCourseInformation(id);
    }

    /**
     * Populates the course information fields.
     * @param id The id of the course.
     */
    private void populateCourseInformation(final int id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("Courses/" + id);
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
