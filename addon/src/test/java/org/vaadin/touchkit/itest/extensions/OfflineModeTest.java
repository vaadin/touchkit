package org.vaadin.touchkit.itest.extensions;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.extensions.OfflineMode;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class OfflineModeTest extends AbstractTouchKitIntegrationTest {
    public OfflineModeTest() {
    }

    @Override
    public void attach() {
        super.attach();
        final OfflineMode offlineModeSettings = new OfflineMode();
        offlineModeSettings.extend(getUI());

        setDescription("Test Offline mode");

        Button detectButton = new Button("Open offline app",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        offlineModeSettings.goOffline();
                    }
                });
        addComponent(detectButton);
    }

}
