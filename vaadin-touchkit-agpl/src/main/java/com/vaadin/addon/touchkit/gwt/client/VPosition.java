package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.Position;

/**
 * Wrapper around Position information.
 * 
 * TODO extend with GeoPositionAddress etc
 * 
 */
public class VPosition extends JavaScriptObject {
    protected VPosition() {
    }

    public final native double getLatitude()
    /*-{ 
    	return this.coords.latitude ? this.coords.latitude : 0;
    }-*/;

    public final native double getLongitude()
    /*-{
    	return this.coords.longitude ? this.coords.longitude : 0;
    }-*/;

    public final native double getAltitude()
    /*-{
    	return this.coords.altitude ? this.coords.altitude : 0;
    }-*/;

    public final native double getAccuracy()
    /*-{
    	return this.coords.accuracy ? this.coords.accuracy : 0;
    }-*/;

    public final native double getAltitudeAccuracy()
    /*-{
     	return this.coords.altitudeAccuracy ? this.coords.altitudeAccuracy : 0;
    }-*/;

    public final native double getHeading()
    /*-{
    	return this.coords.heading ? this.coords.heading : 0;
    }-*/;

    public final native double getSpeed()
    /*-{
    	return this.coords.speed ? this.coords.speed : 0;
    }-*/;

    public final native String toJson()
    /*-{
     return JSON.stringify(this);
    }-*/;

    public final Position getPosition() {
        Position pos = new Position();
        pos.setAccuracy(getAccuracy());
        pos.setAltitude(getAltitude());
        pos.setAltitudeAccuracy(getAltitudeAccuracy());
        pos.setHeading(getHeading());
        pos.setLatitude(getLatitude());
        pos.setLongitude(getLongitude());
        pos.setSpeed(getSpeed());
        return pos;
    }
}