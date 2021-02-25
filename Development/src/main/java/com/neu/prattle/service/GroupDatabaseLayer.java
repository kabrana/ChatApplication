package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.Set;

/**
 * Represents all possible queries for the group objects stored within the database. Any attempts to alter groups, or
 * modify them when they do not exist results in nothing happening.
 */
public interface GroupDatabaseLayer {

    /**
     * Creates a group object and adds it to the database.
     *
     * @param group A group object defined in the model package.
     */
    void createGroup(Group group);

    /**
     * Updates the group with any changes needed for the database.
     *
     * @param group A group object defined in the model package.
     */
    void updateGroup(Group group);

    /**
     * Retrieves a group from the database.
     *
     * @param groupID A string, the groupID.
     * @return The group object defined in the model package.
     */
    Group getGroup(String groupID);

    /**
     * Retrieves the members of the group from the database.
     *
     * @param group The group object defined in the model package.
     * @return A set of strings representing the members.
     */
    Set<String> getMembers(Group group);

    /**
     * Retrieves the moderators of a group from the database.
     *
     * @param group The group object defined in the model package.
     * @return A set of strings representing the moderators.
     */
    Set<String> getModerators(Group group);

    /**
     * Adds a user to the group and pushes the change to the database.
     *
     * @param group The group object defined in the model package.
     * @param user  The user object to be added to the group.
     */
    void addUser(Group group, User user);

    /**
     * Deletes a user from the group and pushes the change to the database. If the user does not exist, nothing happens.
     *
     * @param group The group object defined in the model package.
     * @param user  The user object to be removed from the group.
     */
    void deleteUser(Group group, User user);

    /**
     * Queries the database to see if a particular user is present in the group.
     *
     * @param group The group object defined in the model package.
     * @param user  The user object being checked.
     * @return True if user exists, false if not.
     */
    boolean userExists(Group group, User user);

    /**
     * Queries the database to see if given group exists.
     *
     * @param group The group object defined in the model package.
     * @return True if the group exists, false if not.
     */
    boolean groupExists(Group group);

    /**
     * Removes a group from the database. If the group does not exist, nothing happens.
     *
     * @param group The group object defined in the model package.
     */
    void deleteGroup(Group group);


}
