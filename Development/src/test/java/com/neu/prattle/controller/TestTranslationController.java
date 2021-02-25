package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTranslationController {

    private final TranslationController tc = new TranslationController();

    @Test
    public void testTranslate() throws JsonProcessingException {
        assertEquals("\"bonjour\"", tc.translate("fr", "hello"));
        assertEquals("\"Hallo\"", tc.translate("de", "hello"));
    }
}
