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

public class VNavigationButton extends HTML {
    private static final String NAVBUTTON_CLASSNAME = "v-touchkit-navbutton";
    private boolean enabled;
    private Widget targetWidget;
    private String placeHolderCaption;
    private String caption;
    private Icon icon;

    public VNavigationButton() {
        setStyleName(NAVBUTTON_CLASSNAME);
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (enabled) {
                    getElement().focus();
                    navigate();
                    // String pid = client.getPid(getElement());
                    // // client.updateVariable(pid, "mousedetails",
                    // // new MouseEventDetails(event.getNativeEvent())
                    // // .toString(), false);
                    // client.updateVariable(pid, "state", true, true);
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

    @Override
    public void setText(String text) {
        this.caption = text;
        super.setText(text);
    }

    public String getCaption() {
        return caption;
    }

    public void setTargetWidget(Widget widget) {
        this.targetWidget = widget;
    }

    public void setPlaceHolderCaption(String targetViewCaption) {
        this.placeHolderCaption = targetViewCaption;
    }

    public String getPlaceHolderCaption() {
        if (placeHolderCaption != null) {
            return placeHolderCaption;
        }
        return getCaption();
    }

    public void setIcon(Icon newIcon) {
        getElement().insertFirst(newIcon.getElement());
        this.icon = newIcon;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setDescription(String description) {
        if (description != null) {
            SpanElement desc = Document.get().createSpanElement();
            desc.setClassName(NAVBUTTON_CLASSNAME + "-desc");
            desc.setInnerHTML(description);
            getElement().insertFirst(desc);
        }
    }

}
