package com.neu.prattle.websocket;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class ChatEndpoint.
 * <p>
 * This class handles Messages that arrive on the server.
 */
@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "MismatchedQueryAndUpdateOfCollection"})
@ServerEndpoint(value = "/chatRoom.html/chat/{username}/{group}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

    private static final String USERNAME = "username";
    private static final String GROUP = "group";

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * The account service.
     */
    private final UserDatabaseLayer accountService = UserDatabaseLayerImpl.getInstance();

    /**
     * The database service.
     */
    private final GroupDatabaseLayer groupService = GroupDatabaseImpl.getInstance();

    /**
     * The session.
     */
    private Session session;

    /**
     * The Constant chatEndpoints.
     */
    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();

    /**
     * The users.
     */
    private static HashMap<String, String> users = new HashMap<>();

    /**
     * On open.
     * <p>
     * Handles opening a new session (websocket connection). If the user is a known user (user
     * management), the session added to the pool of sessions and an announcement to that pool is made
     * informing them of the new user.
     * <p>
     * If the user is not known, the pool is not augmented and an error is sent to the originator.
     *
     * @param session  the web-socket (the connection)
     * @param username the name of the user (String) used to find the associated UserService object
     * @throws IOException     Signals that an I/O exception has occurred.
     * @throws EncodeException the encode exception
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(USERNAME) String username, @PathParam("group") String selectedGroup) throws IOException, EncodeException {
        closeDuplicates(username);
        User user = accountService.getUser(username);
        Group group = groupService.getGroup(selectedGroup);
        session.getUserProperties().put(USERNAME, username);
        session.getUserProperties().put(GROUP, group);
        addEndpoint(user, session);
    }

    /**
     * On message.
     * <p>
     * When a message arrives, broadcast it to all connected users.
     *
     * @param session the session originating the message
     * @param message the text of the inbound message
     */
    @OnMessage
    public void onMessage(Session session, Message message) {
        Group tempGroup = (Group) session.getUserProperties().get(GROUP);
        Message newMessage = new Message((String) session.getUserProperties().get(USERNAME), tempGroup.getGroupID(), message.getContent());
        messageDatabase(newMessage);
        for (ChatEndpoint endpoint : chatEndpoints) {
            synchronized (endpoint) {
                try {
                    Group tempGroup2 = (Group) endpoint.session.getUserProperties().get(GROUP);
                    if (endpoint.session.isOpen() && tempGroup.getGroupID().equals(tempGroup2.getGroupID())) {
                        endpoint.session.getBasicRemote().sendObject(newMessage);
                    }
                } catch (IOException | EncodeException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }
    }

    /**
     * On close.
     * <p>
     * Closes the session by removing it from the pool of sessions and broadcasting the s to
     * everyone else.
     *
     * @param session the session
     */
    @OnClose
    public void onClose(Session session) {
        chatEndpoints.remove(this);
    }

    /**
     * On error.
     * <p>
     * Handles situations when an error occurs.  Not implemented.
     *
     * @param session   the session with the problem
     * @param throwable the action to be taken.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private void addEndpoint(User username, Session session) {
        this.session = session;
        chatEndpoints.add(this);
        users.put(username.getName(), session.getId());
    }

    private void messageDatabase(Message message) {
        MessageDatabaseLayer messageDatabase = MessageDatabaseImpl.getInstance();
        messageDatabase.createMessage(message);
    }

    private void closeDuplicates(String username) {
        for (ChatEndpoint endpoint : chatEndpoints) {
            synchronized (endpoint) {
                String endpointString = (String) endpoint.session.getUserProperties().get(USERNAME);
                if (endpointString.equals(username)) {
                    chatEndpoints.remove(endpoint);
                }
            }
        }
    }
}
