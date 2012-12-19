package com.vaadin.addon.touchkit.gwt.client.vcom.touchcombobox;

import com.google.gwt.event.shared.GwtEvent;

public class PageEvent extends GwtEvent<PageEventHandler> {

    public static enum PageEventType {
        NEXT, PREVIOUS, CLOSE, ITEM_AMOUNT
    }

    private static Type<PageEventHandler> TYPE;

    private PageEventType eventType;
    private int value;
    private String key;

    public PageEvent(PageEventType eventType) {
        this.eventType = eventType;
    }

    public PageEvent(PageEventType eventType, int value) {
        this.eventType = eventType;
        this.value = value;
        key = null;
    }

    public PageEvent(PageEventType eventType, String key) {
        this.eventType = eventType;
        this.key = key;
        value = 1;
    }

    public PageEvent(PageEventType eventType, int value, String key) {
        this.eventType = eventType;
        this.value = value;
        this.key = key;
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

    public int getValue(){
        return value;
    }

    public String getKey() {
        return key;
    }
}
