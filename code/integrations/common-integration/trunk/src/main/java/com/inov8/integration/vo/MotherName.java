package com.inov8.integration.vo;

import java.io.Serializable;

public class MotherName implements Serializable {
    private String id;
    private String MotherName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }
}
