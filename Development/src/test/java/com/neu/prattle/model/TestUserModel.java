package com.neu.prattle.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestUserModel {

    private User matt;
    private User ben;

    @Before
    public void init(){
        matt = new User("matt", "1234");
        ben = new User("ben","1234");
    }

    @Test
    public void testGetName(){
        assertEquals("matt", matt.getName());
    }

    @Test
    public void testSetName(){
        ben.setName("benSet");
        assertEquals("benSet", ben.getName());
    }

    @Test
    public void testGetPassword(){
        assertEquals("1234", ben.getPassword());
    }

    @Test
    public void testSetPassword(){
        ben.setPassword("set1234");
        assertEquals("set1234", ben.getPassword());
    }

    @Test
    public void testSetGroups(){
        Set<String> g = new HashSet<>();
        g.add("group1");
        matt.setGroups(g);
        assertEquals(g, matt.getGroups());
    }
}
