package com.vaadin.addon.touchkit.gwt.client;

import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ui.TouchScrollDelegate;

public class VTabBar extends ComplexPanel implements Container {

    private static final String CLASSNAME = "v-touchkit-tabbar";
    private static final int TOOLBARHEIGHT = 46;
    private Element wrapper = Document.get().createDivElement().cast();
    private Element toolbarDiv = Document.get().createDivElement().cast();

    private Paintable content;
    private Paintable toolbar;
    private ApplicationConnection client;
    private boolean rendering;

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
            public void onTouchStart(TouchStartEvent event) {
                touchScrollDelegate.onTouchStart(event);
            }
        }, TouchStartEvent.getType());
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

        if (client.updateComponent(this, uidl, false)) {
            return;
        }
        rendering = true;
        this.client = client;

        // toolbar
        UIDL toolbaruidl = uidl.getChildUIDL(0);
        Paintable paintable2 = client.getPaintable(toolbaruidl);
        if (toolbar != null && toolbar != paintable2) {
            forgetComponent(client, toolbar);
        }
        toolbar = paintable2;
        if (!((Widget) toolbar).isAttached()) {
            add((Widget) toolbar, toolbarDiv);
        }
        toolbar.updateFromUIDL(toolbaruidl, client);

        // and we always have content in second slot
        UIDL childUIDL = uidl.getChildUIDL(1);
        Paintable paintable = client.getPaintable(childUIDL);
        if (content != null && content != paintable) {
            forgetComponent(client, content);
        }
        content = paintable;
        if (!((Widget) content).isAttached()) {
            add((Widget) content, wrapper);
        }
        content.updateFromUIDL(childUIDL, client);

        Util.runWebkitOverflowAutoFix(wrapper);
        rendering = false;
    }

    private void forgetComponent(ApplicationConnection client,
            Paintable content2) {
        ((Widget) content2).removeFromParent();
        client.unregisterPaintable(content2);
        if (content == content2) {
            content = null;
        } else {
            toolbar = null;
        }
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        throw new UnsupportedOperationException();
    }

    public boolean hasChildComponent(Widget component) {
        if (component == content || component == toolbar) {
            return true;
        }
        return false;
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // NOP not needed
    }

    public boolean requestLayout(Set<Paintable> children) {
        return true;
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        if (child == content) {
            return new RenderSpace(getOffsetWidth(), getOffsetHeight()
                    - toolbarDiv.getFirstChildElement().getOffsetHeight(), true);
        } else if (child == toolbar) {
            return new RenderSpace(getOffsetWidth(), TOOLBARHEIGHT, false);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if (!rendering) {
            if (content != null) {
                client.handleComponentRelativeSize((Widget) content);
            }
            if (toolbar != null) {
                client.handleComponentRelativeSize((Widget) toolbar);
            }
        }
    }

}
