package com.neu.prattle.service;

import com.neu.prattle.model.Filter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestFilterImpl {

    private final FilterLayer filterDatabaseLayer = FilterImpl.getInstance();

    private Filter f1;
    private Filter f2;

    @Before
    public void init() {
        f1 = new Filter("1");
        f2 = new Filter("2");
    }

    @Test
    public void testAddBatchWords() {
        List<Filter> batch = new ArrayList<>();
        batch.add(f1);
        batch.add(f2);
        filterDatabaseLayer.addBatchWords(batch);
        assertTrue(filterDatabaseLayer.wordExist(f1));
        assertTrue(filterDatabaseLayer.wordExist(f2));
        filterDatabaseLayer.deleteWord(f1);
        filterDatabaseLayer.deleteWord(f2);
    }

    @Test
    public void testAddWord() {
        filterDatabaseLayer.addWord(f1);
        assertTrue(filterDatabaseLayer.wordExist(f1));
        filterDatabaseLayer.deleteWord(f1);
    }

    @Test
    public void testWordExist() {
        filterDatabaseLayer.addWord(f1);
        assertTrue(filterDatabaseLayer.wordExist(f1));
        filterDatabaseLayer.deleteWord(f1);
        assertFalse(filterDatabaseLayer.wordExist(f2));
    }

    @Test
    public void testGetList() {
        assertNotNull(FilterImpl.getList());
    }

    @After
    public void clear() {
        filterDatabaseLayer.deleteWord(f1);
        filterDatabaseLayer.deleteWord(f2);
    }
}
