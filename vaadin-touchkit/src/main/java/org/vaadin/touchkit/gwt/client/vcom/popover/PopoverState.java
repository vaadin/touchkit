package org.vaadin.touchkit.gwt.client.vcom.popover;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.window.WindowState;

@SuppressWarnings("serial")
public class PopoverState extends WindowState {
    private boolean fullscreen;
    private Connector relatedComponent;

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public Connector getRelatedComponent() {
        return relatedComponent;
    }

    public void setRelatedComponent(Connector relativeComponent) {
        this.relatedComponent = relativeComponent;
    }
}
