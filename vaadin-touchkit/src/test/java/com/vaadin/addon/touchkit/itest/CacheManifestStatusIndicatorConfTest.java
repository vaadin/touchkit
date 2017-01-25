package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.ApplicationCacheSettings;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;

public class CacheManifestStatusIndicatorConfTest extends
        AbstractTouchKitIntegrationTest {

    public CacheManifestStatusIndicatorConfTest() {
        setDescription("Test for modifying CacheManifestStatusIndicator settings");

        final TextField updateNowMessageTF = new TextField("updateNowMessage");
        updateNowMessageTF
                .setValue("There are updates ready to be installed. Would you like to restart now?");

        final TextField updateCheckIntervalTF = new TextField(
                "updateCheckInterval");
        updateCheckIntervalTF.setValue("1800");

        Button commit = new Button("Commit");
        commit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ApplicationCacheSettings applicationCacheSettings = ((TouchKitServlet) VaadinServlet
                        .getCurrent()).getTouchKitSettings()
                        .getApplicationCacheSettings();
                applicationCacheSettings.setUpdateNowMessage(updateNowMessageTF
                        .getValue());
                applicationCacheSettings.setUpdateCheckInterval(Integer
                        .valueOf(updateCheckIntervalTF.getValue()));
            }
        });

        addComponent(updateNowMessageTF);
        addComponent(updateCheckIntervalTF);
        addComponent(commit);
    }
}
