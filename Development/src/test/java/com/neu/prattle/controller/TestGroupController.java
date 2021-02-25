package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupDatabaseImpl;
import com.neu.prattle.service.GroupDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayerImpl;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestGroupController {

    private final GroupDatabaseLayer groupDatabaseLayer = GroupDatabaseImpl.getInstance();
    private final GroupController gc = new GroupController();
    private final UserDatabaseLayer userDatabaseLayer = UserDatabaseLayerImpl.getInstance();
    private final UserController uc = new UserController();

    private static final String GENERAL_ID = "83cb9551-727c-4f3d-ab1d-edd5dc6d7c83";

    @Test
    public void testCreateGroup() {
        User user = new User("testUserGroupController","testUserGroupController");
        Set<String> users = new HashSet<>();
        users.add(user.getName());
        userDatabaseLayer.addUser(user);
        Group g = new Group("testGroupController", users, false);
        assertEquals(Response.ok().build().getStatus(), gc.createGroup(g).getStatus());
        Set<String> group = userDatabaseLayer.getUser(user.getName()).getGroups();
        for(String s: group){
            if(!s.equals("83cb9551-727c-4f3d-ab1d-edd5dc6d7c83")){
                gc.deleteGroup(s);
            }
        }
        userDatabaseLayer.deleteUser(user);
    }


    @Test
    public void testGetGroupObject() throws JsonProcessingException {
        Set<String> members = groupDatabaseLayer.getMembers(groupDatabaseLayer.getGroup(GENERAL_ID));
        StringBuilder membersListAsString = new StringBuilder();
        for(String s : members){
            membersListAsString.append("\"").append(s).append("\",");
        }
        membersListAsString.replace(membersListAsString.length() - 1, membersListAsString.length(),"");
        String object = "{\"groupID\":\"83cb9551-727c-4f3d-ab1d-edd5dc6d7c83\",\"groupName\":\"General\",\"moderatorList\":[\"\\\"-\\\"\"],\"groupList\":[" + membersListAsString + "],\"type\":false}";
        assertEquals(gc.getGroupObject(GENERAL_ID), object);
    }

    @Test
    public void testGetGroupName() throws JsonProcessingException {
        assertEquals("\"General\"", gc.getGroupName(GENERAL_ID));
    }

    @Test
    public void testDeleteGroup(){
        assertEquals(409, gc.deleteGroup("DNE").getStatus());
    }

    @Test
    public void testGetModerators() throws JsonProcessingException {
        assertEquals("[\"\\\"-\\\"\"]", gc.getGroupModerators(GENERAL_ID));
    }

    @Test
    public void testGetMemberList() throws JsonProcessingException {
        User user = new User("testUserGroupController","testUserGroupController");
        Set<String> users = new HashSet<>();
        users.add(user.getName());
        userDatabaseLayer.addUser(user);
        Group g = new Group("testGroupController", users, false);

        groupDatabaseLayer.createGroup(g);
        assertEquals("[\"testUserGroupController\"]", gc.getGroupMemberList(g.getGroupID()));
        groupDatabaseLayer.deleteGroup(g);
        userDatabaseLayer.deleteUser(user);
    }

    @Test
    public void addGroupMember(){
        User user = new User("testUserGroupController","testUserGroupController");
        User user1 = new User("testUserGroupController1","testUserGroupController");
        Set<String> users = new HashSet<>();
        users.add(user.getName());
        userDatabaseLayer.addUser(user);
        userDatabaseLayer.addUser(user1);
        Group g = new Group("testGroupController", users, false);
        groupDatabaseLayer.createGroup(g);
        assertEquals(200, gc.addGroupMember(g.getGroupID(),user1.getName()).getStatus());
        groupDatabaseLayer.deleteGroup(g);
        userDatabaseLayer.deleteUser(user);
        userDatabaseLayer.deleteUser(user1);
    }

    @Test
    public void removeGroupMember(){
        User user = new User("testUserGroupController","testUserGroupController");
        User user1 = new User("testUserGroupController1","testUserGroupController");
        Set<String> users = new HashSet<>();
        users.add(user.getName());
        userDatabaseLayer.addUser(user);
        userDatabaseLayer.addUser(user1);
        Group g = new Group("testGroupController", users, false);
        groupDatabaseLayer.createGroup(g);
        assertEquals(200, gc.removeGroupMember(g.getGroupID(),user1.getName()).getStatus());
        groupDatabaseLayer.deleteGroup(g);
        userDatabaseLayer.deleteUser(user);
        userDatabaseLayer.deleteUser(user1);
    }

    @Test
    public void promoteGroupMember(){
        User user = new User("testUserGroupController","testUserGroupController");
        User user1 = new User("testUserGroupController1","testUserGroupController");
        Set<String> users = new HashSet<>();
        users.add(user.getName());
        userDatabaseLayer.addUser(user);
        userDatabaseLayer.addUser(user1);
        Group g = new Group("testGroupController", users, false);
        groupDatabaseLayer.createGroup(g);
        assertEquals(200, gc.promoteGroupMember(g.getGroupID(),user1.getName()).getStatus());
        groupDatabaseLayer.deleteGroup(g);
        userDatabaseLayer.deleteUser(user);
        userDatabaseLayer.deleteUser(user1);
    }

    @Test
    public void isModerator(){
        User user = new User("testUserGroupController","testUserGroupController");
        User user1 = new User("testUserGroupController1","testUserGroupController");
        Set<String> users = new HashSet<>();
        users.add(user.getName());
        userDatabaseLayer.addUser(user);
        userDatabaseLayer.addUser(user1);
        Group g = new Group("testGroupController", users, false);
        groupDatabaseLayer.createGroup(g);
        assertEquals("false", gc.isModerators(g.getGroupID(),user1.getName()));
        assertEquals("true", gc.isModerators(g.getGroupID(),user.getName()));
        groupDatabaseLayer.deleteGroup(g);
        userDatabaseLayer.deleteUser(user);
        userDatabaseLayer.deleteUser(user1);
    }

    @Test
    public void leaveGroup(){
        User user = new User("testUserGroupController","testUserGroupController");
        User user1 = new User("testUserGroupController1","testUserGroupController");
        Set<String> users = new HashSet<>();
        users.add(user.getName());
        users.add(user1.getName());
        userDatabaseLayer.addUser(user);
        userDatabaseLayer.addUser(user1);
        Group g = new Group("testGroupController", users, false);
        groupDatabaseLayer.createGroup(g);
        assertEquals(200, gc.leaveGroup(g.getGroupID(),user1.getName()).getStatus());
        groupDatabaseLayer.deleteGroup(g);
        userDatabaseLayer.deleteUser(user);
        userDatabaseLayer.deleteUser(user1);
    }
}

