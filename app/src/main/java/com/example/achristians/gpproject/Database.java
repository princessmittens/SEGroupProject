package com.example.achristians.gpproject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    }

}
