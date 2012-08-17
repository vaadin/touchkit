package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.vaadin.shared.ComponentState;

public class NavigationViewState extends ComponentState {
    private int scrollPosition;

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }
}
