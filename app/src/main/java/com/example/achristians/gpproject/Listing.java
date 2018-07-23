package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents an individual course listing as stored on the database.
 * This is an instance of a course component (Lecture, Lab, Tutorial, etc),
 * associated with a CRN number, as pulled from the Dalhousie Academic Timetable
 */
@IgnoreExtraProperties
public class Listing implements Serializable{

    public static Listing exampleListing = new Listing(99999, "TEST_COURSE_KEY", 3, "lec", "Doe, John", 0,
                                                                           "99", "Goldberg 127", "MWF", "1135-1225");

    /**
     * Example list of listings, used for testing
     * @return
     */
    public static ArrayList<Listing> exampleList(){
        ArrayList<Listing> output = new ArrayList<>();
        output.add(exampleListing);
        return output;
    }

    //Listing Properties
    //
    //The key is a string that matches this listing to a single course,
    //in a single semester.
    public String Key;
    public String Format;
    public String Instructor;
    public long Current_Enrollment;
    public String Max_Enrollment;
    public String Location;
    public String Days;
    public String Time;
    public long Credit_Hours;
    public long CRN;

    /**
     * Argumented Constructor, no real complicated stuff here
     * @param crn Listing CRN (Unique)
     * @param key Listing Key (Maps to a Course)
     * @param credit_Hours How many credit hours the Listing is worth
     * @param format Lecture, Lab, Tutorial, Etc.
     * @param instructor Teacher or TA
     * @param max_Enrollment Maximum number of enrollable students
     * @param location Where this listing takes place (Can be empty, see Thesis)
     * @param days Which days this takes place on
     * @param time What time this takes place at
     */
    public Listing(long crn, String key, long credit_Hours, String format, String instructor,
                   long current_Enrollment, String max_Enrollment, String location, String days, String time) {
        CRN = crn;
        Key = key;
        Credit_Hours = credit_Hours;
        Format = format;
        Instructor = instructor;
        Current_Enrollment = current_Enrollment;
        Max_Enrollment = max_Enrollment;
        Location = location;
        Days = days;
        Time = time;
    }

    /**
     * Firebase RealtimeDatabase serializes and deserializes this class
     * to/from dataSnapshots, so a non-argumented constructor is required.
     */
     public Listing(){  }
}
