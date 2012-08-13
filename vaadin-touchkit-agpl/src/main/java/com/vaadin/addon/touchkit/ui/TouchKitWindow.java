package com.vaadin.addon.touchkit.ui;

import java.util.LinkedList;
import java.util.Map;

import com.vaadin.addon.touchkit.server.TouchKitApplicationServlet;
import com.vaadin.addon.touchkit.service.Position;
import com.vaadin.addon.touchkit.service.PositionCallback;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Root;

/**
 * Special window implementation that supports various mobile settings like
 * defining a viewport. For these settings to work,
 * {@link TouchKitApplicationServlet} must be used instead of the standard
 * Vaadin servlet.
 * <p>
 * Contains features for configuring the viewport in various ways, configuring
 * how the application interacts with the device, adding application icons (e.g
 * used to represent the application when it has been added to a homescreen),
 * setting a startup image ('splash-screen') and detecting the current
 * geographical location of the device.
 */
public abstract class TouchKitWindow extends Root {

    private boolean persistentSessionCookie;

    private LinkedList<PositionCallback> positionCbs;
    private boolean goOffline;
    private int offlineTimeout = 10;

    public TouchKitWindow() {
    }

    /**
     * This method is used to detect the current geographic position of the
     * client. The detection happens asynchronously and the position is reported
     * to the callback given as argument.
     * 
     * @param positionCallback
     */
    public void detectCurrentPosition(PositionCallback positionCallback) {
        if (positionCbs == null) {
            positionCbs = new LinkedList<PositionCallback>();
        }
        positionCbs.add(positionCallback);
        requestRepaint();
    }

    /**
     * Old paintcontent method, to be removed.
     * @param target
     * @throws PaintException
     */
    public synchronized void oldPaintContent(PaintTarget target)
            throws PaintException {
        super.paintContent(target);
        if (positionCbs != null && !positionCbs.isEmpty()) {
            target.addAttribute("geoloc", true);
        }
        if (isPersistentSessionCookie()) {
            WebApplicationContext context = (WebApplicationContext) getApplication()
                    .getContext();
            int maxInactiveInterval = context.getHttpSession()
                    .getMaxInactiveInterval();
            target.addAttribute("persistSession", maxInactiveInterval);
        }
        if (goOffline) {
            target.addAttribute("goOffline", true);
            goOffline = false;
        }
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        if (variables.containsKey("position")) {
            for (PositionCallback cb : positionCbs) {
                cb.onSuccess(new Position((String) variables.get("position")));
            }
            positionCbs.clear();
        } else if (variables.containsKey("positionError")) {
            for (PositionCallback cb : positionCbs) {
                Integer errorCode = (Integer) variables.get("positionError");
                cb.onFailure(errorCode.intValue());
            }
            positionCbs.clear();
        }
    }

    /**
     * Vaadin uses the servlet's session mechanism to track users. With its
     * default settings all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps in iOS
     * devices that are added to home screen) this might not be the desired
     * solution. With this method the session cookie can be made persistent. A
     * returning user can then be shown his/her previous UI state.
     * <p>
     * 
     * Note, that the normal session lifetime is still respected although
     * persistent cookies are in use.
     * 
     * @param persistentCookie
     *            true if persistent session cookies should be used
     */
    public void setPersistentSessionCookie(boolean persistentCookie) {
        persistentSessionCookie = persistentCookie;
        requestRepaint();
    }

    /**
     * Vaadin uses the servlet's session mechanism to track users. With its
     * default settings all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps in iOS
     * devices that are added to home screen) this might not be the desired
     * solution.
     * 
     * @return true if session cookie will be made persistent when closing the
     *         browser application
     */
    public boolean isPersistentSessionCookie() {
        return persistentSessionCookie;
    }

    /**
     * Instructs the client side to get into offline mode. Can be used
     * beforehand if e.g. the user knows he is about to lose his network
     * connection.
     */
    public void goOffline() {
        goOffline = true;
        requestRepaint();
    }

    /**
     * Returns the amount of seconds the client side waits request from the
     * server until it opens the offline mode. -1 means "offline mode" is
     * completely disabled on slow connection.
     */
    public int getOfflineTimeout() {
        return offlineTimeout;
    }

    /**
     * Sets the timeout (in seconds) how long the client side waits for server
     * to respond before falling back to "offline mode". If the value is set for
     * -1, the client side waits forever.
     * 
     * @param offlineTimeout
     *            timeout in seconds
     */
    public void setOfflineTimeout(int offlineTimeout) {
        this.offlineTimeout = offlineTimeout;
    }

}
