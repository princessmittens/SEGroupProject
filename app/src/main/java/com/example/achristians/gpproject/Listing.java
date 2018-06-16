package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;


//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class Listing {
    public Listing(){}

    public Listing(String CRN, String key, long credit_Hours, String format, String instructor, long max_Enrollment, String location, String days, String time) {
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

    public String CRN;
    public String Key;

    public long Credit_Hours;
    public String Format;
    public String Instructor;
    public long Max_Enrollment;

    public String Location;
    public String Days;
    public String Time;
}
