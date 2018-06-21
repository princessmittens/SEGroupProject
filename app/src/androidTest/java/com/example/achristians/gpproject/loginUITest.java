package com.example.achristians.gpproject;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class loginUITest {
    private String email,psswd,name;

    @Rule
    public IntentsTestRule<registration> regActivityRule = new IntentsTestRule<>(registration.class, false, false);
    @Rule
    public IntentsTestRule<signUpPage> signupActivityRule = new IntentsTestRule<>(signUpPage.class, false, false);
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class,true,true);



   //check that unregistered user cannot login
    @Test
    public void Test1_unregisteredUserLoginTest(){
        //unrecognized credentials
        email="unregsitered@android.com";
        psswd="loginTest";

        //login page - unrecognized credentials
        onView(withId(R.id.Elogemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Elogpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.loginbutton)).perform(click());

        onView(withText("Authentication failed.")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    //check fail if no email provided at login
    @Test
    public void Test2_noEmailSignInTest(){

        //don't input email
        email="invalid@android.com";
        psswd="";

        onView(withId(R.id.Elogemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Elogpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.loginbutton)).perform(click());
        //check toast error message
        onView(withText("Please enter a valid email or password.")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //check fail if no password provided at login
    @Test
    public void Test3_noPasswordSignInTest(){
        email="";
        psswd="espressoTest";

        onView(withId(R.id.Elogemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Elogpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.loginbutton)).perform(click());
        //check toast error message
        onView(withText("Please enter a valid email or password.")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //check fail if invalid email provided at login
    @Test
    public void Test4_invalidEmailSignInTest(){
        email="email.com";
        psswd="espressoTest";

        onView(withId(R.id.Elogemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Elogpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.loginbutton)).perform(click());
        //check toast error message
        onView(withText("Please enter a valid email or password.")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //check if preexisting user attempts to register, fail
    @Test
    public void Test5_registerExistingUserTest(){
        email="test@test.com";
        psswd="espresso";
        name="EspressoTest";

        //click register
        onView(withId(R.id.createaccount)).perform(click());

        //registration page --> register user, check failure
        onView(withId(R.id.Ename)).perform(typeText(name),closeSoftKeyboard());
        onView(withId(R.id.Eemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Epass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.Echeckpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.regbutton)).perform(click());
        signupActivityRule.launchActivity(null);
        //check failed
        onView(withText("Authentication failed.")).inRoot(withDecorView(not(is(signupActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    //check new user registration is success
    @Test
    public void Test6_registerUserTest(){
        email="espressoTest@android.com";
        psswd="espresso";
        name="EspressoTest";

        //click register
        onView(withId(R.id.createaccount)).perform(click());

        //registration page --> register user, check success
        onView(withId(R.id.Ename)).perform(typeText(name),closeSoftKeyboard());
        onView(withId(R.id.Eemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Epass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.Echeckpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.regbutton)).perform(click());
        //check successful
        regActivityRule.launchActivity(null);
        onView(withText("Authentication success.")).inRoot(withDecorView(not(is(regActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }


}
