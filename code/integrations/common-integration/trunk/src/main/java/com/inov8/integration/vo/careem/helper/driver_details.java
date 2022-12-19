package com.inov8.integration.vo.careem.helper;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/22/2016.
 */
public class driver_details implements Serializable {
    private String driver_name;
    private String driver_number;
    private String driver_picture;
    private String notes_to_driver;

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getDriver_picture() {
        return driver_picture;
    }

    public void setDriver_picture(String driver_picture) {
        this.driver_picture = driver_picture;
    }

    public String getNotes_to_driver() {
        return notes_to_driver;
    }

    public void setNotes_to_driver(String notes_to_driver) {
        this.notes_to_driver = notes_to_driver;
    }
}
