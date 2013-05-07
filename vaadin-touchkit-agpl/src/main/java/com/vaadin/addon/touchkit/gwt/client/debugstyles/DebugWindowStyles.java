package com.vaadin.addon.touchkit.gwt.client.debugstyles;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.DoNotEmbed;

public interface DebugWindowStyles extends ClientBundle {

    @Source({"debugwindowstyles.css"})
    @NotStrict
    public CssResource css();
    
    @Source("debugwindowfont.ttf")
    @DoNotEmbed
    DataResource iconFont();
    
}