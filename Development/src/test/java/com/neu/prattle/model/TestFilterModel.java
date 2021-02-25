package com.neu.prattle.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFilterModel {

    private Filter f;

    @Before
    public void init(){
        f = new Filter("test");
    }

    @Test
    public void testGetWord(){
        assertEquals("test", f.getWord());
    }

    @Test
    public void testSetWord(){
        f.setWord("setFilter");
        assertEquals("setFilter", f.getWord());
    }
}
