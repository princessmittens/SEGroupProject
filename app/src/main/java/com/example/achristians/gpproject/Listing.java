package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.ArrayList;

/** Represents a course listing in Firebase. */
@IgnoreExtraProperties
public class Listing implements Serializable {

    public static ArrayList<Listing> listings;

    public static Listing exampleListing = new Listing(99999, "TEST_COURSE_KEY", 3, "lec", "Doe, John", 0,
                                                                           "99", "Goldberg 127", "MWF", "1705-1755");

    /** The course CRN. */
    public long CRN;
    public String Key;
    /** Course credit hours. */
    public long Credit_Hours;
    /** The course format. */
    public String Format;
    /** The course instructor. */
    public String Instructor;
    /** Current number of students enrolled. */
    public long Current_Enrollment;
    /** Max number of students that can enroll. */
    public String Max_Enrollment;
    /** The location of the course. */
    public String Location;
    /** The days the course is held on. */
    public String Days;
    /** The time the course is held at. */
    public String Time;

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

    public Listing(){
        /* Empty constructor for Firebase. */
    }

    /**
     * Checks whether the time of this course conflicts with the time of another course
     * @param l The course to check conflicts against
     * @return Whether a conflict exists
     */
    public boolean checkConflict(Listing l){
        if (Days == null || l == null || Time == null || l.Time == null ||
                Time.compareTo("C/D") == 0 || l.Time.compareTo("C/D") == 0) {
            return false;
        }

        /* Parse start and end times. */
        int thisStart = Integer.parseInt(Time.substring(0, 4));
        int thisEnd = Integer.parseInt(Time.substring(5,9));
        int otherStart = Integer.parseInt(l.Time.substring(0,4));
        int otherEnd = Integer.parseInt(l.Time.substring(5,9));

        for (int i = 0; i < Days.length(); i++){
            if (l.Days.indexOf(Days.charAt(i)) != -1) {
                if (((thisStart >= otherEnd && thisStart <= otherEnd)) ||
                    (thisEnd >= otherStart && thisEnd <= otherEnd)) {
                    return true;
                }
            }
        }

        return false;
    }
}
