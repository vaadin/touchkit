package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class VEagerResourceLoader implements EntryPoint {

    private static final String TOUCHKIT_STYLE_NAME = "v-tk";

    public void onModuleLoad() {
        VTouchKitResources.INSTANCE.css().ensureInjected();
        if (isHighDPI()) {
            VTouchKitResources.INSTANCE.highDpiCss().ensureInjected();
        }
        RootPanel.getBodyElement().addClassName(TOUCHKIT_STYLE_NAME);
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
