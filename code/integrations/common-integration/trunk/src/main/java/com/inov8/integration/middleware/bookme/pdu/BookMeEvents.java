package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookMeEvents implements Serializable {

    private String careem;

    @JsonProperty("events")
    private BookMeEventDetails[] eventDetailsList;

    public String getCareem() {
        return careem;
    }

    public void setCareem(String careem) {
        this.careem = careem;
    }

    public BookMeEventDetails[] getEventDetailsList() {
        return eventDetailsList;
    }

    public void setEventDetailsList(BookMeEventDetails[] eventDetailsList) {
        this.eventDetailsList = eventDetailsList;
    }
}