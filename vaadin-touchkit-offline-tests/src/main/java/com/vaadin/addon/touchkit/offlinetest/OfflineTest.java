package com.vaadin.addon.touchkit.offlinetest;

import com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings;
import com.vaadin.addon.touchkit.ui.TouchKitUI;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.WrappedRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@PreserveOnRefresh
public class OfflineTest extends TouchKitUI {

    private OfflineModeSettings offline;
    private int times = 0;

    @Override
    protected void init(WrappedRequest request) {
        offline = new OfflineModeSettings();
        offline.extend(this);
        offline.setPersistentSessionCookie(true);

        buildUi();
    }

    private void buildUi() {
        addComponent(new Button("Go offline", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                offline.goOffline();
            }
        }));
        addComponent(new Button("Clicked 0 times", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                times++;
                event.getButton().setCaption("Clicked " + times + " times");
            }
        }));
    }

}
