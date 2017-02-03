package org.vaadin.touchkit.gwt.client.ui;

import com.vaadin.client.event.PointerDownEvent;
import com.vaadin.client.event.PointerDownHandler;
import com.vaadin.client.event.PointerMoveEvent;
import com.vaadin.client.event.PointerMoveHandler;
import com.vaadin.client.event.PointerUpEvent;
import com.vaadin.client.event.PointerUpHandler;

public class VSwipeViewIEImpl extends VSwipeView {

    @Override
    protected void initHandlers() {
        super.initHandlers();
        getElement().getStyle().setProperty("msTouchAction", "pan-y");
        getElement().getStyle().setProperty("msScrollChaining", "none");
        addDomHandler(new PointerDownHandler() {

            @Override
            public void onPointerDown(PointerDownEvent event) {
                dragStart(event);
            }
        }, PointerDownEvent.getType());

        addDomHandler(new PointerMoveHandler() {

            @Override
            public void onPointerMove(PointerMoveEvent event) {
                dragMove(event);
            }
        }, PointerMoveEvent.getType());

        addHandler(new PointerUpHandler() {

            @Override
            public void onPointerUp(PointerUpEvent event) {
                dragEnd(event);
            }
        }, PointerUpEvent.getType());

    }

}
