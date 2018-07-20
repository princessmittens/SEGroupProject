package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.ArrayList;

/** Represents a course in Firebase. */
@IgnoreExtraProperties
public class Course implements Serializable{

    public static ArrayList<Course> courses;

    public static Course exampleCourse = new Course("MATH 1000", "N/A",
            "", "It's a course. It's alive.",
            "MATH 9530 WINTER(2): 07-JAN-2019 - 08-APR-2019", "Calculus 1",
            "Winter", "");

    /** The course code. */
    public String Course_Code;
    /** The cross-listings. */
    public String Cross_Listing;
    /** The cross-listings urls. */
    public String Cross_Listing_URL;
    /** The course description. */
    public String Description;
    public String Key;
    /** The name of the course. */
    public String Name;
    /** The prerequisites of the course. */
    public String Requirements;
    /** The semester the course is held during. */
    public String Semester;

    public Course() {
        /* Empty contructor for Firebase. */
    }

    public Course(String course_Code, String cross_Listing, String cross_Listing_URL,
                  String description, String key, String name, String semester, String requirements) {
        Course_Code = course_Code;
        Cross_Listing = cross_Listing;
        Cross_Listing_URL = cross_Listing_URL;
        Description = description;
        Key = key;
        Name = name;
        Semester = semester;
        Requirements = requirements;
    }

    @Override
    public String toString(){
        return Name;
    }
}
