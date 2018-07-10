package com.example.achristians.gpproject;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
