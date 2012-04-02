package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface VTouchKitResources extends ClientBundle {
    public static final VTouchKitResources INSTANCE = GWT
            .create(VTouchKitResources.class);

    @NotStrict
    @Source("theme/touchkit.css")
    public CssResource css();

    @NotStrict
    @Source("theme/high-dpi.css")
    public CssResource highDpiCss();

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

    @Source("theme/img/popover-arrow.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource popoverArrowImage();

    @Source("theme/img/popover-arrow-down.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource popoverArrowDownImage();

    /*
     * Switch
     */

    @Source("theme/img/switch.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public DataResource switchImage();

    @Source("theme/img/switch-2x.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public DataResource switchImage2x();

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

}