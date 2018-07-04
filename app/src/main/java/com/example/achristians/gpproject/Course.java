package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;

//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class Course implements Serializable{

    public String Course_Code;
    public String Cross_Listing;
    public String Cross_Listing_URL;
    public String Description;
    public String Key;
    public String Name;
    public String Requirements;
    public String Semester;

    public static Course exampleCourse = new Course("MATH 1000", "", "", "It's a course. It's alive.", "MATH 1000 FALL 2018-2019W", "Calculus 1", "Winter", "");


    public Course() {}

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
