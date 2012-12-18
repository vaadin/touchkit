package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import java.io.Serializable;

import com.google.gwt.geolocation.client.Position.Coordinates;

@SuppressWarnings("serial")
public class Position implements Serializable {

    private double latitude;
    private double longitude;
    private double accuracy;
    private Double altitude;
    private Double altitudeAccuracy;
    private Double heading;
    private Double speed;

    public Position() {
    }
    
    public Position(com.google.gwt.geolocation.client.Position r) {
        Coordinates c = r.getCoordinates();
        latitude = c.getLatitude();
        longitude = c.getLongitude();
        accuracy = c.getAccuracy();
        altitude = c.getAltitude();
        altitudeAccuracy = c.getAltitudeAccuracy();
        heading = c.getHeading();
        speed  = c.getSpeed();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    public void setAltitudeAccuracy(Double altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

}
