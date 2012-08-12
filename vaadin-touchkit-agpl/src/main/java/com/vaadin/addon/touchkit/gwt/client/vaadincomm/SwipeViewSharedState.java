package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.ui.csslayout.CssLayoutState;

public class SwipeViewSharedState extends CssLayoutState {
    
    private int scrollTop;

    public int getScrollTop() {
        return scrollTop;
    }

    public void setScrollTop(int scrollTop) {
        this.scrollTop = scrollTop;
    }

}
