package com.example.achristians.gpproject;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// assumes user open to availibility of course with list view
public class MainActivity extends AppCompatActivity {
    TextView Vlogpass, Vlogemail;
    EditText Elogpass, Elogemail;
    Button createaccount, loginbutton;
    public FirebaseAuth firebaseAuth;
    firebase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//         firebaseDB.dbInterface = new firebaseDB(this);
//         firebaseDB.testQueries();
        setContentView(R.layout.login);

        Vlogemail = findViewById(R.id.Vlogemail);
        Vlogpass = findViewById(R.id.Vlogpass);
        Elogpass = findViewById(R.id.Elogpass);
        Elogemail = findViewById(R.id.Elogemail);

        createaccount = findViewById(R.id.createaccount);
        loginbutton = findViewById(R.id.loginbutton);
        fb = new firebase();
        firebaseAuth = fb.firebaseInstance();

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, signUpPage.class);
                startActivity(i);
            }
        });


        final Context context = this;

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Elogemail.getText().toString();
                String pass = Elogpass.getText().toString();
                fb.signIn(MainActivity.this, email, pass);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }
}
