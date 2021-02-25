package com.neu.prattle.service;

import com.neu.prattle.model.Message;

/**
 * Represents all possible queries for the message objects stored within the database. Any attempts to alter messages,
 * or modify them when they do not exist results in nothing happening.
 */
public interface MessageDatabaseLayer {

    /**
     * Stores given message in the database.
     * @param message The message object as defined in the model package.
     */
    void createMessage(Message message);

    /**
     * Deletes a message object from the database.
     * @param message The message object as defined in the model package.
     */
    void deleteMessage(Message message);

    /**
     * Retrieves a message object from the database.
     * @param message The message object as defined in the model package.
     * @return The message specified from the database.
     */
    Message getMessage(String message);

    /**
     * Updates message content in the database.
     * @param message The message object as defined in the model package.
     * @param content The content you want to have replace the original content.
     */
    void updateMessage(Message message, String content);

}
