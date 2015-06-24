/*
 * PRODYNA PAC 2015 - Time Tracker
 * Anastasios Patrikis
 */
package com.prodyna.pac.timetracker.websocket;

import com.prodyna.pac.timetracker.cluster.ClusterMessage;
import com.prodyna.pac.timetracker.jms.OutgoingMessage;
import com.prodyna.pac.timetracker.server.event.BookingForApporvalEvent;
import com.prodyna.pac.timetracker.server.event.BookingForReworkEvent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;

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

    @Inject
    private Logger log;

    @Inject
    @OutgoingMessage
    private Event<ClusterMessage> cluster;

    /**
     * Default constructor.
     */
    public MessageBroker() {
        msgEndpoint = new MessageEndpoint();
    }

    @Override
    public void sendApproval(BookingForApporvalEvent approvalEvent) {
        send(approvalEvent.getReceiver(), approvalEvent.getMessage());
    }

    @Override
    public void sendRework(BookingForReworkEvent reworkEvent) {
        send(reworkEvent.getReceiver(), reworkEvent.getMessage());
    }

    @Override
    public void receive(ClusterMessage message) {
        log.debug("Broker incomming messsage");
        msgEndpoint.sendMessage(message.getReceiver(), message.getMessage());
    }

    /**
     * Send the message to the localy registered user or broadcast to cluster to
     * chekc if the user is logged in somewhere else.
     *
     * @param eMail
     * @param message
     */
    private void send(String eMail, String message) {
        log.debug("Broker outgoing messsage");
        boolean receiverFound = msgEndpoint.sendMessage(eMail, message);
        if (receiverFound == false) {
            cluster.fire(new ClusterMessage("", eMail, message));
        }
    }
}
