package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.Icon;

public class VNavigationButton extends HTML implements Paintable, Focusable {
    private static final String NAVBUTTON_CLASSNAME = "v-touchkit-navbutton";
    private String nextViewId;
    private ApplicationConnection client;
    private String caption;

    public VNavigationButton() {
        setStyleName(NAVBUTTON_CLASSNAME);
        setTabIndex(0);
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                setFocus(true);
                navigate();
                String pid = client.getPid(getElement());
                // client.updateVariable(pid, "mousedetails",
                // new MouseEventDetails(event.getNativeEvent())
                // .toString(), false);
                client.updateVariable(pid, "state", true, true);
            }
        });
    }

    private void navigate() {
        VNavigationManager panel = findNavigationPanel();
        if (panel != null) {
            panel.onNaviButtonClick(this);
        }
    }

    private VNavigationManager findNavigationPanel() {
        Widget parent2 = getParent();
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

        if (uidl.hasAttribute("icon")) {
            Icon icon = new Icon(client, uidl.getStringAttribute("icon"));
            getElement().insertFirst(icon.getElement());
        }

        if (uidl.hasAttribute("description")) {
            String stringAttribute = uidl.getStringAttribute("description");
            SpanElement desc = Document.get().createSpanElement();
            desc.setClassName(NAVBUTTON_CLASSNAME + "-desc");
            desc.setInnerHTML(stringAttribute);
            getElement().appendChild(desc);
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

    public int getTabIndex() {
        return 0;
    }

    public void setAccessKey(char key) {
        // TODO Auto-generated method stub
    }

    public void setFocus(boolean focused) {
        if (focused) {
            getElement().focus();
        } else {
            getElement().blur();
        }
    }

    public void setTabIndex(int index) {
        getElement().setAttribute("tabIndex", "" + index);
    }

}
