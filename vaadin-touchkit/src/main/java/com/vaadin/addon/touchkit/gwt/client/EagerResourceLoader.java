package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.EntryPoint;

public class EagerResourceLoader implements EntryPoint {

    public void onModuleLoad() {
        TouchKitResources.INSTANCE.css().ensureInjected();
        if (isHighDPI()) {
            TouchKitResources.INSTANCE.highDpiCss().ensureInjected();
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
