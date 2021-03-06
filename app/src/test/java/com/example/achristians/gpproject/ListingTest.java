package com.example.achristians.gpproject;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.assertTrue;

/* Unit tests for checking course conflicts. */
public class ListingTest {

    Listing l1, l2, l3, l4;

    @Before
    public void setup(){
        l1 = new Listing(99999, "TEST_COURSE_KEY_0", 3, "lec",
                "Doe, John", 0,  "99", "Goldberg 127",
                "MWF", "1135-1225");
        l2 = new Listing(99999, "TEST_COURSE_KEY_1", 3, "lec",
                "Doe, John", 0,  "99", "Goldberg 127",
                "MWF", "1135-1225");
        //L3 and L4 are the same course, two sections
        //They will not conflict even if they "do", as registering for one
        //means dropping the other
        l3 = new Listing(12345, "TEST_COURSE_KEY_2", 3, "lec",
                "Doe, John", 0,  "99", "Goldberg 127",
                "MW", "1435-1555");
        l4 = new Listing(12346, "TEST_COURSE_KEY_2", 3, "lec",
                "Doe, John", 0,  "99", "Goldberg 127",
                "MW", "1435-1555");
    }

    @Test
    public void testCheckConflict() {


        /* Two courses running simultaneously. */
        assertTrue(l1.checkConflict(l2));

        /* Two courses non-overlapping. */
        l1.Time = "C/D";
        l2.Time = "C/D";
        assertTrue(!l1.checkConflict(l1));

        /* Two courses overlapping. */
        l1.Time = "1035-1225";
        l2.Time = "1135-1225";
        assertTrue(l1.checkConflict(l2));
    }

    @Test
    public void testConflictsUser(){
        //Indicating that the user is registered in L1
        HashMap<String, String> registered = new HashMap<>();
        registered.put(l1.Key, l1.CRN + "");
        User.getUser().setRegistered(registered);

        //Adding the two listings to the list of all listings
        ArrayList<Listing> allListings = new ArrayList<>();
        allListings.add(l1);
        l2.CRN = 12345;
        allListings.add(l2);
        allListings.add(l3);
        allListings.add(l4);
        Listing.listings = allListings;

        assert(User.getUser().checkConflict(l2));
        assert(!User.getUser().checkConflict(l3));

        User.getUser().getRegistered().put(l3.Key, l3.CRN + "");

        //Despite being registered in l3, l4 will not conflict
        assert(!User.getUser().checkConflict(l4));
    }
}
