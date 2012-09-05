package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.navigation.VNavigationButton;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.button.VButton;

public class VerticalComponentGroupWidget extends FlowPanel {

    public static final String TAGNAME = "verticalcomponentgroup";
    private static final String CLASSNAME = "v-touchkit-" + TAGNAME;
    public static final String CAPTION_CLASSNAME = "v-caption";

    private boolean firstElement = true;
    private FlowPanel content = new FlowPanel();

    public VerticalComponentGroupWidget() {
        content.addStyleName(CLASSNAME);

        UIObject.setStyleName(getElement(), TAGNAME + "-"
                + StyleConstants.MARGIN_TOP, true);
        UIObject.setStyleName(getElement(), TAGNAME + "-"
                + StyleConstants.MARGIN_RIGHT, true);
        UIObject.setStyleName(getElement(), TAGNAME + "-"
                + StyleConstants.MARGIN_BOTTOM, true);
        UIObject.setStyleName(getElement(), TAGNAME + "-"
                + StyleConstants.MARGIN_LEFT, true);
    }

    /**
     * Adds Widget with icon url and caption text
     * 
     * @param widget
     * @param icon
     * @param caption
     */
    public void add(final Widget widget, final String iconUrl,
            final String captionText, String widgetWidth) {
        if (firstElement) {
            firstElement = false;
            add(content);
        }
        if (iconUrl != null
                && !iconUrl.isEmpty()
                && !(widget instanceof VButton || widget instanceof VNavigationButton)) {
            IconWidget newIcon = new IconWidget(iconUrl);
            getElement().insertFirst(newIcon.getElement());
        }
        if (captionText != null
                && !captionText.isEmpty()
                && !(widget instanceof VButton
                        || widget instanceof VNavigationButton || widget instanceof VSwitch)) {

            FlowPanel table = new FlowPanel();
            table.addStyleName("v-touchkit-componentgroup-table");

            HTML caption = new HTML(captionText);
            caption.setStyleName(CAPTION_CLASSNAME);
            caption.addStyleName("v-touchkit-componentgroup-cell-caption");
            widget.addStyleName("v-touchkit-componentgroup-cell-left");

            table.add(caption);
            table.add(widget);
            content.add(table);
        } else if (widget instanceof VSwitch) {
            HTML caption = new HTML(captionText);
            caption.setStyleName(CAPTION_CLASSNAME);
            widget.addStyleName("v-touchkit-has-caption");
            content.add(caption);
            content.add(widget);
        } else {
            content.add(widget);
        }
    }
}
