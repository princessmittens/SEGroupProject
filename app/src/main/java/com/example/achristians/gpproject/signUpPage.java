package com.example.achristians.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 * Created by AChristians on 2018-06-05.
 */

public class signUpPage extends AppCompatActivity {

    TextView Vname, Vemail, Vpass, Vcheckpass;
    EditText Ename, Eemail, Epass, Echeckpass;
    Button regbutton;
    firebase fb;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Ename = findViewById(R.id.Ename);
        Eemail = findViewById(R.id.Eemail);
        Epass = findViewById(R.id.Epass);
        Echeckpass = findViewById(R.id.Echeckpass);

        Vname = findViewById(R.id.Vname);
        Vemail = findViewById(R.id.Vemail);
        Vpass = findViewById(R.id.Vpass);
        Vcheckpass = findViewById(R.id.Vcheckpass);
        regbutton = findViewById(R.id.regbutton);

        fb = new firebase();
        firebaseAuth = fb.firebaseInstance();

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Ename.getText().toString();
                String email = Eemail.getText().toString();
                String pass = Epass.getText().toString();

                String secondPass = Echeckpass.getText().toString();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                boolean checkPass = verifyPassword(secondPass, pass);
                if (checkPass == true) {
                    fb.createUser(signUpPage.this, email, pass);
                }

            }

        });

    }

    private boolean verifyPassword(String secondPass, String pass) {
        if (!secondPass.equals(pass)) {
            Toast.makeText(this, "Passwords do not match!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Passwords must be at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}

