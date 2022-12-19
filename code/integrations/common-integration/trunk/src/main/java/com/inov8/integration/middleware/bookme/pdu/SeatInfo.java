package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Umar on 3/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeatInfo implements Serializable {

    @JsonProperty("seatplan")
    private SeatPlan seatplan;


    @JsonProperty("total_seats")
    private String totalSeats;

    @JsonProperty("total_available")
    private String totalAvailable;

    @JsonProperty("available_seats")
    private String availableSeats;

    @JsonProperty("total_occupied")
    private String totalOccupied;

    @JsonProperty("occupied_seats_male")
    private String occupiedSeatsMale;

    @JsonProperty("occupied_seats_female")
    private String occupiedSeatsFemale;

    @JsonProperty("total_reserved")
    private String totalReserved;

    @JsonProperty("reserved_seats_male")
    private String reservedSeatsMale;

    @JsonProperty("reserved_seats_female")
    private String reservedSeatsFemale;

    @Override
    public String toString() {
        return "seat_info{" +
                "total_seats='" + totalSeats + '\'' +
                "total_available='" + totalAvailable + '\'' +
                "available_seats='" + availableSeats + '\'' +
                "total_occupied='" + totalOccupied + '\'' +
                "occupied_seats_male='" + occupiedSeatsMale + '\'' +
                "occupied_seats_female='" + occupiedSeatsFemale + '\'' +
                "total_reserved='" + totalReserved + '\'' +
                "reserved_seats_male='" + reservedSeatsMale + '\'' +
                ", reserved_seats_female=" + reservedSeatsFemale +
                '}';
    }


    public String getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(String totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(String totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getTotalOccupied() {
        return totalOccupied;
    }

    public void setTotalOccupied(String totalOccupied) {
        this.totalOccupied = totalOccupied;
    }

    public String getOccupiedSeatsMale() {
        return occupiedSeatsMale;
    }

    public void setOccupiedSeatsMale(String occupiedSeatsMale) {
        this.occupiedSeatsMale = occupiedSeatsMale;
    }

    public String getOccupiedSeatsFemale() {
        return occupiedSeatsFemale;
    }

    public void setOccupiedSeatsFemale(String occupiedSeatsFemale) {
        this.occupiedSeatsFemale = occupiedSeatsFemale;
    }

    public String getTotalReserved() {
        return totalReserved;
    }

    public void setTotalReserved(String totalReserved) {
        this.totalReserved = totalReserved;
    }

    public String getReservedSeatsMale() {
        return reservedSeatsMale;
    }

    public void setReservedSeatsMale(String reservedSeatsMale) {
        this.reservedSeatsMale = reservedSeatsMale;
    }

    public String getReservedSeatsFemale() {
        return reservedSeatsFemale;
    }

    public void setReservedSeatsFemale(String reservedSeatsFemale) {
        this.reservedSeatsFemale = reservedSeatsFemale;
    }
}
