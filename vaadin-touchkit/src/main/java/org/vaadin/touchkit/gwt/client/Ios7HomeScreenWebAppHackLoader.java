package org.vaadin.touchkit.gwt.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class Ios7HomeScreenWebAppHackLoader extends TouchKitPlatformHackLoader {

    private Integer bootWidth;
    private Integer bootHeight;

    /**
     * Adds workaround for ios7 home screen web apps, that have multiple issues
     * with "standard" viewport settings. Conditionally added ( deferred with n. 1 sec
     * delay) absolute pixel height (copied from window.innerHeight) for
     * "device-height" seems to work for most issue.
     * 
     * @see org.vaadin.touchkit.gwt.client.TouchKitPlatformHackLoader#load()
     */
    @Override
    public void load() {
        // Defer setting "boot values", else may be something weird in iphone
        // when app is started in landscape mode
        new Timer() {
            @Override
            public void run() {
                initSizeIfNeeded();
                /* "Somewhat" working viewport settings */
                addHeightToViewPort();

            }
        }.schedule(1000);

        // ... and set it each time size changes (most often orientation
        // change)
        Window.addResizeHandler(new ResizeHandler() {
            private Timer deferredResizeHandler;

            @Override
            public void onResize(ResizeEvent event) {
                if (deferredResizeHandler != null) {
                    deferredResizeHandler.cancel();
                }
                // Defer to get correct orientation, 1000 ms seems to be just
                // enough (tested on ipad mini)
                deferredResizeHandler = new Timer() {
                    @Override
                    public void run() {
                        if (!isVirtualKeyboardOn()) {
                            fixHtmlHeightToWindowInnerHeight();
                        } else {
                            deferredResizeHandler.schedule(1000);
                        }
                    }
                };
                deferredResizeHandler.schedule(1000);
            }
        });

    }

    private void initSizeIfNeeded() {
        if (bootWidth == null) {
            if (isLandscape()) {
                bootWidth = getWindowInnerHeight();
                bootHeight = getWindowInnerWidth();
            } else {
                bootHeight = getWindowInnerHeight();
                bootWidth = getWindowInnerWidth();
            }
            // log("BS" + bootWidth + " x " + bootHeight);
        }
    }

    private boolean isLandscape() {
        switch (getOrientation()) {
        case 0:
        case 180:
            return false;
        case 90:
        case -90:
        default:
            return true;
        }
    }

    private boolean isVirtualKeyboardOn() {
        int referenceHeight = isLandscape() ? bootWidth : bootHeight;
        int differeceToStart = Math.abs(referenceHeight
                - getWindowInnerHeight());
        // log("IVKBON il" + isLandscape() + " wh" + getWindowInnerHeight()
        // + " ww" + getWindowInnerWidth() + " bw" + bootWidth + " bh"
        // + bootHeight);
        // Allow small changes (~ status bar & e.g. hotspot notification)
        if (differeceToStart > 100) {
            return true;
        }
        return false;
    }

    /**
     * Modifies viewport tag to include both width=device-width AND
     * height=device-height. The latter is not generally known or used, but
     * seems to prevent ios from changing screen size when virtual keyboard pops
     * on. This is how it works in Safari, Android, mobile IE and in previous
     * version of iOS home screen web apps.
     * <p>
     * Instead of "device-height" we use pixel height reported by
     * window.innerHeight. On orientation changes that must be updated.
     */
    private void addHeightToViewPort() {
        MetaElement item = getViewportTag();
        if (item != null) {
            String attribute = item.getContent();
            if (!attribute.contains("width")) {
                attribute += ",width=device-width";
            }
            int viewPortHeight = getWindowInnerHeight();
            if (!attribute.contains("height")) {
                attribute += ",height=" + viewPortHeight;
            } else {
                attribute = updateViewPortHeight(attribute, viewPortHeight);
            }
            item.setContent(attribute);
        }
    }

    private MetaElement getViewportTag() {
        NodeList<Element> metas = Document.get().getElementsByTagName("meta");
        for (int i = 0; i < metas.getLength(); i++) {
            MetaElement item = metas.getItem(i).cast();
            if ("viewport".equals(item.getAttribute("name"))) {
                return item;
            }
        }
        return null;
    }

    private void fixHtmlHeightToWindowInnerHeight() {
        final MetaElement tag = getViewportTag();
        int viewPortHeight = getWindowInnerHeight();
        String c = updateViewPortHeight(tag.getContent(), viewPortHeight);
        tag.setContent(c);
    }

    private static final native String updateViewPortHeight(String s, int h)
    /*-{
        return s.replace(/,height=\w+/,",height=" + h);
    }-*/;;

    private static final native int getOrientation()
    /*-{
        return $wnd.orientation;
    }-*/;

    private static native int getWindowInnerHeight()
    /*-{
        return $wnd.innerHeight;
    }-*/;

    private static native int getWindowInnerWidth()
    /*-{
        return $wnd.innerWidth;
    }-*/;

}
