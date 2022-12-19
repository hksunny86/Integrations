package com.inov8.integration.vo.careem.helper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/22/2016.
 */
public class pickup_details implements Serializable {
    @Pattern(regexp = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "pickup_details.Latitude is not correct")
    private String latitude;
    @Pattern(regexp = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "pickup_details.Longitude is not correct")
    private String longitude;
    @NotNull(message = "Nickname cannot be null")
    private String nickname;

    public float getLongitude() {
        return Float.parseFloat(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return Float.parseFloat(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
