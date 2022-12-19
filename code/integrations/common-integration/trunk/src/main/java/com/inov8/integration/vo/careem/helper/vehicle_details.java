package com.inov8.integration.vo.careem.helper;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/22/2016.
 */
public class vehicle_details implements Serializable {

    private String model;
    private String make;
    private String license_plate;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }
}
