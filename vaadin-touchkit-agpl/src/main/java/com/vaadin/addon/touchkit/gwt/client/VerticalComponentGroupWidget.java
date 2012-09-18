package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.navigation.VNavigationButton;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.button.VButton;
import com.vaadin.client.ui.link.VLink;

public class VerticalComponentGroupWidget extends FlowPanel {

    public static final String TAGNAME = "verticalcomponentgroup";
    private static final String CLASSNAME = "v-touchkit-" + TAGNAME;
    public static final String CAPTION_CLASSNAME = "v-caption";

    public VerticalComponentGroupWidget() {
    	
    	setStyleName(CLASSNAME);
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
        if (iconUrl != null
                && !iconUrl.isEmpty()
                && !(widget instanceof VButton || widget instanceof VNavigationButton)) {
            IconWidget newIcon = new IconWidget(iconUrl);
            getElement().insertFirst(newIcon.getElement());
        }

        FlowPanel row = new FlowPanel();
        SimplePanel widgetWrapper = new SimplePanel();

        row.addStyleName("v-touchkit-componentgroup-row");
        widgetWrapper
                .addStyleName("v-touchkit-componentgroup-cell-wrapper");

        if (captionText == null || captionText.isEmpty()) {
        	widgetWrapper.addStyleName("v-touchkit-componentgroup-cell-fullwrapper");
        } else {
        	row.addStyleName("v-touchkit-componentgroup-rowcap");
            HTML caption = new HTML(captionText);
            caption.setStyleName(CAPTION_CLASSNAME);
            caption.addStyleName("v-touchkit-componentgroup-cell-caption");
            row.add(caption);
        }

        widgetWrapper.add(widget);

        row.add(widgetWrapper);
        add(row);
    }
}
