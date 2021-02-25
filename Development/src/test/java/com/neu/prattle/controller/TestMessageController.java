package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMessageController {

    private static final String GENERAL_ID = "\"83cb9551-727c-4f3d-ab1d-edd5dc6d7c83\"";
    private static final MessageController mc = new MessageController();


    @Test
    public void testGetMessagesByGroupId() throws JsonProcessingException {
        assertEquals("[]", mc.getMessagesByGroupID(GENERAL_ID));
    }
}
