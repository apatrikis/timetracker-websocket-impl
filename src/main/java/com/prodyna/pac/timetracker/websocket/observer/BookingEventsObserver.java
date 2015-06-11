/*
 * PRODYNA PAC 2015 - Time Tracker
 * Anastasios Patrikis
 */
package com.prodyna.pac.timetracker.websocket.observer;

import com.prodyna.pac.timetracker.server.event.BookingForApporvalEvent;
import com.prodyna.pac.timetracker.server.event.BookingForReworkEvent;
import com.prodyna.pac.timetracker.websocket.MessageBroker;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * The class is listening for {@code Event}s and handels the forwaring to the
 * appropriate processing classes.
 *
 * @author apatrikis
 */
@Stateless
public class BookingEventsObserver {

    @Inject
    private MessageBroker msgBroker;

    @Asynchronous
    public void processApprovalEvent(@Observes BookingForApporvalEvent approvalEvent) {
        msgBroker.sendApproval(approvalEvent);
    }

    @Asynchronous
    public void processReworkEvent(@Observes BookingForReworkEvent reworkEvent) {
        msgBroker.sendRework(reworkEvent);
    }
}
