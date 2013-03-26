package com.vaadin.addon.touchkit.gwt.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
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

        
        TouchScrollHandler touchScrollHandler = TouchScrollDelegate.enableTouchScrolling(this);
        touchScrollHandler.addElement(wrapper);
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

    public void setContent(Widget widget) {
        if (content != null && content != widget) {
            forgetComponent(content);
        }
        content = widget;
        if (!content.isAttached()) {
            add(content, wrapper);
        }
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

}
