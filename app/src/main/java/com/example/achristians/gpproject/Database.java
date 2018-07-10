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


public class Database {

    public static FirebaseDatabase rootDataSource;
    public static DatabaseReference rootDataReference;
    public static DatabaseReference childDataReference;

    public static String result;

    public static Database dbInterface;

    public Database(Context appContext){
        FirebaseApp.initializeApp(appContext);
        rootDataSource = FirebaseDatabase.getInstance();
        rootDataReference = rootDataSource.getReference();
        Log.i("Check", "DBReference instantiated");
    }

    //Adds a listener to a database path, whose result will be stored in result
    public void addObjectListener(String DB_path){
        childDataReference = rootDataReference.child(DB_path);

        childDataReference.addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
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

}
