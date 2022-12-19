package com.inov8.integration.vo.careem.request;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemProductsRequestVO implements Serializable {
    private static final long serialVersionUID = 2003476737935646539L;

//    @Pattern(regexp = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "Latitude is not correct")
    private String latitude;
//    @Pattern(regexp = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "Longitude is not correct")
    private String longitude;


    public Float getLatitude() {
        return Float.valueOf(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return Float.valueOf(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
