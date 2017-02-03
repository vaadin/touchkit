package org.vaadin.touchkit.settings;

import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.vaadin.touchkit.service.ApplicationIcon;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

/**
 * The ApplicationIcons class adds OS level icons (e.g. the iOS home screen
 * icon) for the application.
 * 
 * @see ApplicationIcon
 */
public class ApplicationIcons implements BootstrapListener {

    private LinkedList<ApplicationIcon> applicationIcon = new LinkedList<ApplicationIcon>();

    /**
     * Sets the web page icon for this web app. This icon may be used by the
     * client OS in case the user bookmarks the web page containing this window.
     * 
     * @param url
     *            the URL of the icon.
     * @see #addApplicationIcon(int, int, String, boolean)
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
     * See <a href=
     * "http://developer.apple.com/library/ios/#documentation/AppleApplications/Reference/SafariWebContent/ConfiguringWebApplications/ConfiguringWebApplications.html%23//apple_ref/doc/uid/TP40002051-CH3-SW4"
     * >Apple's developer documentation</a> for more details.
     * 
     * @param width
     *            the width of the icon
     * @param height
     *            the height of the icon
     * @param url
     *            the URL of the icon
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
     * Gets the {@link ApplicationIcon}s that have been added to this window
     * with {@link #addApplicationIcon(String)} or
     * {@link #addApplicationIcon(int, int, String, boolean)}.
     * 
     * @return the icons added to this window.
     */
    public ApplicationIcon[] getApplicationIcons() {
        return applicationIcon.toArray(new ApplicationIcon[applicationIcon
                .size()]);
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Document document = response.getDocument();
        Element head = document.getElementsByTag("head").get(0);

        for (ApplicationIcon icon : applicationIcon) {
            // <link rel="apple-touch-icon" sizes="114x114"
            // href="touch-icon-iphone4.png" />
            Element iconEl = document.createElement("link");
            iconEl.attr("rel", "apple-touch-icon");
            String sizes = icon.getSizes();
            if (sizes != null) {
                iconEl.attr("sizes", sizes);
            }
            iconEl.attr("href", icon.getHref());
            head.appendChild(iconEl);
        }
    }

}
