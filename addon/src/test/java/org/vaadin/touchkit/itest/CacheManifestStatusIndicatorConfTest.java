package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.server.TouchKitServlet;
import org.vaadin.touchkit.settings.ApplicationCacheSettings;

import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.TextField;

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
