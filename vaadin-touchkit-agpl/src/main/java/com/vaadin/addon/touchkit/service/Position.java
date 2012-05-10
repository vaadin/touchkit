package com.vaadin.addon.touchkit.service;

import java.io.Serializable;
import java.util.HashMap;

public class Position implements Serializable {

    private double latitude;
    private double longitude;
    private double accuracy;

    public Position(String json) {
        // TODO consider using real JSON library as this may get more complex in
        // the future
        json = json.substring(2).replace("\"coords\":{", "")
                .replaceAll("\\{", ",").replaceAll("\\}", "");
        String[] split = json.split(",");
        HashMap<String, String> keyValue = new HashMap<String, String>();
        for (int i = 0; i < split.length; i++) {
            String field = split[i];
            String[] split2 = field.split(":");
            String fieldName = split2[0].replaceAll("\"", "");
            String value = null;
            if(split2.length> 1) {
            	value = split2[1];
            }
            keyValue.put(fieldName, value);
        }

        accuracy = Double.parseDouble(keyValue.get("accuracy"));
        latitude = Double.parseDouble(keyValue.get("latitude"));
        longitude = Double.parseDouble(keyValue.get("longitude"));

        // TODO other fields

    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
