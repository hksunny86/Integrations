package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Umar on 6/28/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ad implements Serializable{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Ad{name=" + this.name + '}';
    }
}
