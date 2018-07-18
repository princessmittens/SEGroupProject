package com.example.achristians.gpproject;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.assertTrue;

/* Unit tests for checking course conflicts. */
public class ListingTest {

    @Test
    public void testCheckConflict() {
        Listing l1 = new Listing(99999, "TEST_COURSE_KEY", 3, "lec",
                "Doe, John", 0,  "99", "Goldberg 127",
                "MWF", "1135-1225");
        Listing l2 = new Listing(99999, "TEST_COURSE_KEY", 3, "lec",
                "Doe, John", 0,  "99", "Goldberg 127",
                "MWF", "1135-1225");

        /* Two courses running simultaneously. */
        assertTrue(l1.checkConflict(l1));

        /* Two courses non-overlapping. */
        l1.Time = "C/D";
        l2.Time = "C/D";
        assertTrue(!l1.checkConflict(l1));

        /* Two courses overlapping. */
        l1.Time = "1035-1225";
        l2.Time = "1135-1225";
        assertTrue(l1.checkConflict(l2));
    }
}
