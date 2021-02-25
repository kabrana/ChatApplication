package com.neu.prattle;

import com.neu.prattle.main.PrattleApplication;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestPrattleApp {

    @Test
    public void testPrattle() {
        PrattleApplication p = new PrattleApplication();
        assertNotNull(p.getClasses());
    }
}
