package com.vaadin.addon.touchkit.gwt.client;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.VView;

public class VTouchKitView extends VView {

    private ApplicationConnection client;
    private int persistentSessionTimeout;
    private Timer persistenSessionCookieUpdator;

    @Override
    public void setStyleName(String style) {
        super.setStyleName(style + " v-touchkit-view");
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);
        if (uidl.hasAttribute("geoloc")) {
            try {
                VConsole.log("Making geolocation request");
                doGeoLocationLookup();
            } catch (Exception e) {
                onGeolocationError(GEOLOCATION_ERROR_UNKNOWN);
            }
        }
        this.client = client;

        if (uidl.hasAttribute("persistSession")) {
            persistentSessionTimeout = uidl.getIntAttribute("persistSession");
            if (persistenSessionCookieUpdator == null) {
                /*
                 * TODO add plugin architecture to ApplicationConnection and add
                 * this as some sort of listener to it.
                 * 
                 * Implementation note: window close listeners are not fired in
                 * mobile safari -> need to implement with timer.
                 */
                persistenSessionCookieUpdator = new Timer() {
                    @Override
                    public void run() {
                        updateSessionCookieExpiration();
                    }
                };
                persistenSessionCookieUpdator.scheduleRepeating(59000);
                persistenSessionCookieUpdator.run();
            }
        } else if (persistenSessionCookieUpdator != null) {
            persistenSessionCookieUpdator.cancel();
            persistenSessionCookieUpdator = null;
        }

    }

    private native void doGeoLocationLookup()
    /*-{

    	var me = this;
    	var success = function(loc) {
    		me.@com.vaadin.addon.touchkit.gwt.client.VTouchKitView::onGeolocationSuccess(Lcom/vaadin/addon/touchkit/gwt/client/VPosition;)(loc);
    	}
    	var error = function(e) {
    		me.@com.vaadin.addon.touchkit.gwt.client.VTouchKitView::onGeolocationError(I)(e.code);
    	}
    	
    	$wnd.navigator.geolocation.getCurrentPosition(success, error);
    
    }-*/;

    private void onGeolocationSuccess(VPosition position) {

        VConsole.log("Position detected.");
        String json = position.toJson();
        client.updateVariable(client.getPid(this), "position", json, true);
    }

    private static final int GEOLOCATION_ERROR_PERMISSION_DENIED = 1;
    private static final int GEOLOCATION_ERROR_POSITION_UNAVAILABLE = 2;
    private static final int GEOLOCATION_ERROR_POSITION_TIMEOUT = 3;
    private static final int GEOLOCATION_ERROR_UNKNOWN = 0;

    private void onGeolocationError(int errorCode) {
        VConsole.log("Error in geolocation" + 0);
        client.updateVariable(client.getPid(this), "positionError", errorCode,
                true);
    }

    public void updateSessionCookieExpiration() {
        String cookie = Cookies.getCookie("JSESSIONID");
        Date date = new Date();
        date = new Date(date.getTime() + persistentSessionTimeout * 1000L);
        Cookies.setCookie("JSESSIONID", cookie, date);
    }

}
