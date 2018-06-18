package com.example.achristians.gpproject;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * UI tests for the CourseDetails activity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CourseDetailsTest {

    @Rule
    public ActivityTestRule<courseDetails> activityRule =
            new ActivityTestRule<courseDetails>(courseDetails.class,
                    true, false);

    /* Launch the activity. Thread sleeps to allow activity to be created. */
    @Before
    public void init() throws InterruptedException {
        Intent intent = new Intent();
        intent.putExtra("id", 0);
        activityRule.launchActivity(intent);
        Thread.sleep(2000);
    }

    @Test
    public void testCourseCodeDisplayed() {
        onView(withId(R.id.courseCodeView)).check(matches(withText("CSCI 1105")));
    }

    @Test
    public void testCourseNameDisplayed() {
        onView(withId(R.id.courseNameView)).check(matches(
                withText("CSCI 1105 Intro to Computer Programming")));
    }

    @Test
    public void testSemesterDisplayed() {
        onView(withId(R.id.courseSemesterView)).check(matches(
                withText("FALL (1) : 04-SEP-2018 - 04-DEC-2018")));
    }

    @Test
    public void testCrossListingDisplayed() {
        onView(withId(R.id.courseCrossListView)).check(matches(withText("N/A")));
    }
}
