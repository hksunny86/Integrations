package com.inov8.integration.vo.careem.helper;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/22/2016.
 */
public class driver_location_details implements Serializable {
    private float latitude;
    private float longitude;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

}
