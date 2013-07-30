package com.vaadin.addon.touchkit.gwt.client.ui;

import com.google.gwt.event.dom.client.MsPointerDownEvent;
import com.google.gwt.event.dom.client.MsPointerDownHandler;
import com.google.gwt.event.dom.client.MsPointerMoveEvent;
import com.google.gwt.event.dom.client.MsPointerMoveHandler;
import com.google.gwt.event.dom.client.MsPointerUpEvent;
import com.google.gwt.event.dom.client.MsPointerUpHandler;

public class VSwipeViewIEImpl extends VSwipeView {

    @Override
    protected void initHandlers() {
        super.initHandlers();
        getElement().getStyle().setProperty("msTouchAction", "pan-y");
        addDomHandler(new MsPointerDownHandler() {

            @Override
            public void onPointerDown(MsPointerDownEvent event) {
                dragStart(event);
            }
        }, MsPointerDownEvent.getType());

        addDomHandler(new MsPointerMoveHandler() {

            @Override
            public void onPointerMove(MsPointerMoveEvent event) {
                dragMove(event);
            }
        }, MsPointerMoveEvent.getType());

        addHandler(new MsPointerUpHandler() {

            @Override
            public void onPointerUp(MsPointerUpEvent event) {
                dragEnd(event);
            }
        }, MsPointerUpEvent.getType());

    }

}
