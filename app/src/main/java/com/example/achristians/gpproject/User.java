package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;


//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class User {

    public static User SpecUser;

    public static void setSpecUser(User input){
        SpecUser = input;
    }

    public User(){
        //Default no-args constructor is required for firebase RT DB usage.
    }

    public User(String Identifier, String UID, HashMap<String,Long> Courses_Completed, HashMap<String,Long> Courses_Registered){
        this.Identifier = Identifier;
        this.UID = UID;
        this.Courses_Completed = Courses_Completed;
        this.Courses_Registered = Courses_Registered;
    }

    public String Identifier;
    public String UID;

    public HashMap<String,Long> Courses_Completed;
    public HashMap<String,Long> Courses_Registered;
}
