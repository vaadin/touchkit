package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.addon.touchkit.gwt.client.VPosition;
import com.vaadin.addon.touchkit.rootextensions.Geolocator;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;

@SuppressWarnings("serial")
@Connect(Geolocator.class)
public class GeolocatorConnector extends AbstractExtensionConnector {

    private GeolocatorServerRpc rpc = RpcProxy.create(
            GeolocatorServerRpc.class, this);

    public GeolocatorConnector() {
        registerRpc(GeolocatorClientRpc.class, new GeolocatorClientRpc() {
            @Override
            public void detectCurrentPosition() {
                try {
                    VConsole.log("Making geolocation request");
                    doGeoLocationLookup();
                } catch (Exception e) {
                    onGeolocationError(GEOLOCATION_ERROR_UNKNOWN);
                }
            }
        });
    }

    private native void doGeoLocationLookup()
    /*-{

        var me = this;
        var success = function(loc) {
                me.@com.vaadin.addon.touchkit.gwt.client.vaadincomm.GeolocatorConnector::onGeolocationSuccess(Lcom/vaadin/addon/touchkit/gwt/client/VPosition;)(loc);
        }
        var error = function(e) {
                me.@com.vaadin.addon.touchkit.gwt.client.vaadincomm.GeolocatorConnector::onGeolocationError(I)(e.code);
        }
        
        $wnd.navigator.geolocation.getCurrentPosition(success, error);
    
    }-*/;

    private void onGeolocationSuccess(VPosition position) {
        VConsole.log("Position detected.");
        rpc.onGeolocationSuccess(position.getPosition());
    }

    private static final int GEOLOCATION_ERROR_PERMISSION_DENIED = 1;
    private static final int GEOLOCATION_ERROR_POSITION_UNAVAILABLE = 2;
    private static final int GEOLOCATION_ERROR_POSITION_TIMEOUT = 3;
    private static final int GEOLOCATION_ERROR_UNKNOWN = 0;

    private void onGeolocationError(int errorCode) {
        VConsole.log("Error in geolocation " + errorCode);
        rpc.onGeolocationError(errorCode);
    }

}
