package com.vaadin.addon.touchkit.gwt.client.touchcombobox;

import com.google.gwt.event.shared.GwtEvent;

public class PageEvent extends GwtEvent<PageEventHandler> {

    public static enum PageEventType {
        NEXT, PREVIOUS, CLOSE
    }

    private static Type<PageEventHandler> TYPE;

    private PageEventType eventType;

    public PageEvent(PageEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public Type<PageEventHandler> getAssociatedType() {
        return getType();
    }

    public static Type<PageEventHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<PageEventHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(PageEventHandler handler) {
        handler.onPageEvent(this);
    }

    public PageEventType getEventType() {
        return eventType;
    }
}
