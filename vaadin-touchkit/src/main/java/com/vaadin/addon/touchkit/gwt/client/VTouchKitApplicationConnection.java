package com.vaadin.addon.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ValueMap;

/**
 * Adds support for suspending and removing UIDL handling. Helps to implement
 * animations so that UIDL rendering doesn't make it look jumpy and the
 * communication is still safe.
 * 
 * TODO consider moving this stuff to core. Having own AC in touchkit makes it
 * hard for enusers to incorporate for e.g. DontPush for websocket communication
 * (which would otherwise be perfect match for mobile webkits).
 * 
 */
public class VTouchKitApplicationConnection extends ApplicationConnection {

    private static final int MAX_TIMEOUT = 5000;
    private Collection<Object> locks = new ArrayList<Object>();
    private Date start;
    private String jsonText;
    private ValueMap json;

    /**
     * TODO remove this before release.
     */
    Timer forceHandleMessage = new Timer() {
        @Override
        public void run() {
            VConsole.log("WARNING: rendering was never resumed, forcing reload...");
            Location.reload();
        }
    };

    @Override
    protected void handleUIDLMessage(Date start, String jsonText, ValueMap json) {
        if (locks.isEmpty()) {
            super.handleUIDLMessage(start, jsonText, json);
        } else {
            VConsole.log("Posponing UIDL handling due to lock...");
            this.start = start;
            this.jsonText = jsonText;
            this.json = json;
            forceHandleMessage.schedule(MAX_TIMEOUT);
        }
    }

    /**
     * This method can be used to postpone rendering of a response for a short
     * period of time (e.g. to avoid rendering process during animation).
     * 
     * @param lock
     */
    public void suspendRendering(Object lock) {
        locks.add(lock);
    }

    public void resumeRendering(Object lock) {
        VConsole.log("...resuming UIDL handling.");
        locks.remove(lock);
        if (locks.isEmpty()) {
            VConsole.log("locks empty, resuming really");
            forceHandleMessage.cancel();
            handlePendingMessage();
        }
    }

    private void handlePendingMessage() {
        if (json != null) {
            super.handleUIDLMessage(start, jsonText, json);
            json = null;
        }
    }

}
