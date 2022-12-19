package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Ajmal Khan on 6/12/2017.
 * Get the data of getFlexiFares API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class FlexiFares {
    private String service_id;
    private String time_id;
    private String fare;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }
}
