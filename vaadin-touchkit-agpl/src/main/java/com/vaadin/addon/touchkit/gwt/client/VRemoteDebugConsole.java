package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.user.client.Window.Location;
import com.vaadin.client.VDebugConsole;

/**
 * This replacement class for VConsole pipes all log messages to the server side
 * console. Useful when debugging client side code on e.g. Android devices. For
 * iOS devices, use Safari for remote debugging and inspecting of the DOM.
 * <p>
 * Use this instead of VDebugConsole with deferred binding, if needed:
 * 
 * <code>
    <replace-with class="com.vaadin.addon.touchkit.gwt.client.VRemoteDebugConsole">  
    <when-type-is class="com.vaadin.client.Console" /> </replace-with>
   </code>
 */
public class VRemoteDebugConsole extends VDebugConsole {

    @Override
    public void log(String msg) {
        super.log(msg);
        remoteLog(msg);
    }

    @Override
    protected String getRemoteLogUrl() {
        return "http://" + Location.getHost() + "/remotelog";
    }

}
