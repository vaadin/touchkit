package com.vaadin.addon.touchkit.gwt.client;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ui.ui.VUI;

public class VTouchKitView extends VUI {

    private ApplicationConnection client;
    private Integer persistentSessionTimeout;

    @Override
    public void setStyleName(String style) {
        super.setStyleName(style + " v-touchkit-view");
    }

    // @Override
    // public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
    // super.updateFromUIDL(uidl, client);
    // this.client = client;
    //
    // if (uidl.hasAttribute("persistSession")) {
    // persistentSessionTimeout = uidl.getIntAttribute("persistSession");
    // } else if (persistentSessionTimeout != null) {
    // persistentSessionTimeout = null;
    // }
    //
    // if (uidl.getBooleanAttribute("goOffline")) {
    // ((VTouchKitApplicationConnection) client).goOffline(
    // "Going offline manually", -1);
    // }
    //
    // }

    public void updateSessionCookieExpiration() {
        if (persistentSessionTimeout != null) {
            String cookie = Cookies.getCookie("JSESSIONID");
            Date date = new Date();
            date = new Date(date.getTime() + persistentSessionTimeout * 1000L);
            Cookies.setCookie("JSESSIONID", cookie, date);
        }
    }

}
