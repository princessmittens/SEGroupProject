package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;


//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class User {

    public static void MockUser(){
        loggedIn = new User("TestUser",new HashMap<String, String>(), new HashMap<String, String>());
        loggedIn.setCurrent_UID("UserIdentifier");
    }

    private static User loggedIn;

    public static User getUser(){
        if (loggedIn == null){
            loggedIn = new User("", new HashMap<String, String>(), new HashMap<String, String>());
            loggedIn.UID = "";
        }
        return loggedIn;
    }
    public static void setUser(User u){ loggedIn = u;}

    /**
     * Firebase RealtimeDatabase serializes and deserializes this class
     * to/from dataSnapshots, so a non-argumented constructor is required.
     */
    public User(){  }

    public User(String Identifier, HashMap<String, String> Courses_Completed, HashMap<String, String> Courses_Registered){
        this.Identifier = Identifier;
        this.Courses_Completed = Courses_Completed;
        this.Courses_Registered = Courses_Registered;
    }


    private String UID;
    private String Identifier;
    private HashMap<String, String> Courses_Completed;
    private HashMap<String, String> Courses_Registered;

    public static HashMap<String, String> getCompleted() { return loggedIn.Courses_Completed; }
    public static void setCourses_Completed(HashMap<String, String> CC){  if(CC != null) loggedIn.Courses_Completed = CC; }

    public HashMap<String, String> getRegistered() { return loggedIn.Courses_Registered; }
    public void setCourses_Registered(HashMap<String, String> CR){ if(CR != null) loggedIn.Courses_Registered = CR;}

    public static String getCurrent_Identifier() {
        return loggedIn.Identifier;
    }
    public static void setCurrent_Identifier(String id){ if(id != null) loggedIn.Identifier = id; }

    public static String getCurrent_UID() { return loggedIn.UID; }
    public static void setCurrent_UID(String uid){ if(uid != null) loggedIn.UID = uid; }

    public static void deleteUserInfo(){
        if(loggedIn != null && loggedIn.UID != null && loggedIn.UID.compareTo("") != 0) {
            Firebase.getRootDataReference().child("Users").child(loggedIn.UID).removeValue();
        }
    }
}
