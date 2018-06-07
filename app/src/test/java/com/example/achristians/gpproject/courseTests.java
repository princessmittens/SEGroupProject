package com.example.achristians.gpproject;

import org.junit.Test;

public class CourseTests {


    //Test 1: The number of cells in the listview is the same as the number of courses in the arraylist
    @Test
    public numCells() {

        assertTrue(courseListView.getAdapter().getCount() == courses.size());
    }


}