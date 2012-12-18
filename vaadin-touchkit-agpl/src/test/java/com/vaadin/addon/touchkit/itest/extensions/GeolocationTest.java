package com.vaadin.addon.touchkit.itest.extensions;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.Position;
import com.vaadin.addon.touchkit.rootextensions.Geolocator;
import com.vaadin.addon.touchkit.rootextensions.PositionCallback;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public class GeolocationTest extends AbstractTouchKitIntegrationTest implements
        PositionCallback {

    private Label locationLabel;

    public GeolocationTest() {
        setDescription("Test Geolocation");

        Button detectButton = new Button("Detect location",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        Geolocator.detect(GeolocationTest.this);
                    }
                });
        addComponent(detectButton);
        locationLabel = new Label();
        addComponent(locationLabel);
    }

    @Override
    public void onSuccess(Position position) {
        System.err.println("Geolocation request succeeded");
        locationLabel.setValue(getLocationString(position));
    }

    private String getLocationString(Position position) {
        return String
                .format("Your location is %f, %f altitude:%.2f speed:%.2f heading:%.2f accuracy:%.2f",
                        position.getLatitude(), position.getLongitude(),
                        position.getAltitude(), position.getSpeed(),
                        position.getHeading(), position.getAccuracy());
    }

    @Override
    public void onFailure(int errorCode) {
        System.err.println("Geolocation request failed");
        Notification.show("Couldn't find your location " + errorCode);
    }

}
