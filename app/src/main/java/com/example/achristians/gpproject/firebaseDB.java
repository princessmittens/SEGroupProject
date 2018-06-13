package com.example.achristians.gpproject;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class firebaseDB {

    private static FirebaseDatabase rootDataSource;
    private static DatabaseReference rootDataReference;

    private static DatabaseReference testDataReference;
    private static DatabaseReference usersDataReference;

    private static String result;

    public static firebaseDB dbInterface;

    //Runs test queries
    public static void testQueries(){
        dbInterface.addTestListener();
        dbInterface.setTestObject();
        dbInterface.getUserSpec();
        dbInterface.setUserTest();

        User test = User.SpecUser;
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
                    result = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    result = databaseError.toString();
                }
            }
        );
    }

    //Gets the spec user from the database
    public void getUserSpec(){
        usersDataReference = rootDataReference.child("Users");
        DatabaseReference specUserDR = usersDataReference.child("Spec");

        specUserDR.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User.setSpecUser(dataSnapshot.getValue(User.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    result = databaseError.toString();
                }
            }
        );
    }

    //Sets a test user in the database
    public void setUserTest(){
        usersDataReference = rootDataReference.child("Users");
        DatabaseReference testUserDR = usersDataReference.child("Test");

        HashMap<String, Long> completed = new HashMap<>();
        completed.put("CRN", (long)1);
        HashMap<String, Long> registered = new HashMap<>();
        registered.put("CRN", (long)2);

        User testUser = new User("Test", Calendar.getInstance().getTime().toString(), completed, registered);
        testUserDR.setValue(testUser);
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
        testDataReference.setValue("New Value");
    }
}
