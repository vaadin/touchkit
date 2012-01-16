package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VNativeSelect;

public class VCustomNativeSelect extends VNativeSelect {

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // TODO: remove this once Android 2.x is no longer relevant and newer
        // versions actually fix the bug where nativeselects don't work on the
        // second+ page in a VNavigationManager.
        super.updateFromUIDL(uidl, client);
        if (Navigator.getUserAgent().toLowerCase().contains("android 2")) {
            Widget w = this;
            while ((w = w.getParent()) != null) {
                if (w instanceof VNavigationManager) {
                    ((VNavigationManager) w).forceRerender();
                    break;
                }
            }
        }
    }
}
