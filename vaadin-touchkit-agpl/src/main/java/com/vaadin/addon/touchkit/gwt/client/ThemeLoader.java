package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.vaadin.addon.touchkit.gwt.client.debugstyles.DebugWindowStyles;
import com.vaadin.client.ApplicationConfiguration;

/**
 * This class loads the GWT built theme used by TouchKit. If you wish to
 * override or extend the default theme, use deferred binding to replace the
 * default implementation.
 * 
 */
public class ThemeLoader {

    /**
     * Loads TouchKit theme. Called automatically by entrypoint.
     */
    public void load() {
        VTouchKitResources.INSTANCE.css().ensureInjected();
        
        if(ApplicationConfiguration.isDebugMode()) {
            GWT.runAsync(new RunAsyncCallback() {
                @Override
                public void onSuccess() {
                    DebugWindowStyles dws = GWT
                            .create(DebugWindowStyles.class);
                    dws.css().ensureInjected();
                }
                
                @Override
                public void onFailure(Throwable reason) {
                }
            });
        }
    }

}
