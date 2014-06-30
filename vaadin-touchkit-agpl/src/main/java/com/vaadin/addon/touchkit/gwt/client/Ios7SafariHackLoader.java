package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;

public class Ios7SafariHackLoader extends TouchKitPlatformHackLoader {

    /*
     * Fixes iOS 7 viewport height issues by forcing the window to scroll to top
     * whenever the content gets displaced (#14123, #13749). See also
     * http://stackoverflow.com/questions/19012135/
     */
    private class ViewportHeightHack implements ScrollHandler {

        HandlerRegistration registration;

        @Override
        public void onWindowScroll(ScrollEvent event) {
            deactivate();
            new Timer() {
                @Override
                public void run() {
                    Window.scrollTo(0, 0);
                    activate();
                }
            }.schedule(200);
        }

        void activate() {
            registration = Window.addWindowScrollHandler(this);
        }

        void deactivate() {
            registration.removeHandler();
        }
    }

    @Override
    public void load() {
        new ViewportHeightHack().activate();
    }

}
