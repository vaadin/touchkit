package org.vaadin.touchkit.itest.oldtests;

import com.vaadin.server.VaadinRequest;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;

public class FallbackUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        setContent(new Label(
                "Your browser is not supported by this application. You'll instead be shown this fallback application. Use webkit based application to test TouchKit"));
    }

}
