package com.vaadin.addon.touchkit.itest;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * TODO remove as similar stuff can now be achieved with Roots?
 * 
 */
public class FallbackApplication extends Application.LegacyApplication {

    @Override
    public void init() {
        Window window = new Window("Fallback app");
        window.addComponent(new Label(
                "Your browser is not supported by this application. You'll instead be shown this fallback application. Use webkit based application to test TouchKit"));
        // setMainWindow(window);

    }

}
