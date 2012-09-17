package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.user.client.Window.Location;
import com.vaadin.client.VDebugConsole;

/**
 * Use this instead of VDebugConsole with deferred binding, if "remote console"
 * is needed.
 */
public class VRemoteDebugConsole extends VDebugConsole {

    @Override
    public void log(String msg) {
        super.log(msg);
        remoteLog(msg);
    }

    @Override
    protected String getRemoteLogUrl() {
        return "http://" + Location.getHost() +"/remotelog";
    }

}
