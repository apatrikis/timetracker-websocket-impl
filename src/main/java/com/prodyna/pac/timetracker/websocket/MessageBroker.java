/*
 * PRODYNA PAC 2015 - Time Tracker
 * Anastasios Patrikis
 */
package com.prodyna.pac.timetracker.websocket;

import com.prodyna.pac.timetracker.server.event.BookingForApporvalEvent;
import com.prodyna.pac.timetracker.server.event.BookingForReworkEvent;
import javax.inject.Singleton;

/**
 * This class is responsible for routing messgaes between
 * <ul>
 * <li>a client and this server</li>
 * <li>this server and it's connected clients</li>
 * <li>in case of a cluser : send messages to the other cluster members</li>
 * </ul>
 *
 * @author apatrikis
 */
@Singleton
public class MessageBroker implements MessageBrokerAPI {

    private final MessageEndpoint msgEndpoint;

    /**
     * Default constructor.
     */
    public MessageBroker() {
        msgEndpoint = new MessageEndpoint();
    }

    public void sendApproval(BookingForApporvalEvent approvalEvent) {
        send(approvalEvent.getReceiver(), approvalEvent.getMessage());
    }

    public void sendRework(BookingForReworkEvent reworkEvent) {
        send(reworkEvent.getReceiver(), reworkEvent.getMessage());
    }

    private void send(String eMail, String message) {
        msgEndpoint.sendMessage(eMail, message);
    }
}
