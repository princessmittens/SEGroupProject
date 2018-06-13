package com.example.achristians.gpproject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class firebaseDB {

    private static FirebaseDatabase rootDataSource;
    private static DatabaseReference rootDataReference;

    private static DatabaseReference testDataReference;
    private static DatabaseReference usersDataReference;

    private static String result;

    public firebaseDB(Context appContext){
        FirebaseApp.initializeApp(appContext);
        rootDataSource = FirebaseDatabase.getInstance();
        rootDataReference = rootDataSource.getReference();
    }

    public void addObjectListener(String DB_path){
        testDataReference = rootDataReference.child(DB_path);

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

    public void setTestObject(){
        testDataReference = rootDataReference.child("Test");
        testDataReference.setValue("New Value");
    }
}
