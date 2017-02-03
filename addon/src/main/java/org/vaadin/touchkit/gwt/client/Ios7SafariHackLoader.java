package org.vaadin.touchkit.gwt.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.LayoutManager;

public class Ios7SafariHackLoader extends TouchKitPlatformHackLoader {

    /*
     * Fixes iOS 7 viewport height issues by applying inline styles and forcing
     * the window to scroll to top whenever the content gets displaced (#14123,
     * #13749). See also http://stackoverflow.com/questions/19012135/
     */
    private static final class ViewportHeightHack implements RepeatingCommand,
            ScrollHandler {

        enum State {
            STOPPED, POLLING, ADJUSTING;
        }

        int height = 0;
        State state = State.STOPPED;
        boolean onScreenKeyboardMightBeVisible = false;
        int repetitions = 0;
        boolean ignoreNextScrollEvent = false;
        final Style bodyStyle = Document.get().getBody().getStyle();
        final boolean iPad = Window.Navigator.getUserAgent().toLowerCase()
                .contains("ipad");

        void onOrientationChange() {
            bodyStyle.clearHeight();
            bodyStyle.clearPosition();
            forceLayout();
            if (deviceInLandscapeMode()) {
                height = iPad ? 0 : getWindowHeight();
                schedule(1);
            }
        }

        @Override
        public void onWindowScroll(ScrollEvent event) {
            if (ignoreNextScrollEvent) {
                ignoreNextScrollEvent = false;
            } else if (deviceInLandscapeMode() && state == State.STOPPED
                    && height != getWindowHeight()) {
                onScreenKeyboardMightBeVisible &= Document.get()
                        .getDocumentElement().getOffsetHeight() != getWindowHeight();
                if (!onScreenKeyboardMightBeVisible) {
                    schedule(2);
                }
            }
        }

        void onFocusIn() {
            onScreenKeyboardMightBeVisible = true;
        }

        void onFocusOut() {
            onScreenKeyboardMightBeVisible = false;
        }

        @Override
        public boolean execute() {
            onScreenKeyboardMightBeVisible &= Document.get()
                    .getDocumentElement().getOffsetHeight() != getWindowHeight();
            if (onScreenKeyboardMightBeVisible && !iPad) {
                return true;
            }
            if (state == State.ADJUSTING) {
                Window.scrollTo(0, 0);
                ignoreNextScrollEvent = true;
            }
            if (height != getWindowHeight()) {
                height = getWindowHeight();
                bodyStyle.setHeight(height, Unit.PX);
                bodyStyle.setPosition(Position.FIXED);
                state = State.ADJUSTING;
            } else if (state == State.ADJUSTING) {
                state = State.STOPPED;
                forceLayout();
                if (repetitions > 0) {
                    schedule(repetitions);
                }
                return false;
            }
            return true;
        }

        void forceLayout() {
            for (ApplicationConnection app : ApplicationConfiguration
                    .getRunningApplications()) {
                LayoutManager.get(app).forceLayout();
            }
        }

        void schedule(int times) {
            repetitions = times - 1;
            if (state == State.STOPPED) {
                Scheduler.get().scheduleFixedPeriod(this, 200);
            }
            state = State.POLLING;
        }

        native boolean deviceInLandscapeMode()
        /*-{
            return Math.abs($wnd.orientation) === 90;
        }-*/;

        native void attachNativeEventListeners()
        /*-{
            var that = this;
            $wnd.addEventListener('orientationchange', function () {
                that.@org.vaadin.touchkit.gwt.client.Ios7SafariHackLoader.ViewportHeightHack::onOrientationChange()();
            });
            $doc.addEventListener('focusin', function () {
                that.@org.vaadin.touchkit.gwt.client.Ios7SafariHackLoader.ViewportHeightHack::onFocusIn()();
            });
            $doc.addEventListener('focusout', function () {
                that.@org.vaadin.touchkit.gwt.client.Ios7SafariHackLoader.ViewportHeightHack::onFocusOut()();
            });
        }-*/;

        native int getWindowHeight()
        /*-{
            return $wnd.innerHeight;
        }-*/;

        void activate() {
            if (deviceInLandscapeMode()) {
                state = State.ADJUSTING;
                execute();
                state = State.STOPPED;
            }
            attachNativeEventListeners();
            Window.addWindowScrollHandler(this);
        }
    }

    @Override
    public void load() {
        new ViewportHeightHack().activate();
    }

}
