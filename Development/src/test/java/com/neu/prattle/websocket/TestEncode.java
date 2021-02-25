package com.neu.prattle.websocket;

import com.neu.prattle.model.Message;
import com.neu.prattle.websocket.MessageEncoder;

import org.junit.Test;

import javax.websocket.EndpointConfig;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TestEncode {


    @Test
    public void testInit() {
        MessageEncoder e = new MessageEncoder();
        EndpointConfig config = mock(EndpointConfig.class);
        e.init(config);
    }

    @Test
    public void testDestroy() {
        MessageEncoder e = new MessageEncoder();
        e.destroy();
    }
}
