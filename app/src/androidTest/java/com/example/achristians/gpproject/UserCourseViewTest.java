package com.example.achristians.gpproject;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.CoreMatchers.anything;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserCourseViewTest {
    @Rule
    public ActivityTestRule<MyCourses> testRule;

    private MyCourses myCourseActivity;
    private final ArrayList<Course> utilityCourseList = new ArrayList<>();

    @Before
    public void setupForTests() throws Throwable {
        testRule = new ActivityTestRule<>(MyCourses.class);

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Firebase.initializeFirebase(context);

        testRule.launchActivity(new Intent());

        myCourseActivity = testRule.getActivity();

        utilityCourseList.clear();
        utilityCourseList.add(Course.exampleCourse);

        User.MockUser();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myCourseActivity.courseChangeHandler(utilityCourseList);
            }
        });

    }

    //Test 1: Listen to make sure a cell in the listview is clickable
    @Test
    public void cellClicks() {
        onData(anything()).inAdapterView(withId(R.id.myCourseListView)).atPosition(0).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void cellClickOpensDescription() {
        onData(anything()).inAdapterView(withId(R.id.myCourseListView)).atPosition(0).perform(click());
        onView(withId(R.id.courseNameView)).check(matches(withText("Calculus 1")));
        Espresso.pressBack();
    }

}
