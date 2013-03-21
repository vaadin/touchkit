package com.vaadin.addon.touchkit.gwt.client.offlinemode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.vaadin.addon.touchkit.gwt.client.vcom.OfflineModeConnector;
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
    private String activationMessage;
    private boolean active;
    private OfflineModeMessages msg;

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
    public void activate(ActivationEvent event) {
        active = true;
        activationMessage = event.getActivationMessage();
        overlay = new VOverlay();
        overlay.addStyleName("v-window");
        overlay.addStyleName("v-touchkit-offlinemode");
        Style style = overlay.getElement().getStyle();
        style.setZIndex(Z_INDEX); // Make sure this is overloading the indicator
        flowPanel = new FlowPanel();
        overlay.add(flowPanel);

        buildDefaultContent();

        overlay.show();
        overlay.setWidth(Window.getClientWidth() + "px");
        overlay.setHeight(Window.getClientHeight() + "px");
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
        return activationMessage;
    }

    /**
     * This method is called by the default {@link #activate(ActivationEvent)}
     * implementation to build the contents of the offline mode each time it is
     * activated. The simplest method to customize offline mode is to override
     * this method and add a custom app to the panel returned by the
     * {@link #getPanel()} method.
     */
    protected void buildDefaultContent() {
        msg = GWT.create(OfflineModeMessages.class);

        FlowPanel fp = new FlowPanel();
        getPanel().add(fp);
        fp.setStyleName("v-touchkit-offlinemode-panel");
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"v-touchkit-sadface\">:-(</div>");
        sb.append("<h1>");
        sb.append(msg.serverCannotBeReachedMsg());
        sb.append("</h1>");
        sb.append("<p>");
        sb.append(getActivationMessage());
        sb.append("</p>");

        fp.add(new HTML(sb.toString()));

        if (!OfflineModeConnector.isNetworkOnline()) {
            fp.add(new Label(msg.offlineDueToNetworkMsg()));
        } else {
            VNativeButton vButton = new VNativeButton();
            vButton.setText(msg.tryAgainMsg());
            vButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    overlay.hide();
                    Location.reload();
                }
            });
            fp.add(vButton);
        }
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
