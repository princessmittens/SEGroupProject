package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Displays information on a course.
 */
public class courseDetails extends AppCompatActivity {

    private TextView courseNameView;
    private TextView userNameView;
    private TextView courseSemesterView;
    private TextView courseCrossListView;
    private TextView courseDescription;
    private Button btnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        Intent intent = getIntent();
        Course inputCourse = (Course) intent.getSerializableExtra("Course");

        courseNameView = findViewById(R.id.courseNameView);
        courseSemesterView = findViewById(R.id.courseSemesterView);
        courseCrossListView = findViewById(R.id.courseCrossListView);
        courseDescription = findViewById(R.id.courseDescriptionView);
        btnRegistration = findViewById(R.id.registerButton);
        userNameView = findViewById(R.id.userNameView);

        userNameView.setText(User.getCurrent_Identifier());

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),registration.class);
                startActivity(intent);
            }
        });

        populateFromCourse(inputCourse);
    }

    /**
     * Populates text fields with course information based on an inputted course
     * @param c The course to populate based on
     */
    private void populateFromCourse(Course c){
        courseNameView.setText(c.Name);
        courseDescription.setText(c.Description);
        courseSemesterView.setText(c.Semester);

        if (c.Cross_Listing.equals("")) {
            courseCrossListView.setText("N/A");
        } else {
            courseCrossListView.setText(c.Cross_Listing);
        }

        setTitle(c.Course_Code);
    }
}
