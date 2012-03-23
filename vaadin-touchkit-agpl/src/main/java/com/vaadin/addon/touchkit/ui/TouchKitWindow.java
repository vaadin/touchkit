package com.vaadin.addon.touchkit.ui;

import java.util.LinkedList;
import java.util.Map;

import com.vaadin.addon.touchkit.server.TouchKitApplicationServlet;
import com.vaadin.addon.touchkit.service.ApplicationIcon;
import com.vaadin.addon.touchkit.service.Position;
import com.vaadin.addon.touchkit.service.PositionCallback;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;

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
public class TouchKitWindow extends Window {

    private static final String DEVICE_WIDTH = "device-width";
    private Boolean viewPortUserScalable = false;
    private Float viewPortInitialScale = 1f;
    private Float viewPortMinimumScale = 1f;
    private Float viewPortMaximumScale = 1f;
    private String viewPortWidth = DEVICE_WIDTH;
    private boolean webAppCapable = true;
    private String statusBarStyle;
    private String startupImage;
    private boolean persistentSessionCookie;

    private LinkedList<ApplicationIcon> applicationIcon = new LinkedList<ApplicationIcon>();
    private LinkedList<PositionCallback> positionCbs;
    private boolean goOffline;

    /**
     * Sets the webpage icon for this web app. This icon may be used by the
     * client OS in case user bookmarks the web page containing this window.
     * 
     * @param url
     * @see #addApplicationIcon(int, int, String)
     */
    public void addApplicationIcon(final String url) {
        applicationIcon.add(new ApplicationIcon() {

            public String getSizes() {
                return null;
            }

            public String getHref() {
                return url;
            }

            public boolean isPreComposed() {
                return false;
            }

        });
    }

    /**
     * Sets the application icon for this web app. This icon may be used by the
     * client OS in case user bookmarks the web page containing this window.
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param width
     * @param height
     * @param url
     */
    public void addApplicationIcon(final int width, final int height,
            final String url, final boolean preComposed) {
        applicationIcon.add(new ApplicationIcon() {

            public String getSizes() {
                return width + "x" + height;
            }

            public String getHref() {
                return url;
            }

            public boolean isPreComposed() {
                return preComposed;
            }
        });
    }

    /**
     * Sets whether the user should be allowed to zoom the content. The default
     * value for TouchKit apps is false, as we expect that applications are
     * designed for smaller/all devices.
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortUserScalable
     */
    public void setViewPortUserScalable(Boolean viewPortUserScalable) {
        this.viewPortUserScalable = viewPortUserScalable;
    }

    /**
     * @see #setViewPortUserScalable(Boolean)
     * 
     * @return true if the view port is user scalable
     */
    public Boolean isViewPortUserScalable() {
        return viewPortUserScalable;
    }

    /**
     * Sets the initial scale of the viewport.
     * 
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortInitialScale
     */
    public void setViewPortInitialScale(Float viewPortinitialScale) {
        viewPortInitialScale = viewPortinitialScale;
    }

    /**
     * 
     * @return the view port initial scale
     * @see #getViewPortInitialScale()
     */
    public Float getViewPortInitialScale() {
        return viewPortInitialScale;
    }

    /**
     * Sets the maximum scale allowed for the user.
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortmaximumScale
     */
    public void setViewPortMaximumScale(Float viewPortmaximumScale) {
        viewPortMaximumScale = viewPortmaximumScale;
    }

    /**
     * @return
     * @see #getViewPortMaximumScale()
     */
    public Float getViewPortMaximumScale() {
        return viewPortMaximumScale;
    }

    /**
     * Sets a header that tells the client whether the application is designed
     * to be used as a web application rather than a web page. If this is set to
     * true, the client may for instance hide the browsers own UI and give more
     * space for the web app. E.g. if app with this header is added to the home
     * screen on an iOS device, browser controls are hidden when the application
     * is started via the homescreen icon.
     * 
     * <p>
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param webAppCapable
     */
    public void setWebAppCapable(boolean webAppCapable) {
        this.webAppCapable = webAppCapable;
    }

    /**
     * @return
     * @see #setWebAppCapable(Boolean)
     */
    public boolean isWebAppCapable() {
        return webAppCapable;
    }

    /**
     * Some mobile devices (like Apple's iOS devices) may allow customizing
     * status bars appearances.
     * <p>
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param statusBarStyle
     */
    public void setStatusBarStyle(String statusBarStyle) {
        this.statusBarStyle = statusBarStyle;
    }

    /**
     * @return
     * @see #setStatusBarStyle(String)
     */
    public String getStatusBarStyle() {
        return statusBarStyle;
    }

    /**
     * Sets the image that the device may use as a place-holder while the web
     * application is starting up - e.g splash-screen or screenshot of initial
     * screen.
     * 
     * @param startupImage
     */
    public void setStartupImage(String startupImage) {
        this.startupImage = startupImage;
    }

    /**
     * @return
     * @see #setStartupImage(String)
     */
    public String getStartupImage() {
        return startupImage;
    }

    /**
     * Sets the viewport width into which the client browsers should render the
     * page. The value can be pixels or "device-width". The device width
     * constant is used as a default as we expect TouchKit Applications to be
     * designed for small devices. If the value is null, browsers try to figure
     * out a proper viewport width by themselves.
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortWidth
     */
    public void setViewPortWidth(String viewPortWidth) {
        this.viewPortWidth = viewPortWidth;
    }

    /**
     * @return
     * @see #setViewPortWidth(String)
     */
    public String getViewPortWidth() {
        return viewPortWidth;
    }

    /**
     * Sets the minimum scale allowed by the user.
     * 
     * @param viewPortMinimumScale
     */
    public void setViewPortMinimumScale(Float viewPortMinimumScale) {
        this.viewPortMinimumScale = viewPortMinimumScale;
    }

    /**
     * @return
     * @see #setViewPortMinimumScale(Float)
     */
    public Float getViewPortMinimumScale() {
        return viewPortMinimumScale;
    }

    /**
     * Gets the {@link ApplicationIcon}s that have previously been added to this
     * window with {@link #addApplicationIcon(String)} or
     * {@link #addApplicationIcon(int, int, String)}.
     * 
     * @return
     */
    public ApplicationIcon[] getApplicationIcons() {
        return applicationIcon.toArray(new ApplicationIcon[applicationIcon
                .size()]);
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

    @Override
    public synchronized void paintContent(PaintTarget target)
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

}
