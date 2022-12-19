package com.inov8.integration.vo.careem.response;
import com.inov8.integration.vo.careem.helper.*;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemPriceResponseVO  extends CareemResponseVO{
    private float duration;
    private float distance;
    private String currency_code;
    private String estimate;
    private float low_estimate;
    private float high_estimate;
    private String metric;
    private surge_model surge_model;


    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public float getLow_estimate() {
        return low_estimate;
    }

    public void setLow_estimate(float low_estimate) {
        this.low_estimate = low_estimate;
    }

    public float getHigh_estimate() {
        return high_estimate;
    }

    public void setHigh_estimate(float high_estimate) {
        this.high_estimate = high_estimate;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public surge_model getSurge_model() {
        return surge_model;
    }

    public void setSurge_model(surge_model surge_model) {
        this.surge_model = surge_model;
    }
}
