package com.example.achristians.gpproject;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * UI tests for the CourseDetails activity.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class CourseDetailsTest {

    @Rule
    public ActivityTestRule<CourseDetails> activityRule =
            new ActivityTestRule<CourseDetails>(CourseDetails.class,
                    true, false);

    /* Launch the activity. Thread sleeps to allow activity to be created. */
    @Before
    public void init() throws InterruptedException {
        Intent intent = new Intent();
        Course c = Course.exampleCourse;
        Course.courses = new ArrayList<>();
        Course.courses.add(c);

        Listing l = Listing.exampleListing;
        ArrayList<Listing> listings = new ArrayList<>();
        listings.add(l);
        Listing.listings = new ArrayList<>();
        Listing.listings.addAll(listings);

        User.MockUser();

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Firebase.initializeFirebase(context);

        intent.putExtra("Course", c);
        intent.putExtra("Listings", listings);
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

    @Test
    public void AtestRegister(){
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
        assert(User.getUser().getRegistered().containsValue(Listing.exampleListing.CRN));
    }

    @Test
    public void BtestDrop(){
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
        assert(!User.getUser().getRegistered().containsKey(Listing.exampleListing.CRN));
    }

}
