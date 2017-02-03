package org.vaadin.touchkit.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class VEagerResourceLoader implements EntryPoint {

    private static final String TOUCHKIT_STYLE_NAME = "v-tk";

    public void onModuleLoad() {
        TouchKitPlatformHackLoader platformHacks = GWT.create(TouchKitPlatformHackLoader.class);
        platformHacks.load();
        ThemeLoader themeLoader = GWT.create(ThemeLoader.class);
        themeLoader.load();
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
