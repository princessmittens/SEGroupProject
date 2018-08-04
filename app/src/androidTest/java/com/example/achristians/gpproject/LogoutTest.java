package com.example.achristians.gpproject;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogoutTest {

    @Rule
    public ActivityTestRule<Menu> testRule = new ActivityTestRule<>(Menu.class);

    //Test 1: check if the menu exists
    @Test
    public void menuExists() {
        openContextualActionModeOverflowMenu();
        onView(withText("Logout")).check(matches(isDisplayed()));
    }

    //Test 2: Click logout button
    @Test
    public void clickLogout() {
        openContextualActionModeOverflowMenu();
        onView(withText("Logout")).perform(click());
    }
}
