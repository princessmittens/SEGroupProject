package com.example.achristians.gpproject;

import android.util.Pair;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;


//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class Listing implements Serializable{

    public static Listing exampleListing = new Listing(99999, "TEST_COURSE_KEY", 3, "lec", "Doe, John", 0,
                                                                           "99", "Goldberg 127", "MWF", "1135-1225");

    public long CRN;
    public String Key;
    public long Credit_Hours;
    public String Format;
    public String Instructor;
    private long Current_Enrollment;
    public String Max_Enrollment;
    public String Location;
    public String Days;
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
    }

    private int StartMinute;
    private int EndMinute;

    private ArrayList<Pair<Integer,Integer>> startEndPairs;

    /**
     * Checks whether the time of this course conflicts with the time of another course
     * @param l The course to check conflicts against
     * @return Whether a conflict exists
     */
    public boolean checkConflict(Listing l){
        if(Days == null || l == null || Time == null || l.Time == null || Time.compareTo("C/D") == 0 || l.Time.compareTo("C/D") == 0){
            return false;
        }

        for(int i=0; i<Days.length(); i++){
            if(l.Days.indexOf(Days.charAt(i)) != -1){
                if((Integer.parseInt(Time.substring(0,4)) > Integer.parseInt(l.Time.substring(0,4)) &&  //Is MY start time within YOUR range
                     Integer.parseInt(Time.substring(0,4)) < Integer.parseInt(l.Time.substring(5,9))) ||
                    (Integer.parseInt(Time.substring(5,9)) > Integer.parseInt(l.Time.substring(0,4)) &&  //Or is MY end time within YOUR range
                     Integer.parseInt(Time.substring(5,9)) < Integer.parseInt(l.Time.substring(5,9)))){
                    return true; //There is a time conflict
                }
            }
        }

        return false;
    }

    public void setCurrent_Enrollment(long current_Enrollment) {
        Current_Enrollment = current_Enrollment;
    }

    public long getCurrent_Enrollment(){
        return Current_Enrollment;
    }

}
