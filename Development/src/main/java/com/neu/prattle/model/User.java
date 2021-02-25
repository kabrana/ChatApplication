package com.neu.prattle.model;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.S3Link;

import java.util.HashSet;
import java.util.Set;

/***
 * A User object represents a basic account information for a user.
 */
@DynamoDBTable(tableName = "Users")
public class User {

  /**
   * The unique name for this user.
   */
  private String name;
  /**
   * The password for this user account.
   */
  private String password;
  /**
   * The groups that this user is a part of.
   */
  private Set<String> groups;
  /**
   * The avatar for this user, to be displayed on the front end.
   */
  private S3Link avatar;
  /**
   * The current status for this user
   */
  private String status;

  /**
   * Construct a new user object with the given information. Status is set to Offline by default,
   * and the user is added to the general group.
   * @param name the name for this user.
   * @param password the password for this user.
   */
  public User(String name, String password) {
    this.name = name;
    this.password = password;
    this.status = "offline";
    this.groups = new HashSet<>();
    this.groups.add("83cb9551-727c-4f3d-ab1d-edd5dc6d7c83");
  }

  /**
   * Empty Constructor.
   */
  public User() {}

  /**
   * Get the unique username for this user.
   * @return the username for this user.
   */
  @DynamoDBHashKey(attributeName = "UserName")
  public String getName() { return name; }

  /**
   * Set the name for this user.
   * @param name the new name for this user.
   */
  public void setName(String name) { this.name = name; }

  /**
   * Get the password for this user.
   * @return the password for this user.
   */
  @DynamoDBAttribute(attributeName = "Password")
  public String getPassword() { return password; }

  /**
   * Set a new password for this user.
   * @param password the new password for this user.
   */
  public void setPassword(String password) { this.password = password; }

  /**
   * Get a list of the groups that this user is a part of.
   * @return a list of groupIDs that this user is a part of.
   */
  @DynamoDBAttribute(attributeName = "Channels")
  public Set<String> getGroups() { return groups; }

  /**
   * Set a list of groups for this user to be a part of.
   * @param groups the new list of groupIDs that this user will be a part of.
   */
  public void setGroups(Set<String> groups) { this.groups = groups; }

  /**
   * Get the URl for the image for this users avatar.
   * @return an S3Link to the image being stored for this users avatar.
   */
  @DynamoDBAttribute(attributeName = "Avatar")
  public S3Link getAvatar() { return avatar; }

  /**
   * Set this users avatar.
   * @param avatar a link to the new avatar for this user.
   */
  public void setAvatar(S3Link avatar){ this.avatar = avatar; }

  /**
   * Get the current status of this user.
   * @return the current status of this user.
   */
  @DynamoDBAttribute(attributeName = "Status")
  public String getStatus() { return status; }

  /**
   * Set the status of this user to a new status.
   * @param status the new status for this user.
   */
  public void setStatus(String status) { this.status = status; }

  /**
   * Add a new group this users existing list of groups.
   * @param group a groupID for the new group that this user is a part of.
   */
  public void addGroup(String group){ groups.add(group); }

  /**
   * Remove a group from the list of groups that this user is a part of.
   * @param group the groupID being removed from this users list of groups.
   */
  public void removeGroup(String group){ groups.remove(group); }
}