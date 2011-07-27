package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.EntryPoint;

public class EagerResourceLoader implements EntryPoint {

    public void onModuleLoad() {
        TouchKitResources.INSTANCE.css().ensureInjected();
    }

}
