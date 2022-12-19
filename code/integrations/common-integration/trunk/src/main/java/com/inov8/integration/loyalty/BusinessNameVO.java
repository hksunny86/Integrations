package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by bkr on 7/21/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessNameVO implements Serializable {

    private String id;
    private String businessName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
