package org.vaadin.touchkit.gwt.client.ui;

import java.util.Date;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Event;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VButton;

public class TouchButton extends VButton implements TouchStartHandler,
        TouchCancelHandler, TouchEndHandler, TouchMoveHandler {

    static final long IGNORE_SIMULATED_CLICKS_THRESHOLD = 1500;
    private boolean touchStarted;
    private Date fastClickAt;
    public TouchButton() {
        if(VNavigationButton.useFastClicks) {
            addTouchStartHandler(this);
            addTouchCancelHandler(this);
            addTouchMoveHandler(this);
            addTouchEndHandler(this);
        }
    }

    public void onTouchMove(TouchMoveEvent event) {
        touchStarted = false;
    }

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
            fastClickAt = new Date();
        }
    }

    public void onTouchCancel(TouchCancelEvent event) {
        touchStarted = false;
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (fastClickAt != null && event.getTypeInt() == Event.ONCLICK
                && (new Date().getTime() - fastClickAt.getTime()) < IGNORE_SIMULATED_CLICKS_THRESHOLD) {
            VConsole.log("Ignored simulated event fired by old ios or android "
                    + (new Date().getTime() - fastClickAt.getTime()));
            fastClickAt = null;
            return;
        }
        super.onBrowserEvent(event);
    }

    public void onTouchStart(TouchStartEvent event) {
        setFocus(true);
        touchStarted = true;
        fastClickAt = null;
    }

}