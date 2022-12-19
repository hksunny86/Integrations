package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class seat_plan
{
   private String row;
   private String place;
   private seats[] seats;

    @Override
    public String toString() {
        return "seat_plan{" +
                "row='" + row + '\'' +
                ", place='" + place + '\'' +
                ", seats=" + Arrays.toString(seats) +
                '}';
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public com.inov8.integration.middleware.bookme.pdu.seats[] getSeats() {
        return seats;
    }

    public void setSeats(com.inov8.integration.middleware.bookme.pdu.seats[] seats) {
        this.seats = seats;
    }
}
