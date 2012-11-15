package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VButton;

public class TouchButton extends VButton {
	
	protected boolean touchStarted = false;
	
	public TouchButton() {
        sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS
                | Event.KEYEVENTS | Event.TOUCHEVENTS);
	}
	
	@Override
    public void onBrowserEvent(Event event) {
		switch (event.getTypeInt()) {
		case Event.ONTOUCHSTART:
			touchStarted = true;
			break;
		case Event.ONTOUCHMOVE:
		case Event.ONTOUCHCANCEL:
			touchStarted = false;
			break;
		case Event.ONTOUCHEND:
			if (touchStarted) {
				event.preventDefault();
				event.stopPropagation();
				onClick();
			}
			touchStarted = false;
			break;
		default:
			super.onBrowserEvent(event);
		}
	}
}
