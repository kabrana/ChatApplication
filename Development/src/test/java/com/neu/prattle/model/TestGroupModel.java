package com.neu.prattle.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TestGroupModel {

    private Group g1;
    private Set<String> s;

    @Before
    public void init(){
        s = new HashSet<>();
        s.add("matt");
        s.add("ben");
        g1 = new Group("g1", s, false);
        g1.setGroupID("id1");
    }

    @Test
    public void getGroupID(){
        assertEquals("id1", g1.getGroupID());
    }

    @Test
    public void testGetGroupName(){
        assertEquals("g1", g1.getGroupName());
    }

    @Test
    public void testSetGroupName(){
        g1.setGroupName("g1Set");
        assertEquals("g1Set", g1.getGroupName());
    }

    @Test
    public void testGetGroupList(){
        assertEquals(s, g1.getGroupList());
    }

    @Test
    public void testSetGroupList(){
        s.clear();
        s.add("one");
        assertEquals(s, g1.getGroupList());
    }

    @Test
    public void testGetModList(){
        Set<String> mods = new HashSet<>();
        mods.add("matt");
        assertEquals(mods, g1.getModeratorList());
    }

    @Test
    public void testSetModList(){
        Set<String> mods = new HashSet<>();
        mods.add("set");
        g1.setModeratorList(mods);
        assertEquals(mods, g1.getModeratorList());
    }

    @Test
    public void testGetType(){
        assertFalse(g1.getType());
    }

    @Test
    public void testSetType(){
        g1.setType(true);
        assertTrue(g1.getType());
    }

    @Test
    public void testAddUser(){
        User user = new User("added", "1234");
        g1.addUser(user);
        Set<String> set = new HashSet<>();
        set.add("added");
        set.add("matt");
        set.add("ben");
        assertEquals(set, g1.getGroupList());
    }

    @Test
    public void testRemoveUser(){
        User user = new User("matt", "1234");
        g1.removeUser(user);
        Set<String> set = new HashSet<>();
        set.add("ben");
        assertEquals(set, g1.getGroupList());
    }
}
