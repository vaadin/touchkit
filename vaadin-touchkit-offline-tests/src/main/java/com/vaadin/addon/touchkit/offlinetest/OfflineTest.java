package com.vaadin.addon.touchkit.offlinetest;

import com.vaadin.addon.touchkit.extensions.OfflineMode;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;

@PreserveOnRefresh
@Theme("base")
public class OfflineTest extends UI {

    private OfflineMode offline;
    private int times = 0;

    @Override
    protected void init(VaadinRequest request) {
        offline = new OfflineMode();
        offline.setPersistentSessionCookie(true);
        offline.setOfflineModeEnabled(true);
        offline.extend(this);

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
