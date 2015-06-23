/*
 * PRODYNA PAC 2015 - Time Tracker
 * Anastasios Patrikis
 */
package com.prodyna.pac.timetracker.websocket.observer;

import com.prodyna.pac.timetracker.cluster.ClusterMessage;
import com.prodyna.pac.timetracker.jms.IncommingMessage;
import com.prodyna.pac.timetracker.websocket.MessageBrokerAPI;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 * The class is listening for {@code Event}s and handels the forwaring to the
 * appropriate processing classes.
 *
 * @author apatrikis
 */
@Stateless
public class ClusterEventsObserver {

    @Inject
    private Logger log;

    @Inject
    private MessageBrokerAPI msgBroker;

    /**
     * Process a {@link ClusterMessage}.
     *
     * @param clusterMessage The {@code Event} object to handle. It must be a
     * {@link IncommingMessage}.
     */
    @Asynchronous
    public void processMessageEvent(@Observes @IncommingMessage ClusterMessage clusterMessage) {
        log.debug("Received ClusterMessage event");
        msgBroker.receive(clusterMessage);
    }
}
