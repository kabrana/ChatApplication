package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.service.MessageDatabaseImpl;
import com.neu.prattle.service.MessageDatabaseLayer;
import com.neu.prattle.service.TranslationImpl;
import com.neu.prattle.service.TranslationLayer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTranslate {

    private static final TranslationLayer accountService = TranslationImpl.getInstance();

    @Test
    public void testTranslation() {
        Message message = new Message("Ben", "Group1", "Ciao, come va?");

        MessageDatabaseLayer messageDatabase = MessageDatabaseImpl.getInstance();

        messageDatabase.createMessage(message);
        Message mes = messageDatabase.getMessage(message.getMessageId());
        assertEquals("Hi, how's it going?", accountService.translate("en", mes.getContent()));
        messageDatabase.deleteMessage(message);
    }
}
