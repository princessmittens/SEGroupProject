package com.example.achristians.gpproject;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Andrey on 2018-06-05.
 */

public class WeeklyScheduleTests {

    static String timeString="0835-2100";

    @Test
    public void StartTimeTest() {
      assertEquals(8.0*100+35.0*100/60, WeeklySchedule.StartTime(timeString));
    }

    @Test
    public void EndTimeTest() {
        assertEquals(21.0*100+00.0*100/60, WeeklySchedule.EndTime(timeString));
    }

    @Test
    public void NoStartTimeTest() {
        assertEquals(0, WeeklySchedule.StartTime("12"));
    }

    @Test
    public void NoEndTimeTest() {
        assertEquals(0, WeeklySchedule.EndTime("1235-1"));
    }
}
