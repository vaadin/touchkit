package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ui.TouchScrollDelegate;

public class VNavigationView extends ComplexPanel {

    private static final String CLASSNAME = "v-touchkit-navview";
    private static final int NAVBARHEIGHT = 42;
    private Element wrapper = Document.get().createDivElement().cast();
    private Element toolbarDiv = Document.get().createDivElement().cast();

    private VNavigationBar navbar;
    private Widget content;
    private Widget toolbar;
    private boolean hasToolbar;

    public VNavigationView() {
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

        DOM.sinkEvents(wrapper, Event.ONSCROLL);
    }

    @Override
    public void setStyleName(String style) {
        if (!hasToolbar) {
            style += " " + CLASSNAME + "-notoolbar";
        }
        super.setStyleName(style);
    }

    // public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
    // Util.runWebkitOverflowAutoFix(wrapper);
    // rendering = false;
    // }

    public void setToolbar(Widget widget) {
        hasToolbar = widget != null;
        setStyleDependentName("notoolbar", !hasToolbar);
        if (hasToolbar) {
            if (toolbar != null && toolbar != widget) {
                forgetComponent(toolbar);
            }
            toolbar = widget;
            if (!toolbar.isAttached()) {
                add(toolbar, toolbarDiv);
            }
        } else if (toolbar != null) {
            forgetComponent(toolbar);
        }
    }

    public void updateContent(Widget widget) {
        if (content != null && content != widget) {
            forgetComponent(content);
        }
        content = widget;
        if (!content.isAttached()) {
            addContent(content);
        }
    }

    public void addContent(Widget content) {
        add(content, wrapper);
    }

    public void setNavigationBar(VNavigationBar newNavBar) {
        if (navbar != newNavBar && navbar != null) {
            navbar.removeFromParent();
        }
        navbar = newNavBar;
        if (!navbar.isAttached()) {
            insert(navbar, getElement(), 0, true);
        }
    }

    private void forgetComponent(Widget widget) {
        widget.removeFromParent();
        // client.unregisterPaintable(content2);
        if (content == widget) {
            content = null;
        } else {
            toolbar = null;
        }
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        throw new UnsupportedOperationException();
    }

    public boolean hasChildComponent(Widget component) {
        if (component == navbar || component == content || component == toolbar) {
            return true;
        }
        return false;
    }

    public int getScrollTop() {
        return wrapper.getScrollTop();
    }

    public void setScrollTop(int scrollTop) {
        wrapper.setScrollTop(scrollTop);
    }

    // public boolean requestLayout(Set<Paintable> children) {
    // return true;
    // }
    //
    // public RenderSpace getAllocatedSpace(Widget child) {
    // if (child == content) {
    // return new RenderSpace(wrapper.getOffsetWidth(),
    // wrapper.getOffsetHeight(), true);
    // } else if (child == toolbar) {
    // return new RenderSpace(getOffsetWidth(), NAVBARHEIGHT, false);
    //
    // } else if (child == navbar) {
    // return new RenderSpace(getOffsetWidth(),
    // toolbarDiv.getOffsetHeight(), false);
    // } else {
    // throw new IllegalArgumentException();
    // }
    // }
    //
    // @Override
    // public void setWidth(String width) {
    // super.setWidth(width);
    // if (!rendering) {
    // if (content != null) {
    // client.handleComponentRelativeSize((Widget) content);
    // }
    // if (toolbar != null) {
    // client.handleComponentRelativeSize((Widget) toolbar);
    // }
    // }
    // }

}
