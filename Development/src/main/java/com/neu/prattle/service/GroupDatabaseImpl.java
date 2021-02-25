package com.neu.prattle.service;


import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.Set;

/**
 * Class implementation for the GroupDatabaseLayer interface. Responsible for any modifications to group objects
 * within the database as well as simple queries of groups themselves, users within those groups, as well as current
 * moderators.
 */
public class GroupDatabaseImpl implements GroupDatabaseLayer {

    private static final GroupDatabaseLayer groupService = new GroupDatabaseImpl();

    /**
     * The database service.
     *
     * @return GroupDatabaseLayer ADT.
     */
    public static GroupDatabaseLayer getInstance() {
        return groupService;
    }


    @Override
    public synchronized void createGroup(Group group) {
        DynamoDBCredentials.dynamoDBMapper().save(group);
    }

    @Override
    public void updateGroup(Group group) {
        DynamoDBCredentials.dynamoDBMapper().save(group);
    }

    @Override
    public synchronized boolean groupExists(Group group) {
        return DynamoDBCredentials.dynamoDBMapper().load(Group.class, group.getGroupID()) != null;
    }

    @Override
    public Set<String> getMembers(Group group) {
        Group g = DynamoDBCredentials.dynamoDBMapper().load(Group.class, group.getGroupID());
        return g.getGroupList();
    }

    @Override
    public synchronized boolean userExists(Group group, User user) {
        return groupExists(group) && getMembers(group).contains(user.getName());
    }

    @Override
    public Set<String> getModerators(Group group) {
        Group g = DynamoDBCredentials.dynamoDBMapper().load(Group.class, group.getGroupID());
        return g.getModeratorList();
    }

    @Override
    public synchronized void addUser(Group group, User user) {
        group.addUser(user);
        DynamoDBCredentials.dynamoDBMapper().save(group);
    }

    @Override
    public void deleteUser(Group group, User user) {
        group.removeUser(user);
        DynamoDBCredentials.dynamoDBMapper().save(group);
    }

    @Override
    public synchronized void deleteGroup(Group group) {
        DynamoDBCredentials.dynamoDBMapper().delete(group);
    }

    @Override
    public Group getGroup(String groupID) {
        return DynamoDBCredentials.dynamoDBMapper().load(Group.class, groupID);
    }
}

