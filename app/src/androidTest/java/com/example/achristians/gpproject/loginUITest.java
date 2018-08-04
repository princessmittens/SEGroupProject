package com.example.achristians.gpproject;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class loginUITest {
    private String email,psswd,name;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    //set user input for test
    public void initRegistration() throws Exception{
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Firebase.initializeFirebase(context);

        String timePrefix = "" + Calendar.getInstance().getTimeInMillis();

        email=timePrefix + "@android.com";
        psswd="espresso";
        name="EspressoTest";
    }

    //check that unregistered user cannot login
    @Test
    public void unregisteredUserLoginTest(){
        //login page - unrecognized credentials
        onView(withId(R.id.Elogemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Elogpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.loginbutton)).perform(click());
        onView(withText("Authentication failed.")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //check user registration is success
    @Test
    public void registerUserTest() throws InterruptedException {

        //click register
        onView(withId(R.id.createaccount)).perform(click());

        //registration page --> register user, check success
        onView(withId(R.id.Ename)).perform(typeText(name),closeSoftKeyboard());
        onView(withId(R.id.Eemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Epass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.Echeckpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.regbutton)).perform(click());

        //check activity is now at registration page
        Thread.sleep(2000);

        //This will fail if the update window is still open
        try{
            onView(withId(R.id.regbutton)).perform(click());
            assert(false);
        }
        catch(NoMatchingViewException e){ }
    }

    @After
    public void tearDown(){
        mActivityRule.finishActivity();
        User.deleteUserInfo();
    }
}
