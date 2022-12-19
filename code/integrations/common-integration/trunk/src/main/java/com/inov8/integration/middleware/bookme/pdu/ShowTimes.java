package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Umar on 3/9/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowTimes implements Serializable{

    private static final long serialVersionUID = 1L;

    @JsonProperty("time")
    private String time;
    @JsonProperty("show_id")
    private String showId;

    @JsonProperty("hall_name")
    private String hallName;
    @JsonProperty("cinema_id")
    private String cinemaId;
    @JsonProperty("hall_id")
    private String hallId;
    @JsonProperty("ticket_price")
    private String ticketPrice;
    @JsonProperty("handling_charges")
    private String handlingCharges;
    private String houseful;

    @JsonProperty("eticket")
    private String eTicket;

    public String geteTicket() {
        return eTicket;
    }

    public void seteTicket(String eTicket) {
        this.eTicket = eTicket;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getHandlingCharges() {
        return handlingCharges;
    }

    public void setHandlingCharges(String handlingCharges) {
        this.handlingCharges = handlingCharges;
    }

    public String getHouseful() {
        return houseful;
    }

    public void setHouseful(String houseful) {
        this.houseful = houseful;
    }

    @Override
    public String toString() {
        return "ShowTimes{" +
                "houseful='" + houseful + '\'' +
                "time='" + time + '\'' +
                "show_id='" + showId + '\'' +
                "hall_name='" + hallName + '\'' +
                "cinema_id='" + cinemaId + '\'' +
                "hall_id='" + showId + '\'' +
                "ticket_price='" + ticketPrice + '\'' +
                ", handling_charges=" + handlingCharges +
                '}';
    }

}
