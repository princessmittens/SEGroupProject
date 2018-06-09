package com.example.achristians.gpproject;

import org.junit.Test;

import static org.junit.Assert.*;


public class ExampleUnitTest {
    //@Test
    public void RetrieveData(){
        String output = firebaseDB.getObject("Test");
        assertEquals(output, "Returned Value");
    }
}