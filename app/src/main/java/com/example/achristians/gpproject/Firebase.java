package com.example.achristians.gpproject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;


public class Firebase {

    //Singleton instance
    private static Firebase instance;

    /**
     * Gets the singleton Firebase instance, instantiating if need be
     * @return Firebase Singleton
     */
    public static Firebase getFirebase(){
        if(instance == null){
            instance = new Firebase();
        }

        return instance;
    }

    /**
     *Initialize the database connection and references
     */
    public static void initializeFirebase(Context appContext){
        instance = new Firebase();
        FirebaseApp.initializeApp(appContext);
        instance.rootDataSource = FirebaseDatabase.getInstance();
        instance.rootDataReference = instance.rootDataSource.getReference();
    }

    //Database references and Authentication instance
    private static FirebaseDatabase rootDataSource;
    private static DatabaseReference rootDataReference;
    private static DatabaseReference usersDataReference;
    private static DatabaseReference generalDataReference;
    private static FirebaseAuth firebaseAuth;

    public static FirebaseAuth getAuth() {
        return instance.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static DatabaseReference getRootDataReference() {
        return instance.rootDataReference;
    }

    public void signIn(final Context context, String email, String password, Activity callingActivity) {
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(callingActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                            User loggedIn = User.getUser();
                            loggedIn.setUID(user.getUid());
                            loggedIn.setIdentifier(user.getEmail());

                    instance.fetchLoggedInUser();

                    Toast.makeText(context, "Authentication success.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
                }
            }
        );
    }

    /**
     * Currently unused, kept in for posterity
     */
    public void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
    }

    /**
     * Fetches the database information (Registered courses, completed courses)
     * for an authenticated user
     */
    public static void fetchLoggedInUser(){
        usersDataReference = rootDataReference.child("Users").child(User.getUser().getUID());

        usersDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if (u == null || u.getIdentifier() == null) {
                    u = User.getUser();
                }
                User.setUser(u);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Creates a new Authenticated user with email and password
     * @param context App context to run with
     * @param email Email to use for creation
     * @param password Password to use for creation
     * @param name Identified/Username for creation
     * @param callingActivity Activity to run on (Required for onCompleteListeners
     */
    public void createUser(final Context context, final String email, final String password, final String name, final Activity callingActivity) {
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(callingActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        /* While this section of code is not being explicitly used right now,
                        we've left this in for now for testing purposes for iteration 3.
                         */
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference();
                        DatabaseReference usersRef = ref.child("Users/");
                        HashMap<String, String> coursesCompleted = new HashMap<String,String>();
                        HashMap<String, String> coursesRegistered = new HashMap<String,String>();

                        //coursesCompleted.put("100", new Date().toString());
                        //coursesRegistered.put("200", new Date().toString());

                        Map<String, User> users = new HashMap<>();

                        Toast.makeText(context, "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        signIn(context, email, password, callingActivity);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWith Email:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }


    public DataSnapshot storedResult;
    public DatabaseError storedError;

    /**
     * Adds a listener to a database path, whose result will be stored in result
     * Not being explicitly used right now, but keeping it in for testing in iteration 3
     * @param DB_path Path in the database to retrieve
     */
    public void addObjectListener(String DB_path){
        generalDataReference = rootDataReference.child(DB_path);

        generalDataReference.addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    storedResult = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    storedError = databaseError;
                }
            }
        );
    }
}
