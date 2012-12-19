package com.vaadin.addon.touchkit.gwt.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.client.StyleConstants;

public class HorizontalButtonGroupWidget extends FlowPanel {

    public static final String TAGNAME = "horizontalbuttongroup";
    public static final String MARGINTAG = "margin";
    private static final String CLASSNAME = "v-touchkit-" + TAGNAME;
    public static final String CAPTION_CLASSNAME = "v-caption";

    public HorizontalButtonGroupWidget() {
        addStyleName(CLASSNAME);
    }

    /**
     * Sets CSS classes for margin based on the given parameters.
     * 
     * @param margins
     *            A {@link VMarginInfo} object that provides info on
     *            top/left/bottom/right margins
     */
    public void setMarginStyles(MarginInfo margins) {
        setStyleName(getElement(), CLASSNAME + "-" + StyleConstants.MARGIN_TOP,
                margins.hasTop());
        setStyleName(getElement(), CLASSNAME + "-"
                + StyleConstants.MARGIN_RIGHT, margins.hasRight());
        setStyleName(getElement(), CLASSNAME + "-"
                + StyleConstants.MARGIN_BOTTOM, margins.hasBottom());
        setStyleName(getElement(),
                CLASSNAME + "-" + StyleConstants.MARGIN_LEFT, margins.hasLeft());
    }
}
