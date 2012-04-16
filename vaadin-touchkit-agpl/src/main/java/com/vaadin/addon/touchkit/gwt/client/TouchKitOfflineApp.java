package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.vaadin.terminal.gwt.client.ui.VButton;
import com.vaadin.terminal.gwt.client.ui.VOverlay;

/**
 * This class is the "default offline mode" used by Vaadin TouchKit. It is
 * displayed when network connection is down or if the server cannot be reached
 * for some other reason.
 * <p>
 * Applications that need a to have offline mode connect their offline mode
 * written with pure GWT to an extended version of this class. This class can be
 * replaced by adding following GWT deferred binding rule to your widgetset:
 * <code><pre>
        <replace-with
                class="com.example.widgetset.client.MyOfflineMode">
                <when-type-is
                        class="com.vaadin.addon.touchkit.gwt.client.TouchKitOfflineApp" />
        </replace-with>
 * </pre></code>
 * <p>
 * Messages displayed by the default "offline mode", the can replaced by adding
 * customized properties files for {@link OfflineModeMessages} bundle. See GWT
 * int18n docs for more details.
 * 
 */
public class TouchKitOfflineApp {

    protected static final int Z_INDEX = 30001;
    private VTouchKitApplicationConnection ac;
    private FlowPanel flowPanel;
    private VOverlay overlay;
    private String activationMessage;
    private int statusCode;
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

    /**
     * This method is called when the touchkit app desides to go offline.
     * <p>
     * By default it creates a simple overlay that is displayed over the
     * existing content and displays offline status for user. The content of the
     * overlay is built by {@link #buildDefaultContent()} method. Simplest
     * method to build custom offline app is to override that method instead of
     * this one. By overriding this method, developer can get full control on
     * what happens when offline mode gets triggered by the
     * ApplicationConnection.
     * 
     * @param details
     * @param statusCode
     */
    public void activate(String details, int statusCode) {
        activationMessage = details;
        this.statusCode = statusCode;
        overlay = new VOverlay();
        overlay.setStyleName("v-window v-touchkit-offlinemode");
        Style style = overlay.getElement().getStyle();
        style.setZIndex(Z_INDEX); // Make sure this is over loading indicator
        flowPanel = new FlowPanel();
        overlay.add(flowPanel);

        buildDefaultContent();

        overlay.show();
        overlay.setWidth(Window.getClientWidth() + "px");
        overlay.setHeight(Window.getClientHeight() + "px");
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public String getActivationMessage() {
        return activationMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * This method is called by the default {@link #activate(String, int)}
     * implementation to build the contents of the offline mode each time
     * offline mode is activated. Simplest method to customize offline mode is
     * to override this method and add custom app to panel returned by
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

        if (!VTouchKitApplicationConnection.isNetworkOnline()) {
            fp.add(new Label(msg.offlineDueToNetworkMsg()));
        } else {
            VButton vButton = new VButton();
            vButton.setText(msg.tryAgainMsg());
            vButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    overlay.hide();
                    ac.reload();
                }
            });
            fp.add(vButton);
        }
    }

    public void init(VTouchKitApplicationConnection mainApp) {
        ac = mainApp;
    }

    public VTouchKitApplicationConnection getApplicationConnection() {
        return ac;
    }

    /**
     * This method is called when the touch kit detects that now it might be
     * possible to get online again (e.g. network connection has returned). If
     * you have implemented a more advanced offline mode, override this method
     * and gracefully return to normal operation.
     */
    public void deactivate() {
        // Hide the floating overlay
        overlay.hide();
        active = false;
        // tell application it can resume its operations
        ac.resume();
    }

    /**
     * Called when the online app has successfully started. If the offline mode
     * has something to synchronize with the server it can do it here.
     */
    public void onlineApplicationStarted() {

    }

}
