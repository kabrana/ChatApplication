package com.neu.prattle.model;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class TestMessageModel {

    private Message emptyConstructor;
    private Message fullConstructor;

    @Before
    public void setup() {
        emptyConstructor = new Message();
        fullConstructor = new Message("General Kenobi", "General Grievous",
                "Hello there");
    }

    @Test
    public void testSetID() throws NoSuchFieldException, IllegalAccessException {
        emptyConstructor.setMessageId("ID1");
        Field field = emptyConstructor.getClass().getDeclaredField("messageId");
        field.setAccessible(true);
        assertEquals("ID1", field.get(emptyConstructor));
    }

    @Test
    public void testGetID() {
        emptyConstructor.setMessageId("1");
        assertEquals("1", emptyConstructor.getMessageId());
    }

    @Test
    public void testSetFrom() throws NoSuchFieldException, IllegalAccessException {
        emptyConstructor.setFrom("Matt");
        Field field = emptyConstructor.getClass().getDeclaredField("from");
        field.setAccessible(true);
        assertEquals("Matt", field.get(emptyConstructor));
    }

    @Test
    public void testGetFrom() {
        emptyConstructor.setFrom("Matt");
        assertEquals("Matt", emptyConstructor.getFrom());
    }

    @Test
    public void testSetRecipient() throws NoSuchFieldException, IllegalAccessException {
        emptyConstructor.setRecipient("Matt");
        Field field = emptyConstructor.getClass().getDeclaredField("recipient");
        field.setAccessible(true);
        assertEquals("Matt", field.get(emptyConstructor));
    }

    @Test
    public void testGetRecipient() {
        emptyConstructor.setFrom("Matt");
        assertEquals("Matt", emptyConstructor.getFrom());
    }

    @Test
    public void testSetContent() throws NoSuchFieldException, IllegalAccessException {
        emptyConstructor.setContent("Hello there");
        Field field = emptyConstructor.getClass().getDeclaredField("content");
        field.setAccessible(true);
        assertEquals("Hello there", field.get(emptyConstructor));
    }

    @Test
    public void testGetContent() {
        emptyConstructor.setContent("Hello there");
        assertEquals("Hello there", emptyConstructor.getContent());
    }

    @Test
    public void testGetActive() {
        assertTrue(fullConstructor.getActive());
    }

    @Test
    public void testSetActive() throws NoSuchFieldException, IllegalAccessException {
        fullConstructor.setActive(false);
        Field field = fullConstructor.getClass().getDeclaredField("active");
        field.setAccessible(true);
        assertEquals(false, field.get(fullConstructor));
    }

    @Test
    public void testSetRecordDate() throws NoSuchFieldException, IllegalAccessException {
        fullConstructor.setRecordCreateDate("now");
        Field field = fullConstructor.getClass().getDeclaredField("recordCreateDate");
        field.setAccessible(true);
        assertEquals("now", field.get(fullConstructor));
    }

    @Test
    public void testGetRecordDate() throws NoSuchFieldException, IllegalAccessException {
        fullConstructor.setRecordCreateDate("tomorrow");
        assertEquals("tomorrow", fullConstructor.getRecordCreateDate());;
    }

    @Test
    public void testToString() {
        assertEquals("From: General Kenobi Recipient: General Grievous " +
                "Content: Hello there CreateDate: " + fullConstructor.getRecordCreateDate() +
                " MessageId: " + fullConstructor.getMessageId(), fullConstructor.toString());
    }

    @Test
    public void testCompareTo() {
        Message m1 = new Message("matt", "ben", "hello");
        m1.setRecordCreateDate("2020-04-09T14:25:07.242859Z");
        Message m2 = new Message("matt", "ben", "hello");
        m2.setRecordCreateDate("2020-04-09T14:25:07.242859Z");
        assertEquals(1, m1.compareTo(m2));

        m2.setRecordCreateDate("2020-05-09T14:25:07.242859Z");
        assertEquals(-1, m1.compareTo(m2));
    }

    @Test
    public void testEquals(){
        Message m1 = new Message("matt", "ben", "hello");
        Message m2 = new Message("matt", "ben", "hello");
        assertNotEquals(m1, m2);
        m2.setMessageId(m1.getMessageId());
        assertEquals(m1, m2);

        assertNotEquals(m1, new User());
    }

    @Test
    public void testHashcode(){
        Message m1 = new Message("matt", "ben", "hello");
        Message m2 = new Message("matt", "ben", "hello");
        assertEquals(m1.hashCode(), m2.hashCode());
    }
}
