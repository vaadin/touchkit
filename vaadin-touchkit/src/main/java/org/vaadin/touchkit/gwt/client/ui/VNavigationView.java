package org.vaadin.touchkit.gwt.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.TouchScrollDelegate;
import com.vaadin.client.ui.TouchScrollDelegate.TouchScrollHandler;

public class VNavigationView extends ComplexPanel {

    private static final String CLASSNAME = "v-touchkit-navview";
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

        TouchScrollHandler touchScrollHandler = TouchScrollDelegate
                .enableTouchScrolling(this, wrapper);
        DOM.sinkEvents(wrapper, Event.ONSCROLL);
    }

    @Override
    public void setStyleName(String style) {
        if (!hasToolbar) {
            style += " " + CLASSNAME + "-notoolbar";
        }
        super.setStyleName(style);
    }

    public void setToolbar(Widget widget) {
        hasToolbar = widget != null;
        setStyleDependentName("notoolbar", !hasToolbar);
        if (hasToolbar) {
            if (toolbar != null && toolbar != widget) {
                remove(toolbar);
            }
            toolbar = widget;
            if (!toolbar.isAttached()) {
                add(toolbar, toolbarDiv);
            }
        } else if (toolbar != null) {
            remove(toolbar);
        }
    }

    public void setContent(Widget widget) {
        if (content != null && content != widget) {
            remove(content);
        }
        content = widget;
        if (content.getParent() != this) {
            add(content, wrapper);
        }
    }

    public void setNavigationBar(VNavigationBar newNavBar) {
        if (navbar != newNavBar && navbar != null) {
            navbar.removeFromParent();
        }
        navbar = newNavBar;
        if (navbar.getParent() != this) {
            insert(navbar, getElement(), 0, true);
        }
    }

    @Override
    public boolean remove(Widget w) {
        boolean removed = super.remove(w);
        if (removed) {
            if (content == w) {
                content = null;
            } else if (toolbar == w) {
                toolbar = null;
            }
        }
        return removed;
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

    public HandlerRegistration addScrollHandler(ScrollHandler scrollHandler) {
        return addHandler(scrollHandler, ScrollEvent.getType());
    }

    @Override
    public void add(Widget child) {
        add(child, getElement());
    }

    public Widget getContent() {
        return content;
    }

}
