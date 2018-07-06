package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;


//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class Listing implements Serializable{

    public long CRN;
    public String Key;
    public long Credit_Hours;
    public String Format;
    public String Instructor;
    public String Max_Enrollment;
    public String Location;
    public String Days;
    public String Time;

    public Listing(long crn, String key, long credit_Hours, String format, String instructor,
                   String max_Enrollment, String location, String days, String time) {
        CRN = crn;
        Key = key;
        Credit_Hours = credit_Hours;
        Format = format;
        Instructor = instructor;
        Max_Enrollment = max_Enrollment;
        Location = location;
        Days = days;
        Time = time;
    }

     public Listing(){
     }
}
