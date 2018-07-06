package com.example.achristians.gpproject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

//Changing this file so Git will decide that my version is up to date

public class firebaseDB {

    private static FirebaseDatabase rootDataSource;
    private static DatabaseReference rootDataReference;
    private static DatabaseReference testDataReference;
    private static DatabaseReference usersDataReference;

    public static String result;

    public static firebaseDB dbInterface;

    public DatabaseReference getRootDataReference() {
        return rootDataReference;
    }

    //Runs test queries
    public static void testQueries(){

        /*dbInterface.addTestListener();
        dbInterface.setTestObject();
        dbInterface.getUserSpec();
        dbInterface.setUserTest();*/
    }

    public firebaseDB(Context appContext){
        FirebaseApp.initializeApp(appContext);
        rootDataSource = FirebaseDatabase.getInstance();
        rootDataReference = rootDataSource.getReference();
    }

    //Adds a listener to a database path, whose result will be stored in result
    public void addObjectListener(String DB_path){
        testDataReference = rootDataReference.child(DB_path);

        testDataReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //result = dataSnapshot.getValue(Course.class).Name;
                        Course c = dataSnapshot.getValue(Course.class);
                        Log.i("TEST", c.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result = databaseError.toString();
                    }
                }
        );
    }

    public static void fetchLoggedInUser(){
        usersDataReference = rootDataReference.child("Users/" + User.getUser().getCurrent_UID());
        usersDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if (u == null) {
                    u = User.getUser();
                    u.setCourses_Completed(new HashMap<String, String>());
                    u.setCourses_Registered(new HashMap<String, String>());

                    usersDataReference = rootDataReference.child("Users/" + User.getUser().getCurrent_UID());
                    usersDataReference.setValue(u);
                } else {
                    User.getUser().setCourses_Completed(new HashMap<String, String>());
                    User.getUser().setCourses_Registered(new HashMap<String, String>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Sets a test user in the database
    public void setUserTest(){
        usersDataReference = rootDataReference.child("Users");
        DatabaseReference testUserDR = usersDataReference.child("Test");

        HashMap<String, Long> completed = new HashMap<>();
        completed.put("CRN", (long)1);
        HashMap<String, Long> registered = new HashMap<>();
        registered.put("CRN", (long)2);

        //User testUser = new User("Test", completed, registered);
        // testUserDR.setValue(testUser);
    }

    //Adds a listener to the Test object, fetching it once
    public void addTestListener(){
        testDataReference = rootDataReference.child("Test");

        testDataReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        result = dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result = databaseError.toString();
                    }
                }
        );
    }

    //Sets a single test data point in the database, no template object
    public void setTestObject(){
        testDataReference = rootDataReference.child("Test");
        testDataReference.setValue("Testing...");
    }
}
