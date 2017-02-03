package org.vaadin.touchkit.gwt.client;


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
    }

}
