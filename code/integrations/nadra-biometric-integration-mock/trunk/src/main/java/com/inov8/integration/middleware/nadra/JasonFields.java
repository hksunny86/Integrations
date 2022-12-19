package com.inov8.integration.middleware.nadra;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by inov8 on 4/14/2016.
 */
@Component
public class JasonFields implements Serializable {
    private byte[] imageBase;
    private String CNIC;
    private String name;

    public byte[] getImageBase() {
        return imageBase;
    }

    public void setImageBase(byte[] imageBase) {
        this.imageBase = imageBase;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
