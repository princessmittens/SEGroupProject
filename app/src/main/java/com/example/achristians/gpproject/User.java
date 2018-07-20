package com.example.achristians.gpproject;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;


//Changing this file so Git will decide that my version is up to date

@IgnoreExtraProperties
public class User {

    public static void MockUser(){
        loggedIn = new User("TestUser",new HashMap<String, String>(), new HashMap<String, String>());
        loggedIn.UID = "UserIdentifier";
    }

    public static void deleteUserInfo(){
        if(loggedIn != null && loggedIn.UID != null && loggedIn.UID.compareTo("") != 0) {
            firebaseDB.dbInterface.getRootDataReference().child("Users").child(User.getUser().UID).removeValue();
        }
    }

    public User(){
        //Default no-args constructor is required for firebase RT DB usage.
    }

    public User(String Identifier, HashMap<String, String> Courses_Completed, HashMap<String, String> registered){
        this.setIdentifier(Identifier);
        this.setCompleted(Courses_Completed);
        this.setRegistered(registered);
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

    private String UID;
    private String Identifier;
    private HashMap<String, String> Completed;
    private HashMap<String, String> Registered;

    public void setUID(String uid){
        UID = uid;
    }
    public String getUID(){
        return UID;
    }

    public void setIdentifier(String identifier){
        Identifier = identifier;
    }
    public String getIdentifier(){
        return Identifier;
    }

    public HashMap<String, String> getCompleted() {
        if(Completed == null){
            Completed = new HashMap<>();
        }
        return Completed;
    }

    public void setCompleted(HashMap<String, String> completed) {
        Completed = completed;
    }

    public HashMap<String, String> getRegistered() {
        if(Registered == null){
            Registered = new HashMap<>();
        }
        return Registered;
    }

    public void setRegistered(HashMap<String, String> registered) {
        Registered = registered;
    }

    /**
     * Adds an event to a user's calendar, on the condition that there are no scheduling conflicts
     * @param l
     * @return Whether the course was added successfully
     */
    public boolean addEvent(Listing l){
        return false;
    }

    /**
     * Adds an event to a user's calendar, irrelevant to whether there are scheduling conflicts
     * @param l
     */
    public void addEventOverride(Listing l){

    }

    public boolean checkConflict(Listing l){
        boolean listingFound = false;

        for(String value : getRegistered().values()){
            for(int i=0; i<Listing.listings.size() && !listingFound; i++){
                if(Listing.listings.get(i).CRN == Long.parseLong(value)){
                    listingFound = true;
                    if(Listing.listings.get(i).checkConflict(l)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
