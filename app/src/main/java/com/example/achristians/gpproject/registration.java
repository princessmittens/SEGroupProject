package com.example.achristians.gpproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by AChristians on 2018-05-14.
 */

public class registration extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener mAuth;

    firebase fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
    }
}

