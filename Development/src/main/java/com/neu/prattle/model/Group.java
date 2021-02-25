package com.neu.prattle.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * An object to represent a group. This representation acts as a data container for all
 * relevant group information.
 */
@DynamoDBTable(tableName = "Group")
public class Group {

  /**
   * The unique ID for this group.
   */
  private String groupID;
  /**
   * The name for this group.
   */
  private String groupName;
  /**
   * A list of username's that represent all the moderators for this group.
   */
  private Set<String> moderatorList;
  /**
   * A list of users that are a part of this group.
   */
  private Set<String> groupList;
  /**
   * The type of group. Used to determine direct or group.
   */
  private Boolean type;

  /**
   * Construct a new group with the provided parameters. A new random ID is assigned upon creation.
   * @param groupName the name of the new group.
   * @param groupList a list of members for the new group.
   * @param type the type of group.
   */
  public Group(String groupName, Set<String> groupList, Boolean type) {
    this.groupID = UUID.randomUUID().toString();
    this.groupName = groupName;
    this.groupList = groupList;
    this.moderatorList = new HashSet<>();
    moderatorList.add(groupList.iterator().next());
    this.type = type;
  }

  /**
   * Empty Constructor.
   */
  public Group() {}

  /**
   * Get the unique group ID for this group.
   * @return the group ID for this group.
   */
  @DynamoDBHashKey(attributeName = "GroupID")
  public String getGroupID() { return groupID; }

  /**
   * Set a new group ID for this group.
   * @param groupID the new group ID for this group.
   */
  public void setGroupID(String groupID) { this.groupID = groupID; }

  /**
   * Get the name for this group.
   * @return the name of this group.
   */
  @DynamoDBAttribute(attributeName = "GroupName")
  public String getGroupName() { return groupName;}

  /**
   * Set a new name for this group.
   * @param name the new name for this group.
   */
  public void setGroupName(String name) { groupName = name;}

  /**
   * Get a list of members that are a part of this group
   * @return a list of usernames of the members of this group.
   */
  @DynamoDBAttribute(attributeName = "Members")
  public Set<String> getGroupList() { return groupList;}

  /**
   * Set the members of this group.
   * @param groupList a list of usernames to set as members of this group.
   */
  public void setGroupList(Set<String> groupList) { this.groupList = groupList; }

  /**
   * Get a list of the moderators for this group.
   * @return a list of usernames of the moderators of this group.
   */
  @DynamoDBAttribute(attributeName = "Moderators")
  public Set<String> getModeratorList(){ return moderatorList; }

  /**
   * Set the moderators of this group.
   * @param moderatorList a list of usernames to set as moderators of this group.
   */
  public void setModeratorList(Set<String> moderatorList){this.moderatorList = moderatorList; }

  /**
   * Get the type of this group.
   * @return the type of this group, either direct or group.
   */
  @DynamoDBAttribute(attributeName = "Direct")
  public Boolean getType() { return type; }

  /**
   * Set the type for this group.
   * @param type the type for this group.
   */
  public void setType(Boolean type) { this.type = type; }

  /**
   * Add a new user to the existing group list of members in this group.
   * @param user the user to add to this group
   */
  public void addUser(User user) { groupList.add(user.getName()); }

  /**
   * Remove an existing user from this group.
   * @param user the user being removed from this group.
   */
  public void removeUser(User user){ groupList.remove(user.getName()); }

}