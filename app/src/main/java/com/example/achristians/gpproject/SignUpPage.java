package com.example.achristians.gpproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
/**
 * Created by AChristians on 2018-06-05.
 */

public class SignUpPage extends AppCompatActivity {

    TextView Vname, Vemail, Vpass, Vcheckpass;
    EditText Ename, Eemail, Epass, Echeckpass;
    Button regbutton;

    Activity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Ename = findViewById(R.id.Ename);
        Eemail = findViewById(R.id.Eemail);
        Epass = findViewById(R.id.Epass);
        Echeckpass = findViewById(R.id.Echeckpass);
        regbutton = findViewById(R.id.regbutton);

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Ename.getText().toString();
                String email = Eemail.getText().toString();
                String pass = Epass.getText().toString();
                String secondPass = Echeckpass.getText().toString();
                FirebaseUser user = Firebase.getFirebase().getAuth().getCurrentUser();
                boolean isValidCredentials = checkCred(name, email, pass, secondPass);
                if (isValidCredentials == true) {
                    Firebase.getFirebase().createUser(SignUpPage.this, email, pass, name, thisActivity);
                }
            }

        });

    }

    private boolean checkCred(String name, String email, String pass, String checkpass) {
        if (name.equals("")) {
            Toast.makeText(this, "Please enter a valid name.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.equals("")) {
            Toast.makeText(this, "Please fill in your email.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.equals("")) {
            Toast.makeText(this, "Please enter an empty string",
                    Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Password length is not long enough.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!pass.equals(checkpass)) {
            Toast.makeText(this, "Passwords must match.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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

