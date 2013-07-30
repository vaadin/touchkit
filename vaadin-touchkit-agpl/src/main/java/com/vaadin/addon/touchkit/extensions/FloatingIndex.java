package com.vaadin.addon.touchkit.extensions;

import com.vaadin.addon.touchkit.gwt.client.vcom.FloatingIndexSharedState;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Component;

public class FloatingIndex extends AbstractExtension {
    
    public void addTo(NavigationView view) {
        extend(view);
    }
    
    public void map(String key, Component component) {
        getState().map(key, component);
    }
    
    @Override
    protected FloatingIndexSharedState getState() {
        return (FloatingIndexSharedState) super.getState();
    }

}
