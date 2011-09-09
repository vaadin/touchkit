package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.GWT;
import com.vaadin.terminal.gwt.client.VDebugConsole;

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
        String host = GWT.getHostPageBaseURL().replaceFirst("http://", "")
                .replaceFirst(":.*", "");
        return "http://" + host + ":8080/remotelog";
    }

}
