/*
 * PRODYNA PAC 2015 - Time Tracker
 * Anastasios Patrikis
 */
package com.prodyna.pac.timetracker.websocket;

import com.prodyna.pac.timetracker.server.event.BookingForApporvalEvent;
import com.prodyna.pac.timetracker.server.event.BookingForReworkEvent;
import com.prodyna.pac.timetracker.websocket.connector.ClusterConnector;
import javax.inject.Inject;
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

    @Inject
    private ClusterConnector cluster;

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

    /**
     * Send the message to the localy registered user or broadcast to cluster to
     * chekc if the user is logged in somewhere else.
     *
     * @param eMail
     * @param message
     */
    private void send(String eMail, String message) {
        boolean receiverFound = msgEndpoint.sendMessage(eMail, message);
        if (!receiverFound) {
            cluster.broadcastInCluster(eMail, message);
        }
    }
}
