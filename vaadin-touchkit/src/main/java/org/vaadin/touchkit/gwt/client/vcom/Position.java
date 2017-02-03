package org.vaadin.touchkit.gwt.client.vcom;

import java.io.Serializable;

/**
 * Contains details received from browsers <a
 * href="http://dev.w3.org/geo/api/spec-source.html">HTML5 geolocation</a>
 * request. Note that on some devices selected fields may be null.
 * <p>
 * The geographic coordinate reference system used by the attributes is the
 * World Geodetic System (2d) aka WGS84 aka EPSG:4326.
 */
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

    /**
     * @return the latitude of the position
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @see #getLatitude()
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude of the position
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @see #getLongitude()
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the accuracy of the position information in meters
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * @see #setAccuracy(double)
     */
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * @return the height of the position, specified in meters above the
     *         ellipsoid or null if device cannot provide the information.
     */
    public Double getAltitude() {
        return altitude;
    }

    /**
     * @see #getAltitude()
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    /**
     * @return the accuracy of the altitude informations in meters
     */
    public Double getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    /**
     * @see #getAltitudeAccuracy()
     */
    public void setAltitudeAccuracy(Double altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }

    /**
     * @return denotes the direction of travel of the hosting device and is
     *         specified in degrees, where 0° ≤ heading &lt; 360°, counting
     *         clockwise relative to the true north. Null if device don't
     *         support it or it is not moving.
     */
    public Double getHeading() {
        return heading;
    }

    /**
     * @see #getHeading()
     */
    public void setHeading(Double heading) {
        this.heading = heading;
    }

    /**
     * @return the magnitude of the horizontal component of the hosting device's
     *         current velocity and is specified in meters per second. If the
     *         implementation cannot provide speed information, the value of
     *         this attribute must be null. Otherwise, the value of the speed
     *         attribute must be a non-negative real number.
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * @see #getSpeed()
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

}
