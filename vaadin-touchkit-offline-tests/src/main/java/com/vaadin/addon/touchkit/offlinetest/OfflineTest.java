package com.vaadin.addon.touchkit.offlinetest;

import com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings;
import com.vaadin.server.WrappedRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

public class OfflineTest extends UI {

    private OfflineModeSettings offline;
    private int times = 0;

    @Override
    protected void init(WrappedRequest request) {
        offline = new OfflineModeSettings();
        offline.extend(this);
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
