package com.vaadin.addon.touchkit.ui;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

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

    public TouchKitApplication() {
        setTheme(BaseTheme.THEME_NAME);
    }

    @Override
    public void init() {
        setMainWindow(new TouchKitWindow());
    }

    /**
     * Gets the active application instance for this thread. Allows one to do
     * <code>MyTouchKitApplication.get()</code> to get the application instance
     * anywhere in the UI code, provided we're in the request/response thread
     * (using the ThreadLocal pattern).
     * 
     * @return the active application instance
     */
    public static TouchKitApplication get() {
        return activeApplication.get();
    }

    /**
     * @see WebApplicationContext#getBrowser()
     */
    public WebBrowser getBrowser() {
        return ((WebApplicationContext) getContext()).getBrowser();
    }

    /**
     * UI building should happen when this method is called. At this point all
     * details in {@link WebBrowser} ({@link #getBrowser()}) is available - i.e
     * the type of device and screen size is known, allowing for decisions about
     * which type of UI to show. A {@link TouchKitWindow} is set as the main
     * window.
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

    /**
     * Performs two tasks:
     * <ul>
     * <li>Waits for browser details to become available, then calls
     * {@link #onBrowserDetailsReady()}.</li>
     * <li>Sets the active application instance for this thread, allowing it to
     * be fetched within this reqest/response (or thread, more specifically)
     * using {@link TouchKitApplication#get()} (ThreadLocal pattern)</li>
     * </ul>
     * 
     * @see com.vaadin.terminal.gwt.server.HttpServletRequestListener#onRequestStart(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
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
                browserDetailsReady = true;
                if(isRunning()) {
                	onBrowserDetailsReady();
                }
            }
        }
    }
    
    @Override
    public void start(URL applicationUrl, Properties applicationProperties,
    		ApplicationContext context) {
    	super.start(applicationUrl, applicationProperties, context);
    	if(browserDetailsReady) {
    		onBrowserDetailsReady();
    	}
    }

    /**
     * Unsets the active application instance for this thread before the
     * request/response ends (ThreadLocal pattern)
     * 
     * @see com.vaadin.terminal.gwt.server.HttpServletRequestListener#onRequestEnd(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void onRequestEnd(HttpServletRequest request,
            HttpServletResponse response) {
        activeApplication.set(null);
    }

}
