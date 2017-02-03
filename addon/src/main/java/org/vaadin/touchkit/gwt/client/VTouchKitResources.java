package org.vaadin.touchkit.gwt.client;

import org.vaadin.touchkit.gwt.client.theme.TouchKitStyles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface VTouchKitResources extends ClientBundle {
    public static final VTouchKitResources INSTANCE = GWT
            .create(VTouchKitResources.class);

    @Source({ "theme/base.css", "theme/icon-defs.css", "theme/touchkit.css" })
    public TouchKitStyles css();

    /*
     * Popover images
     */
    @Source("theme/img/popover-arrow-parking.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource popoverArrowImage();

    @Source("theme/img/popover-arrow-parking-down.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource popoverArrowDownImage();

    /*
     * HiRes pictures based on base theme
     */
    @Source("theme/img/drag-slot-dot.png")
    public DataResource dragSlotDotImage();

    /*
     * Loading indicator icon
     */
    @Source("theme/fonts/fontawesome-webfont-refresh.woff")
    public DataResource faIconRefreshWoff();

    @Source("theme/fonts/fontawesome-webfont-refresh.ttf")
    public DataResource faIconRefreshTtf();

}