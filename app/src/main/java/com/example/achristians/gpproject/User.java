package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;

@IgnoreExtraProperties
public class User {

    private String UID;
    private String Identifier;
    private HashMap<String, String> Completed;
    private HashMap<String, String> Registered;
    private static User loggedIn;

    public User(){
        //Default no-args constructor is required for firebase RT DB usage.
    }

    public User(String Identifier, HashMap<String, String> Courses_Completed,
                HashMap<String, String> registered){
        this.setIdentifier(Identifier);
        this.setCompleted(Courses_Completed);
        this.setRegistered(registered);
    }

    /** Deletes the current user's data. */
    public static void deleteUserInfo(){
        if(loggedIn != null && loggedIn.UID != null && loggedIn.UID.compareTo("") != 0) {
            firebaseDB.dbInterface.getRootDataReference().child("Users")
                    .child(User.getUser().UID).removeValue();
        }
    }

    /**
     * Gets the user. Singleton.
     * @return The current user.
     */
    public static User getUser(){
        if (loggedIn == null){
            loggedIn = new User("", new HashMap<String, String>(),
                    new HashMap<String, String>());
            loggedIn.UID = "";
        }
        return loggedIn;
    }

    /**
     * Sets the current logged in user.
     * @param u The new user.
     */
    public static void setUser(User u){
        loggedIn = u;
    }

    /**
     * Sets the user ID.
     * @param uid The new user ID.
     */
    public void setUID(String uid){
        UID = uid;
    }

    /**
     * Gets the user ID.
     * @return The current user's ID.
     */
    public String getUID(){
        return UID;
    }

    /**
     * Sets the identifier.
     * @param identifier The new indentifier.
     */
    public void setIdentifier(String identifier){
        Identifier = identifier;
    }

    /**
     * Gets the identifier.
     * @return Gets the identifier.
     */
    public String getIdentifier(){
        return Identifier;
    }

    /**
     * Gets the user's completed courses.
     * @return The HashMap of completed courses.
     */
    public HashMap<String, String> getCompleted() {
        if(Completed == null){
            Completed = new HashMap<>();
        }

        return Completed;
    }

    /**
     * Sets the user's HashMap of completed courses.
     * @param completed The new HashMap of completed courses.
     */
    public void setCompleted(HashMap<String, String> completed) {
        Completed = completed;
    }

    /**
     * Gets the user's HashMap of registered courses.
     * @return The HashMap containing courses the user is registered in.
     */
    public HashMap<String, String> getRegistered() {
        if(Registered == null){
            Registered = new HashMap<>();
        }

        return Registered;
    }

    /**
     * Sets the user's HashMap of registered courses.
     * @param registered Sets the user's HashMap of registered courses.
     */
    public void setRegistered(HashMap<String, String> registered) {
        Registered = registered;
    }

    /**
     * Adds an event to a user's calendar, on the condition that there are no scheduling conflicts.
     * @param l The listing to add.
     * @return Whether the course was added successfully
     */
    public boolean addEvent(Listing l){
        return false;
    }

    /**
     * Adds an event to a user's calendar, irrelevant to whether there are scheduling conflicts
     * @param l The listing to add.
     */
    public void addEventOverride(Listing l){
        /* Stub */
    }

    /**
     * Check if a listing conflicts with the courses a user is registered in.
     * @param l The listing to check against.
     * @return
     */
    public boolean checkConflict(Listing l) {
        boolean listingFound = false;

        for (String value : getRegistered().values()) {
            for (int i = 0; i < Listing.listings.size() && !listingFound; i++) {
                if (Listing.listings.get(i).CRN == Long.parseLong(value)) {
                   listingFound = true;

                   if (Listing.listings.get(i).checkConflict(l)) {
                       return true;
                   }
                }
            }
        }

        return false;
    }

    /* For testing. */
    public static void MockUser(){
        loggedIn = new User("TestUser",new HashMap<String, String>(),
                new HashMap<String, String>());
        loggedIn.UID = "UserIdentifier";
    }
}
