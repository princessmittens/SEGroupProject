package com.example.achristians.gpproject;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MenuTest {

    @Rule
    public ActivityTestRule<Menu> testRule = new ActivityTestRule<>(Menu.class);

    @Before
    public void init(){
        User.MockUser();
    }

    //Test 1: check if the menu exists
    @Test
    public void menuExists() {
        onView(withId(R.id.navigateCourseDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.navListView)).check(matches(isDisplayed()));
    }

    //Test 2: Click menu button navListView
    @Test
    public void clickCourseDescription() {
        onView(withText("Course Description")).perform(click());
        pressBack();
    }

    //Test 3: Click menu button navigateCourseDescription
    @Test
    public void clickCourseList() {
        onView(withText("Course List")).perform(click());
        pressBack();
    }

    //Test 4: Both buttons
    @Test
    public void clickBothButtons() {
        onView(withText("Course Description")).perform(click());
        pressBack();
        onView(withText("Course List")).perform(click());
        pressBack();
    }
}
