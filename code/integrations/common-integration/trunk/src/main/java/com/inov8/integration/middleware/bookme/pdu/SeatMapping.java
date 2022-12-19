package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Umar Aziz on 5/2/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeatMapping implements Serializable{

    @JsonProperty("seat_id")
    private String seatId;

    @JsonProperty("seat_name")
    private String seatName;

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }
}
