package com.example.achristians.gpproject;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

// assumes user open to availibility of course with list view
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        firebaseDB dbInterface = new firebaseDB(this);

        dbInterface.addTestListener();
        dbInterface.setTestObject();
        dbInterface.getUserSpec();
    }
}
