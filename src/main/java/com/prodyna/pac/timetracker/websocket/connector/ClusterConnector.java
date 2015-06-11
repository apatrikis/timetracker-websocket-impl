/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prodyna.pac.timetracker.websocket.connector;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for sending and receiving a Message within a {@code Glassfish Cluster}.
 *
 * @see
 * <a href="http://stackoverflow.com/questions/11070116/how-to-notify-all-same-singleton-beans-in-a-glassfish-3-1-cluster">Glassfisch
 * Cluster Messaging</a>
 *
 * @author apatrikis
 */
@Startup
@Singleton
public class ClusterConnector {

    private static final Logger log = LoggerFactory.getLogger(ClusterConnector.class);

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void cleanup() {
    }

    /**
     * Broadcast messge to cluster members.
     *
     * @param eMail The user to check on the other cluster members.
     * @param message The message for the user.
     */
    public void broadcastInCluster(String eMail, String message) {
        // TODO
        log.warn("TODO: broadcast message in cluster");
    }
}
