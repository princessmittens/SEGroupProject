package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Course class used to implement Course objects
 */
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

    public static Course exampleCourse = new Course("MATH 1000", "N/A", "", "It's a course. It's alive.", "MATH 9530 WINTER (2) : 07-JAN-2019 - 08-APR-2019", "Calculus 1", "Winter", "");

    /**
     * Empty constructor used for Firebase integration
     */
    public Course() {}

    /**
     * Course object constructor
     *
     * @param course_Code: Course number (ex: CSCI 3130)
     * @param cross_Listing: Cross listings
     * @param cross_Listing_URL: Not currently used on the database (might be changed later)
     * @param description: Course description
     * @param key: Key for Firebase functionality
     * @param name: Course name
     * @param semester: Semester the course is being offered in
     * @param requirements: Listing of prerequisite courses
     */
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

    /**
     * toString method
     * @return: name of the course
     */
    @Override
    public String toString(){
        return Name;
    }
}
