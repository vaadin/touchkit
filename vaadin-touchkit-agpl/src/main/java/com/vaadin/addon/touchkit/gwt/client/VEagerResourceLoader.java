package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.EntryPoint;

public class VEagerResourceLoader implements EntryPoint {

    public void onModuleLoad() {
        VTouchKitResources.INSTANCE.css().ensureInjected();
        if (isHighDPI()) {
            VTouchKitResources.INSTANCE.highDpiCss().ensureInjected();
        }
    }

    public static native boolean isHighDPI()
    /*-{
        if (window.devicePixelRatio >= 2) {
            return true;
        } else {
            return false;
        }
    }-*/;

}
