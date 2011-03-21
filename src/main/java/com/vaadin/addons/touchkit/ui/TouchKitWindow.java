package com.vaadin.addons.touchkit.ui;

import java.util.LinkedList;

import com.vaadin.addons.touchkit.server.TouchKitApplicationServlet;
import com.vaadin.ui.Window;

/**
 * Special window implementation that supports various mobile settings like
 * defining viewport. For these settings to work, one must use
 * {@link TouchKitApplicationServlet} instead of the std Vaadin servlet.
 * 
 * TODO features
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

	private LinkedList<ApplicationIcon> applicationIcon = new LinkedList<ApplicationIcon>();

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
		});
	}

	/**
	 * Sets the webpage icon for this web app. This icon may be used by the
	 * client OS in case user bookmarks the web page containing this window.
	 * <p>
	 * 
	 * See {@link http://developer.apple.com/library/safari/} for more details.
	 * 
	 * @param widht
	 * @param height
	 * @param url
	 */
	public void addApplicationIcon(final int widht, final int height,
			final String url) {
		applicationIcon.add(new ApplicationIcon() {

			public String getSizes() {
				return widht + "x" + height;
			}

			public String getHref() {
				return url;
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
	 * @return
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
	 * @return
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
	 * Sets a header that tells the client wheter the application is designed to
	 * be used as a web application rather than a web page. If this is set to
	 * true, the client may e.g. hide browsers own UI and give more space for
	 * the web app. E.g. if app with this header is added to home screen, IOS
	 * devices hide browser chrome when application is accessed via homescreen
	 * button.
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
	 * Some mobile devices (like Apple's ios) may allow customizing status bars
	 * appearances.
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
	 * Sets the image that the device may use as a placeholder while the web
	 * application is starting up.
	 * 
	 * @param startupImage
	 */
	public void setStartupImage(String startupImage) {
		this.startupImage = startupImage;
	}

	/**
	 * 
	 * 
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

	public ApplicationIcon[] getApplicationIcons() {
		return applicationIcon.toArray(new ApplicationIcon[applicationIcon
				.size()]);
	}

}
