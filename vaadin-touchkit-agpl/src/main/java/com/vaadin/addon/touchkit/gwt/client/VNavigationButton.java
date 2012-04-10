package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.Icon;

public class VNavigationButton extends HTML implements Paintable {
    private static final String NAVBUTTON_CLASSNAME = "v-touchkit-navbutton";
    private String nextViewId;
    private ApplicationConnection client;
    private String caption;
    private boolean enabled;
    private String nextViewCaption;

    public VNavigationButton() {
        setStyleName(NAVBUTTON_CLASSNAME);
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (enabled) {
                    getElement().focus();
                    navigate();
                    String pid = client.getPid(getElement());
                    // client.updateVariable(pid, "mousedetails",
                    // new MouseEventDetails(event.getNativeEvent())
                    // .toString(), false);
                    client.updateVariable(pid, "state", true, true);
                }
            }
        });
    }

    private void navigate() {
        VNavigationManager panel = findNavigationPanel(this);
        if (panel != null) {
            panel.onNaviButtonClick(this);
        }
    }

    public static VNavigationManager findNavigationPanel(Widget w) {
        Widget parent2 = w.getParent();
        while (parent2 != null && !(parent2 instanceof VNavigationManager)) {
            parent2 = parent2.getParent();
        }
        return (VNavigationManager) parent2;
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, false)) {
            return;
        }
        this.client = client;
        caption = uidl.getStringAttribute("caption");
        setText(caption);
        nextViewCaption = uidl.getStringAttribute("nvc");
        enabled = !uidl.getBooleanAttribute("disabled");

        if (uidl.hasAttribute("icon")) {
            Icon icon = new Icon(client, uidl.getStringAttribute("icon"));
            getElement().insertFirst(icon.getElement());
        }

        if (uidl.hasAttribute("description")) {
            String stringAttribute = uidl.getStringAttribute("description");
            SpanElement desc = Document.get().createSpanElement();
            desc.setClassName(NAVBUTTON_CLASSNAME + "-desc");
            desc.setInnerHTML(stringAttribute);
            getElement().insertFirst(desc);
        }

        if (uidl.hasAttribute("nv")) {
            setNextViewId(uidl.getStringAttribute("nv"));
        }
    }

    private void setNextViewId(String nextViewId) {
        this.nextViewId = nextViewId;
    }

    public String getNextViewId() {
        return nextViewId;
    }

    public String getCaption() {
        return caption;
    }

    public String getNextViewCaption() {
        if (nextViewCaption != null) {
            return nextViewCaption;
        }
        return getCaption();
    }

}
