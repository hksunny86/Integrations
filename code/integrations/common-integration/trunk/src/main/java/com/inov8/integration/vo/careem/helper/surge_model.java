package com.inov8.integration.vo.careem.helper;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/22/2016.
 */
public class surge_model implements Serializable {
    private String token;
    private float surge_multiplier;
    private float expiry_in_minutes;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getSurge_multiplier() {
        return surge_multiplier;
    }

    public void setSurge_multiplier(float surge_multiplier) {
        this.surge_multiplier = surge_multiplier;
    }

    public float getExpiry_in_minutes() {
        return expiry_in_minutes;
    }

    public void setExpiry_in_minutes(float expiry_in_minutes) {
        this.expiry_in_minutes = expiry_in_minutes;
    }

}
