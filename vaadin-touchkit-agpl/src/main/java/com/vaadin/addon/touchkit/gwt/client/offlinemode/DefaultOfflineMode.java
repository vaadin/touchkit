package com.vaadin.addon.touchkit.gwt.client.offlinemode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.vaadin.client.ui.VNativeButton;
import com.vaadin.client.ui.VOverlay;

/**
 * This class is the default implementation of the offline mode used by Vaadin
 * TouchKit. It is displayed when the network connection is down or if the
 * server cannot be reached for some other reason.
 * <p>
 * Messages displayed by the default offline mode can be replaced by adding
 * customized properties files for the {@link OfflineModeMessages} bundle. See
 * <a href="https://developers.google.com/web-toolkit/doc/latest/DevGuideI18n">
 * the GWT i18n documentation</a> for more details.
 *
 */
public class DefaultOfflineMode implements OfflineMode {

    protected static final int Z_INDEX = 30001;
    private FlowPanel flowPanel;
    private VOverlay overlay;
    private boolean active;
    private OfflineModeMessages msg;
    private ActivationReason activationEvent;

    public DefaultOfflineMode() {
        active = false;
        flowPanel = new FlowPanel();
        overlay = new VOverlay();
        msg = GWT.create(OfflineModeMessages.class);

        overlay.addStyleName("v-window");
        overlay.addStyleName("v-touchkit-offlinemode");

        // Make sure that the outer and containers have 100% sizes
        overlay.getElement().getStyle().setWidth(100, Unit.PCT);
        overlay.getElement().getStyle().setHeight(100, Unit.PCT);
        overlay.setWidth("100%");
        overlay.setHeight("100%");

        // Make sure this is overloading the indicator
        overlay.getElement().getStyle().setZIndex(Z_INDEX);
        overlay.add(flowPanel);
    }

    /**
     * Returns the panel created by default activate function. Extended offline
     * modes can e.g. use this panel as their root.
     *
     * <p>
     * Note, that if the super method is not called in activate method, the
     * panel will not be created on null will be returned. Developers have then
     * full control over the offline mode behaviour.
     *
     * @return the panel created by default activate function.
     */
    public Panel getPanel() {
        return flowPanel;
    };

    @Override
    public void activate(ActivationReason event) {
        active = true;
        activationEvent = event;
        if (event == ActivationReason.APP_STARTING) {
            buildLoadingContent();
        } else if (event == ActivationReason.ONLINE_APP_NOT_STARTED) {
            buildReloadContent();
        } else {
            buildDefaultContent();
        }
        overlay.show();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return The activation message passed to us in the parameter for the
     *         {@link #activate(ActivationEvent)} method.
     */
    public String getActivationMessage() {
        return activationEvent.getMessage();
    }

    /**
     * This method is called by the default {@link #activate(ActivationEvent)}
     * implementation to build the contents of the offline mode each time it is
     * activated. The simplest method to customize offline mode is to override
     * this method and add a custom app to the panel returned by the
     * {@link #getPanel()} method.
     */
    protected void buildDefaultContent() {
        getPanel().clear();
        HTML h = new HTML("<div class=\"v-touchkit-sadface\">:-(</div><h1>"
                + msg.serverCannotBeReachedMsg() + "</h1><p>"
                + getActivationMessage() + "</p><div>"
                + msg.offlineDueToNetworkMsg() + "</div>");
        h.setStyleName("v-touchkit-offlinemode-panel");
        getPanel().add(h);
    }

    protected void buildLoadingContent() {
        getPanel().clear();
        HTML h = new HTML("<h1>Loading UI ...</h1>");
        h.setStyleName("v-touchkit-offlinemode-panel");
        getPanel().add(h);
    }

    /**
     * This method is called by the default {@link #deactivate()}
     * implementation to build the contents of this overlay when the
     * device goes online and the online app was not loaded previously.
     *
     * The simplest method to customize this view mode is to override
     * this method and add a custom app to the panel returned by the
     * {@link #getPanel()} method.
     */
    protected void buildReloadContent() {
        getPanel().clear();

        FlowPanel fp = new FlowPanel();
        getPanel().add(fp);

        fp.setStyleName("v-touchkit-offlinemode-panel");
        fp.add(new HTML("<h1>" + msg.networkBack() + "<h1>"));

        VNativeButton vButton = new VNativeButton();
        vButton.setText(msg.reload());
        vButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                overlay.hide();
                Location.reload();
            }
        });
        fp.add(vButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deactivate() {
        active = false;
        // Hide the floating overlay
        overlay.hide();
        return true;
    }
}
