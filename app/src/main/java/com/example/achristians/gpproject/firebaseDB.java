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

    static FirebaseDatabase dataSource;
    static DatabaseReference dataReference;

    private static String outputString;

    public firebaseDB(Context appContext){
        FirebaseApp.initializeApp(appContext);
    }

    public String getObject(String DB_path){
        dataSource = FirebaseDatabase.getInstance();
        dataReference = dataSource.getReference(DB_path);

        dataReference.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    outputString = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    outputString = databaseError.toString();
                }
            }
        );

        String output = outputString;
        outputString = null;
        return output;
    }
}
