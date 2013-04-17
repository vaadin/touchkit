package com.vaadin.addon.touchkit.gwt.client;

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
        /*
         * We should use some saner method for "retina images". E.g.
         * https://github.com/kDCYorke/RetinaImages
         * 
         * At least should have a permutation for high dpi and use that in css
         * so that GWT compiler could optimized themes.
         */
        if (VEagerResourceLoader.isHighDPI()) {
            VTouchKitResources.INSTANCE.highDpiCss().ensureInjected();
        }
    }

}
