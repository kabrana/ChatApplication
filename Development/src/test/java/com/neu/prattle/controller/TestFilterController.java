package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestFilterController {

    private static final FilterController fc = new FilterController();

    @Test
    public void testGetFilterWordList() throws JsonProcessingException {
        assertNotNull(fc.getFilterWordList());
    }
}
