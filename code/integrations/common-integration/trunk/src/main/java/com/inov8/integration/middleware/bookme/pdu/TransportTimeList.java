package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MuhammadJah on 2/24/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportTimeList implements Serializable {

    @JsonProperty("times")
    private List<TransportTime> transportTimeList;

    public List<TransportTime> getTransportTimeList() {
        return transportTimeList;
    }

    public void setTransportTimeList(List<TransportTime> transportTimeList) {
        this.transportTimeList = transportTimeList;
    }

    @Override
    public String toString() {
        return "TransportTimeList{" +
                "transportTimeList=" + transportTimeList +
                '}';
    }
}
