package com.example.achristians.gpproject;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;

@IgnoreExtraProperties
public class User {

    public static void MockUser(){
        HashMap<String,String> mockRegistered = new HashMap<>();
        mockRegistered.put("CSCI 1105 WINTER (1) : 07-JAN-2019 - 08-APR-2019", "20615");
        mockRegistered.put("CSCI 1110 WINTER (1) : 07-JAN-2019 - 08-APR-2019", "20629");
        loggedIn = new User("TestUser",new HashMap<String, String>(), mockRegistered);
        loggedIn.UID = "UserIdentifier";
    }
    private String UID;
    private String Identifier;
    private HashMap<String, String> Completed;
    private HashMap<String, String> Registered;
    private static User loggedIn;

    /**
     * Firebase RealtimeDatabase serializes and deserializes this class
     * to/from dataSnapshots, so a non-argumented constructor is required.
     */
    public User(){  }

    public User(String Identifier, HashMap<String, String> Courses_Completed,
                HashMap<String, String> registered){
        this.setIdentifier(Identifier);
        this.setCompleted(Courses_Completed);
        this.setRegistered(registered);
    }

    /** Deletes the current user's data. */
    public static void deleteUserInfo(){
        if(loggedIn != null && loggedIn.UID != null && loggedIn.UID.compareTo("") != 0) {
            Firebase.getRootDataReference().child("Users")
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
     * Check if a listing conflicts with the courses a user is registered in.
     * @param l The listing to check against.
     * @return
     */
    public boolean checkConflict(Listing l) {
        boolean listingFound;

        for (String value : getRegistered().values()) {
            //Listing found needs to be reset every loop
            listingFound = false;
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

    /**
     * Check if a listing is at full capacity
     * @param l The listing to check the number of students enrolled in
     * @return
     */
    public boolean checkMax(Listing l) {

        //Return whether the current and max enrollments are equal
        if (String.valueOf(l.Current_Enrollment).equals(l.Max_Enrollment))
            return true;
        else
            return false;
    }
}
