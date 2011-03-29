package com.vaadin.addons.touchkit.gwt.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTabsheet extends com.vaadin.terminal.gwt.client.ui.VTabsheet {
	private boolean fixScheduled;

	public VTabsheet() {
		TouchKitResources.INSTANCE.css().ensureInjected();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		fixMe();
	}

	private void fixMe() {
		if (!fixScheduled) {
			fixScheduled = true;
			Scheduler.get().scheduleFinally(new ScheduledCommand() {
				public void execute() {
					/*
					 * Overcome the Vaadin core annoyance, that the tab bar
					 * height is fixed on Safari.
					 */
					Element tbEl = getElement().getFirstChildElement();
					tbEl.getStyle().setProperty("height", "");
					setHeight(getOffsetHeight() + "px");
					fixScheduled = false;
				}
			});
		}

	}
}
