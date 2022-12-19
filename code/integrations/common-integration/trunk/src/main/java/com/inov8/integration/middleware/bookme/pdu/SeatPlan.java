package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Umar Aziz on 5/2/2017.
 */
public class SeatPlan {

    @JsonProperty("rows")
    private String rows;

    @JsonProperty("cols")
    private String cols;

    @JsonProperty("seats")
    private String seats;

    @JsonProperty("seatplan")
    private SeatMapping[][] seatplan;

    public SeatMapping[][] getSeatplan() {
        return seatplan;
    }

    public void setSeatplan(SeatMapping[][] seatplan) {
        this.seatplan = seatplan;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }


}
