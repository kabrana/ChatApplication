package com.neu.prattle.service;

import com.neu.prattle.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestMessageDatabase {

    private final MessageDatabaseLayer messageDatabase = MessageDatabaseImpl.getInstance();

    private Message message;

    @Before
    public void init() {
        message = new Message("Benjamin", "Tyler", "whats up");
    }

    @Test
    public void testCreate() {
        messageDatabase.createMessage(message);
        assertEquals(message.getMessageId(),
                messageDatabase.getMessage(message.getMessageId()).getMessageId());
        messageDatabase.deleteMessage(message);
    }

    @Test
    public void testDeleteMessage() {
        messageDatabase.createMessage(message);
        messageDatabase.deleteMessage(message);
        assertNull(messageDatabase.getMessage(message.getMessageId()));
    }

    @Test
    public void testLoad() {
        messageDatabase.createMessage(message);
        assertEquals("whats up",
                messageDatabase.getMessage(message.getMessageId()).getContent());
        messageDatabase.deleteMessage(message);
    }

    @Test
    public void testUpdateMessage() {
        Message original = new Message("Matt", "Ben", "oldMsg");
        messageDatabase.createMessage(original);
        messageDatabase.updateMessage(original, "newMsg");
        assertEquals("newMsg", messageDatabase.getMessage(original.getMessageId()).getContent());
        messageDatabase.deleteMessage(original);

    }
}