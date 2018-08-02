package com.example.achristians.gpproject;

        import android.content.Context;
        import android.os.SystemClock;
        import android.support.test.InstrumentationRegistry;
        import android.support.test.espresso.NoMatchingViewException;
        import android.support.test.espresso.intent.rule.IntentsTestRule;

        import static android.support.test.espresso.Espresso.onData;
        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
        import static android.support.test.espresso.action.ViewActions.scrollTo;
        import static android.support.test.espresso.action.ViewActions.swipeUp;
        import static android.support.test.espresso.action.ViewActions.typeText;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;
        import static android.support.test.espresso.matcher.ViewMatchers.withText;

        import org.junit.After;
        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;

        import java.util.Calendar;
        import static org.hamcrest.CoreMatchers.anything;


public class TrackCourseCapacityTest {
    private String email,psswd,name;

    private static boolean hasCreated = false;

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
        name="coursecapacitytest";
    }

    //test course capacity
    @Test
    public void clickUnclick() throws InterruptedException {

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

        //enter the course details view after logged in
        onView(withText("Course List")).perform(click());

        //wait for firebase to do stuff
        SystemClock.sleep(3000);

        //find course listing for csci 1105
        onData(anything()).inAdapterView(withId(R.id.courseListView)).atPosition(0).perform(click());

        //switch between course times
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());

    }

    //test course capacity
    @Test
    public void clickNewClick() throws InterruptedException {

        //click register
        onView(withId(R.id.createaccount)).perform(click());

        //registration page --> register user, check success
        onView(withId(R.id.Ename)).perform(typeText(name),closeSoftKeyboard());
        onView(withId(R.id.Eemail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.Epass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.Echeckpass)).perform(typeText(psswd),closeSoftKeyboard());
        onView(withId(R.id.regbutton)).perform(click());

        //check activity is now at main menu page
        Thread.sleep(1000);

        //This will fail if the register window is still open
        try{
            onView(withId(R.id.regbutton)).perform(click());
            assert(false);
        }
        catch(NoMatchingViewException e){ }

        //enter the course details view after logged in
        onView(withText("Course List")).perform(click());

        //wait for firebase to do stuff
        SystemClock.sleep(500);

        //find course listing for csci 1105
        onData(anything()).inAdapterView(withId(R.id.courseListView)).atPosition(0).perform(click());

        //switch between course times
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click(), swipeUp());
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(1).perform(click());

    }

    @After
    public void tearDown(){
        mActivityRule.finishActivity();
        User.deleteUserInfo();
    }
}
