package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.DoNotEmbed;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.vaadin.addon.touchkit.gwt.client.theme.ParkingStyles;

public interface VTouchKitResources extends ClientBundle {
    public static final VTouchKitResources INSTANCE = GWT
            .create(VTouchKitResources.class);

    @Source({"theme/base.css","theme/parking.css"})
    public ParkingStyles css();
    
    @Source("theme/FontAwesome.woff")
    @DoNotEmbed
    DataResource fontAwesome();

    @Source("theme/FontAwesome.otf")
    @DoNotEmbed
    DataResource fontAwesomeOtf();

    @Source("theme/img/spinner.png")
    public DataResource spinnerImage();

    @Source("theme/img/linen-bg.png")
    public DataResource linenBgImage();

    @Source("theme/img/back-button.png")
    public DataResource backButtonImage();

    @Source("theme/img/back-button-2x.png")
    public DataResource backButtonImage2x();

    @Source("theme/img/back-button-popover.png")
    public DataResource popoverBackButtonImage();

    @Source("theme/img/back-button-popover-2x.png")
    public DataResource popoverBackButtonImage2x();

    @Source("theme/img/forward-button.png")
    public DataResource forwardButtonImage();

    @Source("theme/img/forward-button-2x.png")
    public DataResource forwardButtonImage2x();

    @Source("theme/img/forward-button-popover.png")
    public DataResource popoverForwardButtonImage();

    @Source("theme/img/forward-button-popover-2x.png")
    public DataResource popoverForwardButtonImage2x();

    /**
     * Arrow used in Navbuttons (right pointing gray arrow)
     */

    @Source("theme/img/nav-arrow.png")
    public DataResource navArrowImage();

    @Source("theme/img/nav-arrow-2x.png")
    public DataResource navArrowImage2x();

    @Source("theme/img/nav-arrow-white.png")
    public DataResource navArrowWhiteImage();

    @Source("theme/img/nav-arrow-white-2x.png")
    public DataResource navArrowWhiteImage2x();

    /**
     * Custom navbar icons for button
     */

    @Source("theme/img/navbar-arrow-up.png")
    public DataResource navbarArrowUpImage();

    @Source("theme/img/navbar-arrow-up-2x.png")
    public DataResource navbarArrowUpImage2x();

    @Source("theme/img/navbar-arrow-down.png")
    public DataResource navbarArrowDownImage();

    @Source("theme/img/navbar-arrow-down-2x.png")
    public DataResource navbarArrowDownImage2x();

    @Source("theme/img/navbar-arrow-left.png")
    public DataResource navbarArrowLeftImage();

    @Source("theme/img/navbar-arrow-left-2x.png")
    public DataResource navbarArrowLeftImage2x();

    @Source("theme/img/navbar-arrow-right.png")
    public DataResource navbarArrowRightImage();

    @Source("theme/img/navbar-arrow-right-2x.png")
    public DataResource navbarArrowRightImage2x();

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
     * Table
     */

    @Source("theme/img/asc-light.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource columnAscending();

    @Source("theme/img/desc-light.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource columnDescending();

    @Source("theme/img/col-sel-light.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource columnSelector();

    /*
     * ComboBox
     */
    @Source("theme/img/combobox-arrow-down-2x.png")
    public DataResource comboBoxArrowDown2x();

    @Source("theme/img/combobox-arrow-up-2x.png")
    public DataResource comboBoxArrowUp2x();

    /*
     * DateField
     */
    @Source("theme/img/calendar-2x.png")
    public DataResource calendar2x();

    /*
     * DatePicker
     */
    @Source("theme/img/clock.png")
    public DataResource clockImage();
    @Source("theme/img/clock-2x.png")
    public DataResource clockImage2x();

    /*
     * HiRes pictures based on base theme
     */
    @Source("theme/img/arrow-down.png")
    public DataResource arrowDownImage();

    @Source("theme/img/arrow-right.png")
    public DataResource arrowRightImage();

    @Source("theme/img/check.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource check();

    @Source("theme/img/tree/drag-slot-dot.png")
    public DataResource dragSlotDotImage();
    
}