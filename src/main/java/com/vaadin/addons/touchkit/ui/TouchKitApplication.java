package com.vaadin.addons.touchkit.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Window;

/**
 * TouchKitApplication is a specialized {@link Application} implementation to
 * help building Vaadin applications designed specifically for various touch
 * devices. It provides quick access to the {@link WebBrowser} object via
 * {@link #getBrowser()} and adds a specific method to do real application setup
 * in a phase when all information in WebBrowser is available via
 * {@link #onBrowserDetailsReady()}.
 * <p>
 * Helper also provides static getter method for TouchKitApplication a.k.a
 * thread local pattern.
 * <p>
 * The standard Vaadin application init process calls the {@link #init()} method
 * very early. In that phase data all browser details are not necessary known by
 * the server side, so instead of overriding the init method as usual,
 * developers should override {@link #onBrowserDetailsReady()} method in case
 * their UI depends on details detected via {@link WebBrowser}. The init method
 * implementation sets a {@link TouchKitWindow} as main window and does the
 * required magic to call {@link #onBrowserDetailsReady()} when JavaScript
 * detected details from the client have been received. On that phase the main
 * window also has window width and height set according to the data provided by
 * the client side code.
 * <p>
 * Note, that only {@link TouchKitWindow}s are supported as top level windows.
 */
@SuppressWarnings("serial")
public abstract class TouchKitApplication extends Application implements
        HttpServletRequestListener {

    private boolean browserDetailsReady = false;

    private static ThreadLocal<TouchKitApplication> activeApplication = new ThreadLocal<TouchKitApplication>();

    @Override
    public void init() {
        setMainWindow(new TouchKitWindow());
    }

    public static TouchKitApplication get() {
        return activeApplication.get();
    }

    public WebBrowser getBrowser() {
        return ((WebApplicationContext) getContext()).getBrowser();
    }

    /**
     * UI building should happen when this method is called. At this point all
     * details in WebBrowser is available. A {@link TouchKitWindow} is set as
     * the main window.
     */
    public abstract void onBrowserDetailsReady();

    @Override
    public void setMainWindow(Window mainWindow) {
        if (mainWindow instanceof TouchKitWindow) {
            super.setMainWindow(mainWindow);
        } else {
            throw new IllegalArgumentException("Only "
                    + TouchKitWindow.class.getSimpleName()
                    + " can be set as main window");
        }
    }

    @Override
    public TouchKitWindow getMainWindow() {
        return (TouchKitWindow) super.getMainWindow();
    }

    @Override
    public TouchKitWindow getWindow(String name) {
        return (TouchKitWindow) super.getWindow(name);
    }

    public void onRequestStart(HttpServletRequest request,
            HttpServletResponse response) {

        activeApplication.set(this);

        if (!browserDetailsReady) {
            if (request.getParameter("repaintAll") != null) {
                int viewWidth = Integer.parseInt(request.getParameter("vw"));
                int viewHeight = Integer.parseInt(request.getParameter("vh"));
                Collection<Window> windows2 = getWindows();
                for (Iterator<Window> iterator = windows2.iterator(); iterator
                        .hasNext();) {
                    TouchKitWindow window = (TouchKitWindow) iterator.next();
                    window.setWidth(viewWidth, TouchKitWindow.UNITS_PIXELS);
                    window.setHeight(viewHeight, TouchKitWindow.UNITS_PIXELS);
                }
                onBrowserDetailsReady();
                browserDetailsReady = true;
            }
        }
    }

    public void onRequestEnd(HttpServletRequest request,
            HttpServletResponse response) {
        activeApplication.set(null);
    }

}
