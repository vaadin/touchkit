package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wrapper around Position information.
 * 
 * TODO extend with GeoPositionAddress etc
 * 
 */
public class VPosition extends JavaScriptObject {
    protected VPosition() {}

     public final native double getLatitude() 
     /*-{ 
     	return this.coords.latitude;
     }-*/;
     
     public final native double getLongitude()
     /*-{
     	return this.coords.longitude;
     }-*/;

     public final native double getAltitude() 
     /*-{
     	return this.coords.altitude;
     }-*/;
     
     public final native double getAccuracy() 
     /*-{
     	return this.coords.accuracy;
     }-*/;
     
     public final native double getAltitudeAccuracy()
     /*-{
      	return this.coords.altitudeAccuracy;
     }-*/;
     
     public final native double getHeading()
     /*-{
     	return this.coords.heading;
     }-*/;
     
     public final native double getSpeed()
     /*-{
     	return this.coords.speed;
     }-*/;

     public final native String toJson() 
     /*-{
    	 return JSON.stringify(this);
     }-*/;
}