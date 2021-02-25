package com.neu.prattle.websocket;

import com.neu.prattle.model.Message;
import com.neu.prattle.websocket.MessageDecoder;

import org.junit.Test;

import javax.websocket.EndpointConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TestDecode {

    @Test
    public void testDecode() {
        MessageDecoder d = new MessageDecoder();
        Message m = new Message();
        m.setFrom("Matt");
        m.setContent("Hello");
        assertEquals(m.toString(),
                d.decode("{\"messageId\":null,\"from\":\"Matt\",\"recipient\":null,\"content\":\"Hello\",\"active\":null,\"recordCreateDate\":null}").toString());
        assertNull(d.decode("hello"));
    }

    @Test
    public void testWillDecode() {
        MessageDecoder d = new MessageDecoder();
        assertTrue(d.willDecode("Hello"));
        assertFalse(d.willDecode(null));
    }

    @Test
    public void testInit() {
        MessageDecoder decoder = new MessageDecoder();
        EndpointConfig config = mock(EndpointConfig.class);
        decoder.init(config);
    }

    @Test
    public void testDestroy() {
        MessageDecoder d = new MessageDecoder();
        d.destroy();
    }
}
