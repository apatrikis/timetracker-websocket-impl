/*
 * PRODYNA PAC 2015 - Time Tracker
 * Anastasios Patrikis
 */
package com.prodyna.pac.timetracker.websocket;

import java.time.Instant;
import java.util.HashMap;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;

/**
 * This is the glue code to handle the messaging between the web client and the
 * server. A main feature is the bidirectional communication from the server to
 * a specific client rather than a brodcast.
 * <p>
 * The message format is:</p>
 * {"event":"_the_event_name_","data":"_the_event_message_"}
 *
 * @author apatrikis
 *
 * @see
 * <a href="http://dreamand.me/java/jee7-websocket-example/">http://dreamand.me/java/jee7-websocket-example/</a>
 */
@ServerEndpoint(value = "/messages")
public class MessageEndpoint {

    @Inject
    private Logger log;

    private final static HashMap<String, RemoteEndpoint.Async> client2endpoint = new HashMap(50);
    private final static HashMap<String, String> email2client = new HashMap(50);
    private final static HashMap<String, String> client2email = new HashMap(50);

    @OnOpen
    public void onOpen(Session session) {
        log.debug("New connection with client: {}", session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        log.debug("New message from Client [{}]: {}", getEmail(message), getMessage(message));
        checkRegisterClient(message, session);
        return createMessageForClient(ServerMessageType.RESPONSE, "confirm");
    }

    @OnClose
    public void onClose(Session session) {
        log.debug("Close connection for client: {}", session.getId());
        unregisterClient(session);
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        log.warn("Error for client: {}", session.getId(), exception);
    }

    /**
     * Extract the senders email address from the message.
     *
     * @param message The composite message.
     * @return The senders email.
     */
    private String getEmail(String message) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(message);
            return json.get("event").toString();
        } catch (ParseException ex) {
            log.warn("Email not found", ex);
            return "";
        }
    }

    /**
     * Extract the plain email address from the message.
     *
     * @param message The composite message.
     * @return The plain message (without payload).
     */
    private String getMessage(String message) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(message);
            return json.get("data").toString();
        } catch (ParseException ex) {
            log.warn("Data not found", ex);
            return "";
        }
    }

    /**
     * Check if the sender of the message is already registered for receiving
     * callbacks.
     *
     * @param message The composite message.
     * @param session The user session.
     */
    private void checkRegisterClient(String message, Session session) {
        String email = getEmail(message);
        if ((email.length() > 0) && (email2client.containsKey(email) == false)) {
            client2endpoint.put(session.getId(), session.getAsyncRemote());
            client2email.put(session.getId(), email);
            email2client.put(email, session.getId());

            log.debug("Registered: {}", email);
            sendMessage(ServerMessageType.REGISTER_EMAIL, email, "sucess");
        }
    }

    /**
     * Unregister the client so no more messages will be sent.
     *
     * @param session The user session.
     */
    private void unregisterClient(Session session) {
        RemoteEndpoint.Async endpoint = client2endpoint.remove(session.getId());
        if (endpoint != null) {
            String email = client2email.remove(session.getId());
            email2client.remove(email);

            log.debug("Unregistered: {}", email);
            sendMessage(ServerMessageType.UNREGISTER_EMAIL, email, "sucess");
        }
    }

    /**
     * Send a message to the specified receiver.
     *
     * @param eMail The receivers email.
     * @param message The message to send.
     * @return {@code true} when the receiver is registered for receiving
     * callback messages.
     */
    public boolean sendMessage(String eMail, String message) {
        return sendMessage(ServerMessageType.GENRAL, eMail, message);
    }

    /**
     * Send a message to the specified receiver.
     *
     * @param messageType The type of message to send. The client may handle
     * different messages in different ways.
     * @param eMail The receivers email.
     * @param message The message to send.
     * @return {@code true} when the receiver is registered for receiving
     * callback messages.
     */
    public boolean sendMessage(ServerMessageType messageType, String eMail, String message) {
        String client = email2client.get(eMail);
        if (client != null) {
            client2endpoint.get(client).sendText(createMessageForClient(messageType, message));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a messge to send to the client.
     *
     * @param messageType The type of message to send. The client may handle
     * different messages in different ways.
     * @param message The message to send.
     * @return The message object as {@code JSON}.
     */
    private String createMessageForClient(ServerMessageType messageType, String message) {
        JSONObject json = new JSONObject();
        json.put("event", messageType.toString());
        json.put("data", String.format("%s: %s", Instant.now().toString(), message));
        return json.toJSONString();
    }
}
