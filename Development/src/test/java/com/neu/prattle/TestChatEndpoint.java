package com.neu.prattle;
import com.neu.prattle.model.Message;
import com.neu.prattle.service.MessageDatabaseImpl;
import com.neu.prattle.service.MessageDatabaseLayer;
import com.neu.prattle.websocket.ChatEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import javax.websocket.Session;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static org.junit.Assert.assertEquals;
public class TestChatEndpoint {
    private static final MessageDatabaseLayer messageDatabaseLayer = MessageDatabaseImpl.getInstance();
    private Message message;
    @Mock
    Session session;
    @InjectMocks
    private ChatEndpoint c;

    @Before
    public void init() {
        c = new ChatEndpoint();
        message = new Message("testFrom", "testRecipient", "testHello");
    }
    @Test
    public void testOnClose(){
        c.onClose(session);
    }
    @Test
    public void testMessageDatabase() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = ChatEndpoint.class.getDeclaredMethod("messageDatabase", Message.class);
        m.setAccessible(true);
        m.invoke(c, message);
        assertEquals("testFrom", messageDatabaseLayer.getMessage(message.getMessageId()).getFrom());
        messageDatabaseLayer.deleteMessage(message);
    }
    @Test
    public void testCloseDuplicates() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = ChatEndpoint.class.getDeclaredMethod("closeDuplicates", String.class);
        m.setAccessible(true);
        m.invoke(c, "Tyler");
    }
}