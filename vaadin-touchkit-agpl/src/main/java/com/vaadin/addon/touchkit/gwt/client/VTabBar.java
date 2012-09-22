package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.TouchScrollDelegate;

public class VTabBar extends ComplexPanel {

    private static final String CLASSNAME = "v-touchkit-tabbar";
    private Element wrapper = Document.get().createDivElement().cast();
    private Element toolbarDiv = Document.get().createDivElement().cast();

    private Widget content;
    private Widget toolbar;

    public VTabBar() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
        wrapper.setClassName(CLASSNAME + "-wrapper");
        getElement().appendChild(wrapper);
        toolbarDiv.setClassName(CLASSNAME + "-toolbar");
        getElement().appendChild(toolbarDiv);

        /*
         * Touch scrolling using one finger handled by TouchScrollDelegate.
         */
        sinkEvents(Event.TOUCHEVENTS);
        final TouchScrollDelegate touchScrollDelegate = new TouchScrollDelegate(
                wrapper);
        addHandler(new TouchStartHandler() {
            @Override
            public void onTouchStart(TouchStartEvent event) {
                touchScrollDelegate.onTouchStart(event);
            }
        }, TouchStartEvent.getType());
    }

    public void setToolbar(Widget widget) {
        if (toolbar == widget) {
            return;
        }
        if (toolbar != null) {
            toolbar.removeFromParent();
        }
        add(widget, toolbarDiv);
        toolbar = widget;
    }

    public void setContent(Widget widget) {
        if (content == widget) {
            return;
        }
        if (content != null) {
            content.removeFromParent();
        }
        add(widget, wrapper);
        content = widget;
    }

}
