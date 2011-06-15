package com.vaadin.addons.touchkit.gwt.client;

import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;

public class VNavigationBar extends ComplexPanel implements Container {

    private static final String CLASSNAME = "v-navbar";
    private ApplicationConnection client;
    private DivElement caption = Document.get().createDivElement();
    private DivElement componentEl = Document.get().createDivElement();
    private DivElement backButtonEl = Document.get().createDivElement();
    private Paintable backButton;
    private Paintable component;

    public VNavigationBar() {
        TouchKitResources.INSTANCE.css().ensureInjected();
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
        getElement().appendChild(caption);
        caption.setClassName(CLASSNAME + "-caption");
        componentEl.setClassName(CLASSNAME + "-actions");
        getElement().appendChild(componentEl);
        backButtonEl.setClassName(CLASSNAME + "-backb");
        getElement().appendChild(backButtonEl);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        this.client = client;
        if (client.updateComponent(this, uidl, false)) {
            return;
        }

        /*
         * Note, HTML caption. TODO XSS vuln.
         */
        caption.setInnerHTML(uidl.getStringAttribute("caption"));

        UIDL backUidl = uidl.getChildByTagName("back");
        if (backUidl == null && backButton != null) {
            ((Widget) backButton).removeFromParent();
            client.unregisterPaintable(backButton);
            backButton = null;
        }
        if (backUidl != null) {
            UIDL backButtonUidl = backUidl.getChildUIDL(0);
            Paintable newBackButton = client.getPaintable(backButtonUidl);
            if (backButton != null && backButton != newBackButton) {
                clearBackComponent();
            }
            backButton = newBackButton;
            if (!((Widget) backButton).isAttached()) {
                add((Widget) backButton,
                        (com.google.gwt.user.client.Element) backButtonEl
                                .cast());
            }
            backButton.updateFromUIDL(backButtonUidl, client);
        } else if (backButton != null) {
            clearBackComponent();
        }

        UIDL componentUidl = uidl.getChildByTagName("component");
        boolean hasComponent = componentUidl != null;
        componentEl.getStyle().setDisplay(
                hasComponent ? Display.BLOCK : Display.NONE);
        if (hasComponent) {
            componentUidl = componentUidl.getChildUIDL(0);
            Paintable paintable = client.getPaintable(componentUidl);
            if (component != paintable && component != null) {
                clearComponent();
            }

            component = paintable;
            if (!(((Widget) component)).isAttached()) {
                add((Widget) component,
                        (com.google.gwt.user.client.Element) componentEl.cast());
            }

            component.updateFromUIDL(componentUidl, client);

        } else if (component != null) {
            clearComponent();
        }

    }

    private void clearBackComponent() {
        ((Widget) backButton).removeFromParent();
        client.unregisterPaintable(backButton);
        backButton = null;
    }

    private void clearComponent() {
        ((Widget) component).removeFromParent();
        client.unregisterPaintable(component);
        component = null;
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        throw new UnsupportedOperationException();
    }

    public boolean hasChildComponent(Widget component) {
        if (component == backButton || component == this.component) {
            return true;
        }
        return false;
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // NOP, doesn't support delegated caption rendering.
    }

    public boolean requestLayout(Set<Paintable> children) {
        return true; // always 100% width + fixed height
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        return new RenderSpace(getOffsetWidth(), getOffsetHeight());
    }

}
