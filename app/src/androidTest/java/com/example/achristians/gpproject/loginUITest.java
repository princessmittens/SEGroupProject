package com.example.achristians.gpproject;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import static android.content.Intent.getIntent;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;


public class loginUITest {
    private FirebaseAuth firebaseAuth;
    private firebase fb;
    private String email,psswd,name;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);
//    @Rule
//    public IntentsTestRule<registration> rActivityRule = new IntentsTestRule<>(
//            registration.class);

    @Before
    //set user input for test
    public void initRegistration() throws Exception{
        email="espressoTest@android.com";
        psswd="espresso";
        name="EspressoTest";
    }

//    //check that unregistered user cannot login
//    @Test
//    public void unregisteredUserLoginTest(){
//        //login page - unrecognized credentials
//        onView(withId(R.id.Elogemail)).perform(typeText(email),closeSoftKeyboard());
//        onView(withId(R.id.Elogpass)).perform(typeText(psswd),closeSoftKeyboard());
//        onView(withId(R.id.loginbutton)).perform(click());
//        onView(withText("Authentication failed.")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
//    }

    //check user registration is success
    @Test
    public void registerUserTest(){
//        Intent registerIntent = new Intent();
//        String response = "ok----";
//        registerIntent.putExtra("response",response);
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK,registerIntent);
//
//        intending(toPackage("package com.example.achristians.gpproject;")).respondWith(result);

        //click register
        onView(withId(R.id.createaccount)).perform(click());

        //registration page --> register user, check success
        onView(withId(R.id.Ename)).perform(typeText(name),closeSoftKeyboard());
        onView(withId(R.id.Eemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Epass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.Echeckpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.regbutton)).perform(click());
        //check activity is now at registration page
//        Intents.init();
        intended(hasComponent(registration.class.getName()));


    }

//    @After
//    public void cleanUp () throws Exception{
//        Intents.release();
//    }

}
