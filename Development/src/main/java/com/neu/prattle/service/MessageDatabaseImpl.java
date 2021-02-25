package com.neu.prattle.service;

import com.neu.prattle.model.Message;

/**
 * Class implementation for the MessageDatabaseLayer interface. Responsible for any modifications to message objects
 * within the database as well as simple queries of messages regarding creation, deletion, retrieval as well as update.
 */
public class MessageDatabaseImpl implements MessageDatabaseLayer {

    private static final MessageDatabaseLayer messageDatabaseService = new MessageDatabaseImpl();

    /**
     * The database service.
     *
     * @return MessageDatabaseLayer ADT.
     */
    public static MessageDatabaseLayer getInstance() {
        return messageDatabaseService;
    }

    @Override
    public void createMessage(Message message) {
        DynamoDBCredentials.dynamoDBS3Mapper().save(message);
    }

    @Override
    public void deleteMessage(Message message) {
        message.setActive(false);
        DynamoDBCredentials.dynamoDBS3Mapper().save(message);
        DynamoDBCredentials.dynamoDBS3Mapper().delete(message);
    }

    @Override
    public Message getMessage(String message) {
        return DynamoDBCredentials.dynamoDBS3Mapper().load(Message.class, message);
    }

    @Override
    public void updateMessage(Message message, String content) {
        message.setContent(content);
        DynamoDBCredentials.dynamoDBS3Mapper().save(message);
    }
}
