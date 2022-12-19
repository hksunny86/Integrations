package com.inov8.integration.vo.careem.helper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/22/2016.
 */
public class customer_details implements Serializable{

    private String name;
    private String email;
    private String phone_number;

    @JsonProperty("uuid")
    public String getUuid() {
        return this.phone_number;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
