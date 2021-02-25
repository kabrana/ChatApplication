package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TestUserDatabase {

    private final UserDatabaseLayer test = UserDatabaseLayerImpl.getInstance();

    @Test
    public void testAdd() {

        User matt = new User("Matt", "bad_password");
        test.addUser(matt);
        assertTrue(test.userExist(matt));
        assertFalse(test.userExist(new User("DNE", "DNE")));
        test.deleteUser(matt);
    }

    @Test
    public void testDeleteUser() {
        User removed = new User("removed", "removedPass");
        test.addUser(removed);
        test.deleteUser(removed);
        assertFalse(test.userExist(removed));
    }

    @Test
    public void testUserExist() {
        assertFalse(test.userExist(new User("DNE", "noPass")));
    }

    @Test
    public void testGetUser() {
        User matt = new User("matt", "something");
        test.addUser(matt);
        assertEquals(matt.getName(), test.getUser("matt").getName());
        test.deleteUser(matt);

    }

    @Test
    public void testUserGroups() {
        User matt = new User("Matt", "crap");
        Set<String> dmGroup = new HashSet<>();
        dmGroup.add(matt.getName());
        Group dm = new Group("Private", dmGroup, true);
        matt.addGroup(dm.getGroupName());
        assertTrue(matt.getGroups().contains(dm.getGroupName()));
        matt.removeGroup(dm.getGroupName());
        assertFalse(matt.getGroups().contains(dm.getGroupName()));
    }
}
