package com.vaadin.addon.touchkit.gwt.client.offlinemode;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason;
import com.vaadin.addon.touchkit.gwt.client.vcom.OfflineModeConnector;
import com.vaadin.client.ApplicationConfiguration;

/**
 * This entry point checks if the actual Vaadin application starts. If not, the
 * pure GWT (client side) OfflineMode application is started.
 */
public class OfflineModeEntrypoint implements EntryPoint {

    private static OfflineMode app;

    @Override
    public void onModuleLoad() {
        if (!OfflineModeConnector.isNetworkOnline()) {
            getOfflineMode().activate(
                    new OfflineModeActivationEventImpl("No network connection",
                            ActivationReason.NO_NETWORK));
        } else {
            // FIXME the core in V7 should get rid of its custom javascript
            // kickstart, now we can't hook to listen for bad responses for
            // initial requests :-(
            new Timer() {
                @Override
                public void run() {
                    if (ApplicationConfiguration.getRunningApplications()
                            .isEmpty()
                            || !ApplicationConfiguration
                                    .getRunningApplications().get(0)
                                    .isApplicationRunning()) {
                        if (!getOfflineMode().isActive()) {
                            getOfflineMode()
                                    .activate(
                                            new OfflineModeActivationEventImpl(
                                                    "The application didn't start properly.",
                                                    ActivationReason.ONLINE_APP_NOT_STARTED));
                        }

                    }
                }
            }.schedule(5000);
        }
    }

    /**
     * Creates an instance of {@link OfflineMode} by using GWT deferred binding.
     * 
     * @return the OfflineMode application.
     */
    public static OfflineMode getOfflineMode() {
        if (app == null) {
            app = GWT.create(OfflineMode.class);
        }
        return app;
    }

}
