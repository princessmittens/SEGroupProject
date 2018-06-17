package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Listing {

    public int CRN;
    public String Key;
    public int Credit_Hours;
    public String Format;
    public String Instructor;
    public int Max_Enrollment;
    public String Location;
    public String Days;
    public String Time;

    public Listing(int CRN, String key, int credit_Hours, String format, String instructor,
                   int max_Enrollment, String location, String days, String time) {
        this.CRN = CRN;
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
