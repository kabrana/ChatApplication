package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupDatabaseImpl;
import com.neu.prattle.service.GroupDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestUserController {

    private final UserDatabaseLayer test = UserDatabaseLayerImpl.getInstance();
    private final GroupDatabaseLayer groupDatabaseLayer = GroupDatabaseImpl.getInstance();
    private final UserController uc = new UserController();
    private static final String GENERAL_ID = "83cb9551-727c-4f3d-ab1d-edd5dc6d7c83";

    private User testUser;
    private Response res;
    private int ok;


    @Before
    public void init() {
        testUser = new User("testUser", "testPassword");
        res = uc.createUserAccount(testUser.getName(), testUser.getPassword());
        ok = Response.ok().build().getStatus();
    }

    @Test
    public void testCreateUser() {
        assertEquals(ok, res.getStatus());
        assertEquals(409,
                uc.createUserAccount(testUser.getName(), testUser.getPassword()).getStatus());
    }

    @Test
    public void testGetGroupList() throws JsonProcessingException {
        List<String> test = new ArrayList<>();
        test.add("\"" + GENERAL_ID + "\"");
        assertEquals(test.toString(), uc.getGroupList(testUser.getName()));
    }

    @Test
    public void testChangeStatus() {
        assertEquals(ok, uc.changeStatus(testUser.getName(), "Offline").getStatus());
    }

    @Test
    public void testLogin() {
        assertEquals("Success", uc.loginAccount(testUser.getName(), testUser.getPassword()));
        assertEquals("Failure", uc.loginAccount(testUser.getName(), "wrongPass"));
        uc.changeStatus(testUser.getName(), "Offline");
    }

    @Test
    public void testUserExist(){
        assertEquals("True",uc.userExist(testUser.getName()));
        assertEquals("False", uc.userExist("testNonExistentUser"));
    }

    @Test
    public void testChangePassword(){
        assertEquals("True", uc.changePassword(testUser.getName(), testUser.getPassword(),
                "changePass"));
        assertEquals("False", uc.changePassword(testUser.getName(), "wrong",
                testUser.getPassword()));
    }

    @After
    public void clear() {
        groupDatabaseLayer.deleteUser(groupDatabaseLayer.getGroup(GENERAL_ID), testUser);
        test.deleteUser(testUser);
    }
}
