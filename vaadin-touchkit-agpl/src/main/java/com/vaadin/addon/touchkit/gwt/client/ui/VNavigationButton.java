package com.vaadin.addon.touchkit.gwt.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class VNavigationButton extends HTML implements TouchStartHandler,
        TouchCancelHandler, TouchEndHandler, TouchMoveHandler, ClickHandler {
    private static final String NAVBUTTON_CLASSNAME = "v-touchkit-navbutton";
    private Widget targetWidget;
    private String placeHolderCaption;
    private String caption;
    private ImageElement icon;
    private SpanElement descriptionElement;
    private boolean touchStarted = false;
    private boolean enabled;

    public VNavigationButton() {
        setStyleName(NAVBUTTON_CLASSNAME);
        addTouchStartHandler(this);
        addTouchCancelHandler(this);
        addTouchEndHandler(this);
        addTouchMoveHandler(this);
        addClickHandler(this);
    }

    public Widget getTargetWidget() {
        return targetWidget;
    }

    private void navigate() {
        VNavigationManager panel = findNavigationPanel(this);
        if (panel != null) {
            if (getTargetWidget() != null) {
                if (getTargetWidget().getParent() == panel) {
                    panel.setCurrentWidget(getTargetWidget());
                } else {
                    panel.setNextWidget(getTargetWidget());
                    panel.navigateForward();
                }
            } else {
                panel.navigateToPlaceholder(getPlaceHolderCaption());
            }
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
        caption = text;
        super.setText(text);
    }

    public String getCaption() {
        return caption;
    }

    public void setTargetWidget(Widget widget) {
        targetWidget = widget;
    }

    public void setPlaceHolderCaption(String targetViewCaption) {
        placeHolderCaption = targetViewCaption;
    }

    public String getPlaceHolderCaption() {
        if (placeHolderCaption != null) {
            return placeHolderCaption;
        }
        return getCaption();
    }

    public void setIcon(String iconUrl) {
        if (icon == null) {
            icon = Document.get().createImageElement();
            icon.setClassName(IconWidget.CLASSNAME);
        }
        icon.setSrc(iconUrl);
        getElement().insertFirst(icon);
    }

    public void setDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {

            if (descriptionElement == null) {
                descriptionElement = Document.get().createSpanElement();
                descriptionElement.setClassName(NAVBUTTON_CLASSNAME + "-desc");
            }
            /*
             * Add the descriptionElement if it's not already added as it might
             * have been overwritten by a call to #setText
             */
            if (!getElement().isOrHasChild(descriptionElement)) {
                getElement().insertFirst(descriptionElement);
            }
            descriptionElement.setInnerHTML(description);

        } else if (descriptionElement != null) {

            descriptionElement.removeFromParent();
            descriptionElement = null;
        }
    }

    @Override
    public void onTouchMove(TouchMoveEvent event) {
        touchStarted = false;
    }

    @Override
    public void onTouchEnd(TouchEndEvent event) {
        if (touchStarted) {
            event.preventDefault();
            event.stopPropagation();
            NativeEvent nativeEvent = event.getNativeEvent();
            NativeEvent evt = Document.get().createClickEvent(1,
                    nativeEvent.getScreenX(), nativeEvent.getScreenY(),
                    nativeEvent.getClientX(), nativeEvent.getClientY(), false,
                    false, false, false);
            getElement().dispatchEvent(evt);
            touchStarted = false;
        }
    }

    @Override
    public void onTouchCancel(TouchCancelEvent event) {
        touchStarted = false;
    }

    @Override
    public void onTouchStart(TouchStartEvent event) {
        touchStarted = true;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (enabled) {
            getElement().focus();
            navigate();
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
