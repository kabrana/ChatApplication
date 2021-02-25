package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TestGroupDatabase {

  private final GroupDatabaseLayer groupDatabaseLayer = GroupDatabaseImpl.getInstance();

  private User testUser1;
  private User testUser2;
  private Set<String> users;

  @Before
  public void init(){
    testUser1 = new User("testUser1", "123");
    testUser2 = new User("testUser2", "1234");
    users = new HashSet<>();
  }

  @Test
  public void testCreateGroup() {
    users.add(testUser1.getName());
    users.add(testUser2.getName());
    Group testCreate = new Group("test", users, true);
    groupDatabaseLayer.createGroup(testCreate);
    assertTrue(groupDatabaseLayer.groupExists(testCreate));
    groupDatabaseLayer.deleteGroup(testCreate);
  }

  @Test
  public void testDeleteGroup() {
    users.add(testUser1.getName());
    Group toDelete = new Group("toDelete", users, true);
    groupDatabaseLayer.createGroup(toDelete);
    groupDatabaseLayer.deleteGroup(toDelete);
    assertFalse(groupDatabaseLayer.groupExists(toDelete));
  }

  @Test
  public void testUpdateGroup() {
    users.add(testUser1.getName());
    Group updated = new Group("updated", users, true);
    groupDatabaseLayer.createGroup(updated);
    users.add(testUser2.getName());
    groupDatabaseLayer.updateGroup(updated);
    assertTrue(groupDatabaseLayer.userExists(updated, testUser2));
    groupDatabaseLayer.deleteGroup(updated);
  }

  @Test
  public void testGetMembers() {
    users.add(testUser1.getName());
    users.add(testUser2.getName());
    Group members = new Group("members", users, false);
    groupDatabaseLayer.createGroup(members);
    assertEquals(2, groupDatabaseLayer.getMembers(members).size());
    groupDatabaseLayer.deleteGroup(members);
  }

  @Test
  public void testGetModerators() {
    users.add(testUser1.getName());
    Group modGroup = new Group("ModGroup", users, false);
    modGroup.addUser(testUser2);
    groupDatabaseLayer.createGroup(modGroup);
    assertEquals("testUser1", modGroup.getModeratorList().iterator().next());
    groupDatabaseLayer.deleteGroup(modGroup);
  }

  @Test
  public void testAddUser() {
    users.add(testUser1.getName());
    Group add = new Group("1", users, false);
    add.addUser(testUser2);
    groupDatabaseLayer.createGroup(add);
    assertTrue(groupDatabaseLayer.userExists(add, testUser1));
    groupDatabaseLayer.deleteGroup(add);
  }

  @Test
  public void testDeleteUser() {
    users.add(testUser1.getName());
    Group add = new Group("1", users, false);
    add.addUser(testUser2);
    groupDatabaseLayer.createGroup(add);
    assertTrue(groupDatabaseLayer.userExists(add, testUser1));
    groupDatabaseLayer.deleteUser(add, testUser1);
    assertFalse(groupDatabaseLayer.userExists(add, testUser1));
    groupDatabaseLayer.deleteGroup(add);
  }

  @Test
  public void testFindUser() {
    users.add(testUser1.getName());
    Group find = new Group("find2", users, false);
    groupDatabaseLayer.createGroup(find);
    groupDatabaseLayer.addUser(find, testUser2);
    assertTrue(groupDatabaseLayer.userExists(find, testUser2));
    assertFalse(groupDatabaseLayer.userExists(find, new User("Not there", "fwfcs")));
    assertFalse(groupDatabaseLayer.userExists(new Group("DNE", users, false), testUser2));
    groupDatabaseLayer.deleteGroup(find);
  }

  @Test
  public void testGroupExists() {
    users.add(testUser1.getName());
    Group exists = new Group("exists", users, true);
    groupDatabaseLayer.createGroup(exists);
    Group exists1 = new Group("exists", users, true);
    assertTrue(groupDatabaseLayer.groupExists(exists));
    assertFalse(groupDatabaseLayer.groupExists(exists1));
    groupDatabaseLayer.deleteGroup(exists);
  }

  @Test
  public void testGetGroup() {
    User newUser1 = new User("test1234", "1231");
    users.add(newUser1.getName());
    Group getExists = new Group("exists", users, true);
    groupDatabaseLayer.createGroup(getExists);
    assertEquals(getExists.getGroupID(), groupDatabaseLayer.getGroup(getExists.getGroupID()).getGroupID());
    groupDatabaseLayer.deleteGroup(getExists);
  }
}