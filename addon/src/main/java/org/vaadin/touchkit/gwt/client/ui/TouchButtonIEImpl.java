package org.vaadin.touchkit.gwt.client.ui;

import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VButton;

public class TouchButtonIEImpl extends VButton {

    // TODO add similar button optimization as for modern webkits

    @Override
    public void onBrowserEvent(Event event) {
        if (((event.getTypeInt() & Event.MOUSEEVENTS) != 0)) {
            // Vaadin button has some huge hacks (although it is just a button
            // :-( ) that are broken on some Window touch devices. E.g. on Lumia
            // 820 one gets double click on each click. Ignore the
            // hacks and just listen to the actual click events.
            return;
        }
        super.onBrowserEvent(event);
    }

}