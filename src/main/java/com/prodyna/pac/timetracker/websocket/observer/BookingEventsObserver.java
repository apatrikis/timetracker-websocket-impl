/*
 * PRODYNA PAC 2015 - Time Tracker
 * Anastasios Patrikis
 */
package com.prodyna.pac.timetracker.websocket.observer;

import com.prodyna.pac.timetracker.server.event.BookingForApporvalEvent;
import com.prodyna.pac.timetracker.server.event.BookingForReworkEvent;
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
public class BookingEventsObserver {

    @Inject
    private Logger log;

    @Inject
    private MessageBrokerAPI msgBroker;

    /**
     * Process a {@link BookingForApporvalEvent}.
     *
     * @param approvalEvent The {@code Event} object to handle.
     */
    @Asynchronous
    public void processApprovalEvent(@Observes BookingForApporvalEvent approvalEvent) {
        log.debug("Received BookingForApporvalEvent");
        msgBroker.sendApproval(approvalEvent);
    }

    /**
     * Process a {@link BookingForReworkEvent}.
     *
     * @param reworkEvent The {@code Event} object to handle.
     */
    @Asynchronous
    public void processReworkEvent(@Observes BookingForReworkEvent reworkEvent) {
        log.debug("Received BookingForReworkEvent");
        msgBroker.sendRework(reworkEvent);
    }
}
