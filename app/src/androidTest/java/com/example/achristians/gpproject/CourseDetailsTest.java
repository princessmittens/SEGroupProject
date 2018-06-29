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
        Course c = new Course("MATH 1000", "", "", "It's a course. It's alive.", "MATH 1000 FALL 2018-2019W", "Calculus 1", "Winter", "");
        intent.putExtra("Course", c);
        activityRule.launchActivity(intent);
        Thread.sleep(2000);
    }

    @Test
    public void testCourseNameDisplayed() {
        onView(withId(R.id.courseNameView)).check(matches(
                withText("Calculus 1")));
    }

    @Test
    public void testSemesterDisplayed() {
        onView(withId(R.id.courseSemesterView)).check(matches(
                withText("Winter")));
    }

    @Test
    public void testCrossListingDisplayed() {
        onView(withId(R.id.courseCrossListView)).check(matches(withText("N/A")));
    }
}
