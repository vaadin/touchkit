package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.ApplicationConfiguration;

/**
 * This entry point checks if the actual Vaadin application starts. If not, the
 * pure GWT (client side) OfflineMode application is started.
 */
public class OfflineModeEntrypoint implements EntryPoint {

    private static TouchKitOfflineApp app;

    @Override
    public void onModuleLoad() {
        if (!isNetworkOnline()) {
            getApp().activate("No network connection", 0);
        } else {
            new Timer() {
                @Override
                public void run() {
                    if (ApplicationConfiguration.getRunningApplications()
                            .isEmpty()
                            || !ApplicationConfiguration
                                    .getRunningApplications().get(0)
                                    .isApplicationRunning()) {
                        getApp().activate(
                                "Online app didn't start properly, server down?",
                                0);
                    }
                }
            }.schedule(5000);
        }
    }

    public static TouchKitOfflineApp getApp() {
        if (app == null) {
            app = GWT.create(TouchKitOfflineApp.class);
        }
        return app;
    }

    private static native boolean isNetworkOnline()
    /*-{
        return $wnd.navigator.onLine;
    }-*/;

}
