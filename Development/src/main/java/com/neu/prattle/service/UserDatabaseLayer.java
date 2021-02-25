package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.io.File;

/**
 * Represents all possible queries for the user objects stored within the database. Any attempts to alter users, or
 * modify them when they do not exist results in nothing happening.
 */
public interface UserDatabaseLayer {

    /**
     * Queries the database if specified user exists.
     *
     * @param user User object specified in the model package.
     * @return True if user exists, false if not.
     */
    boolean userExist(User user);

    /**
     * Removes a user from the database.
     *
     * @param user User object specified in the model package.
     */
    void deleteUser(User user);

    /**
     * Adds a user to the database.
     *
     * @param user User object specified in the model package.
     */
    void addUser(User user);

    /**
     * Retrieves a user object from the database.
     *
     * @param username A string, the name of the user.
     * @return User object specified in the model package.
     */
    User getUser(String username);

    /**
     * Sets the avatar displayed on the front-end web client.
     *
     * @param user User object specified in the model package.
     * @param file The picture file you wish to use.
     */
    void setAvatar(User user, File file);
}
