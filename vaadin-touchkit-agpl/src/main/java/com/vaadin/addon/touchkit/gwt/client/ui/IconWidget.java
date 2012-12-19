package com.vaadin.addon.touchkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class IconWidget extends Widget {

    public static final String CLASSNAME = "v-icon";

    public IconWidget(String iconUrl) {
        setElement(DOM.createImg());
        DOM.setElementProperty(getElement(), "alt", "");
        DOM.setElementProperty(getElement(), "src", iconUrl);
        setStyleName(CLASSNAME);
    }
}
