package org.vaadin.touchkit.service;

import java.io.Serializable;

/**
 * The ApplicationIcon interface specifies the icon for the application. The
 * icon is used e.g. on the home screen on iOS if the application has been
 * bookmarked there.
 * 
 * See <a href=
 * "http://developer.apple.com/library/ios/#documentation/AppleApplications/Reference/SafariWebContent/ConfiguringWebApplications/ConfiguringWebApplications.html%23//apple_ref/doc/uid/TP40002051-CH3-SW4"
 * >Specifying a Webpage Icon for Web Clip</a> in Apple's developer
 * documentation for more details and naming conventions.
 */
public interface ApplicationIcon extends Serializable {

    /**
     * @return The value of the "sizes" attribute for the icon.
     */
    String getSizes();

    /**
     * @return The url of the icon.
     */
    String getHref();
}
