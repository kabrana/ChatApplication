package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.io.File;

/**
 * Class implementation for the UserDatabaseLayer interface. Responsible for any modifications to user objects
 * within the database as well as simple queries of users regarding creation, deletion, retrieval as well as avatars.
 */
public class UserDatabaseLayerImpl implements UserDatabaseLayer {

    private static final UserDatabaseLayer accountService = new UserDatabaseLayerImpl();

    /**
     * The database service.
     *
     * @return UserDatabaseLayer ADT.
     */
    public static UserDatabaseLayer getInstance() {
        return accountService;
    }

    @Override
    public boolean userExist(User user) {
        return DynamoDBCredentials.dynamoDBS3Mapper().load(User.class, user.getName()) != null;
    }

    @Override
    public synchronized void deleteUser(User user) {
        DynamoDBCredentials.dynamoDBS3Mapper().delete(user);
    }

    @Override
    public synchronized void addUser(User user) {
        DynamoDBCredentials.dynamoDBS3Mapper().save(user);
    }

    @Override
    public User getUser(String username) {
        return DynamoDBCredentials.dynamoDBS3Mapper().load(User.class, username);
    }

    @Override
    public void setAvatar(User user, File file) {
        user.setAvatar(DynamoDBCredentials.dynamoDBS3Mapper().createS3Link("us-east-2", "imagestestbenjaminsteele", "Users/" + user.getName() + "/avatar.jpeg"));
        user.getAvatar().uploadFrom(file);
        DynamoDBCredentials.dynamoDBS3Mapper().save(user);
    }
}