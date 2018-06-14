package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Course {

    public String Course_Code;
    public String Cross_Listing;
    public String Cross_Listing_URL;
    public String Description_URL;
    public String Key;
    public String Name;
    public String Semester;

    public Course() {

    }

    public Course(String course_Code, String cross_Listing, String cross_Listing_URL,
                  String description_URL, String key, String name, String semester) {
        Course_Code = course_Code;
        Cross_Listing = cross_Listing;
        Cross_Listing_URL = cross_Listing_URL;
        Description_URL = description_URL;
        Key = key;
        Name = name;
        Semester = semester;
    }

    public String toString() {
        return Course_Code + " " + Name;
    }
}
