package com.example.achristians.gpproject;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListTest {

    //Might need this to run properly?
    @Rule
    public ActivityTestRule<registration> registrationRule =
            new ActivityTestRule(registration.class);


    //Test 1: check if the listview exists
    @Test
    public void listViewExists() {
        onView(withId(R.id.courseListView)).check(matches(isDisplayed()));
    }


    //Test 2: Listen to make sure a cell in the listview is clickable
    @Test
    public void cellClicks() {

        onData(anything()).inAdapterView(withId(R.id.courseListView)).atPosition(0).perform(click());
    }
}