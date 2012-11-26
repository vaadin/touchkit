package com.vaadin.addon.touchkit.offlinetest;

import com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@PreserveOnRefresh
@Theme("base")
public class OfflineTest extends UI {

    private OfflineModeSettings offline;
    private int times = 0;

    @Override
    protected void init(VaadinRequest request) {
        offline = new OfflineModeSettings();
        offline.extend(this);
        offline.setPersistentSessionCookie(true);

        buildUi();
    }

    private void buildUi() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Button("Go offline",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        offline.goOffline();
                    }
                }));
        layout.addComponent(new Button("Clicked 0 times",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        times++;
                        event.getButton().setCaption(
                                "Clicked " + times + " times");
                    }
                }));
        setContent(layout);
    }

}
