package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;


//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class User {

    public static User SpecUser;


    private static String current_UID;

    public static String getCurrent_UID() {
        return current_UID;
    }

    public static void setCurrent_UID(String current_UID) {
        if(current_UID == null){
            return;
        }
        User.current_UID = current_UID;
    }

    private static String current_Identifier;

    public static String getCurrent_Identifier() {
        return current_Identifier;
    }

    public static void setCurrent_Identifier(String current_Identifier) {
        if(current_Identifier == null){
            return;
        }
        User.current_Identifier = current_Identifier;
    }



    public static void setSpecUser(User input){
        SpecUser = input;
    }

    public User(){
        //Default no-args constructor is required for firebase RT DB usage.
    }

    public User(String Identifier, HashMap<String, String> Courses_Completed, HashMap<String, String> Courses_Registered){
        this.Identifier = Identifier;
        //this.UID = UID;
        this.Courses_Completed = Courses_Completed;
        this.Courses_Registered = Courses_Registered;
    }

    public String Identifier;
    //public String UID;

    public HashMap<String, String> Courses_Completed;
    public HashMap<String, String> Courses_Registered;
}
