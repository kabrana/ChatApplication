package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestQuery {

    private final MessageDatabaseLayer messageDatabase = MessageDatabaseImpl.getInstance();
    private final GroupDatabaseLayer groupDatabaseLayer = GroupDatabaseImpl.getInstance();

    @Test
    public void testQuery() {
        User ben = new User("Ben", "hello");
        Set<String> users = new HashSet<>();
        users.add(ben.getName());
        Group g = new Group("1", users, true);
        groupDatabaseLayer.createGroup(g);

        Message m = new Message("Ben", g.getGroupID(), "hi");
        messageDatabase.createMessage(m);
        assertEquals(m.getMessageId(), Query.messages(g.getGroupID()).iterator().next().getMessageId());
        groupDatabaseLayer.deleteGroup(g);
        messageDatabase.deleteMessage(m);
    }
}
