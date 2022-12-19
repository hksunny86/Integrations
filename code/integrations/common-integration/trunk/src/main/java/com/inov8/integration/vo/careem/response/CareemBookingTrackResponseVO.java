package com.inov8.integration.vo.careem.response;

import com.inov8.integration.vo.careem.helper.*;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemBookingTrackResponseVO extends CareemResponseVO {

    private Long eta;
    private String status;
    private int product_id;
    private String product_name;
    private vehicle_details vehicle_details;
    private driver_details driver_details;
    private driver_location_details driver_location_details;

    public Long getEta() {
        return eta;
    }

    public void setEta(Long eta) {
        this.eta = eta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public vehicle_details getVehicle_details() {
        return vehicle_details;
    }

    public void setVehicle_details(vehicle_details vehicle_details) {
        this.vehicle_details = vehicle_details;
    }

    public driver_details getDriver_details() {
        return driver_details;
    }

    public void setDriver_details(driver_details driver_details) {
        this.driver_details = driver_details;
    }

    public driver_location_details getDriver_location_details() {
        return driver_location_details;
    }

    public void setDriver_location_details(driver_location_details driver_location_details) {
        this.driver_location_details = driver_location_details;
    }
}
