package com.inov8.integration.vo.careem.helper;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/22/2016.
 */
public class price_details implements Serializable {
    private float base_now;
    private float base_later;
    private float cost_per_distance;
    private String distance_unit;
    private String currency_code;
    private float minimum_now;
    private float minimum_later;
    private float cost_per_minute;
    private float cost_per_hour;
    private float cancellation_fee_later;
    private float cancellation_fee_now;

    public float getCost_per_hour() {
        return cost_per_hour;
    }

    public void setCost_per_hour(float cost_per_hour) {
        this.cost_per_hour = cost_per_hour;
    }

    public float getMinimum_later() {
        return minimum_later;
    }

    public void setMinimum_later(float minimum_later) {
        this.minimum_later = minimum_later;
    }

    public float getCost_per_distance() {
        return cost_per_distance;
    }

    public void setCost_per_distance(float cost_per_distance) {
        this.cost_per_distance = cost_per_distance;
    }

    public float getBase_now() {
        return base_now;
    }

    public void setBase_now(float base_now) {
        this.base_now = base_now;
    }

    public float getBase_later() {
        return base_later;
    }

    public void setBase_later(float base_later) {
        this.base_later = base_later;
    }

    public String getDistance_unit() {
        return distance_unit;
    }

    public void setDistance_unit(String distance_unit) {
        this.distance_unit = distance_unit;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public float getMinimum_now() {
        return minimum_now;
    }

    public void setMinimum_now(float minimum_now) {
        this.minimum_now = minimum_now;
    }

    public float getCost_per_minute() {
        return cost_per_minute;
    }

    public void setCost_per_minute(float cost_per_minute) {
        this.cost_per_minute = cost_per_minute;
    }

    public float getCancellation_fee_later() {
        return cancellation_fee_later;
    }

    public void setCancellation_fee_later(float cancellation_fee_later) {
        this.cancellation_fee_later = cancellation_fee_later;
    }

    public float getCancellation_fee_now() {
        return cancellation_fee_now;
    }

    public void setCancellation_fee_now(float cancellation_fee_now) {
        this.cancellation_fee_now = cancellation_fee_now;
    }

}
