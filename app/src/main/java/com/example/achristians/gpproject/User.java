package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    public User(){
        //Default no-args constructor is required for firebase RT DB usage.
    }

    public User(String Identifier, String UID, List<String> Courses_Completed, List<String> Courses_Registered){
        this.Identifier = Identifier;
        this.UID = UID;
        this.Courses_Completed = Courses_Completed;
        this.Courses_Registered = Courses_Registered;
    }

    public String Identifier;
    public String UID;

    public List<String> Courses_Completed;
    public List<String> Courses_Registered;
}
