package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowDetail implements Serializable {

    private static final long serialVersionUID = -912918641509475813L;

    @JsonProperty("city_id")
    private String cityId;
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("show_id")
    private String showId;
    @JsonProperty("show_cenima_id")
    private String showCinemaId;
    @JsonProperty("cinema_name")
    private String cinemaName;
    @JsonProperty("hall_id")
    private String hallId;
    @JsonProperty("hall_name")
    private String hallName;
    @JsonProperty("show_date")
    private String showDate;
    @JsonProperty("show_start_time")
    private String showStartTime;
    @JsonProperty("show_time")
    private String showTime;
    @JsonProperty("ticket_price")
    private String ticketPrice;

    public String getHousefull() {
        return housefull;
    }

    public void setHousefull(String housefull) {
        this.housefull = housefull;
    }

    @JsonProperty("house_full")
    private String housefull;

    @JsonProperty("eticket")
    private String eTicket;

    public String geteTicket() {
        return eTicket;
    }

    public void seteTicket(String eTicket) {
        this.eTicket = eTicket;
    }

    @Override
    public String toString() {
        return "ShowDetail{" +
                "cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", showId='" + showId + '\'' +
                ", showCinemaId='" + showCinemaId + '\'' +
                ", cinemaName='" + cinemaName + '\'' +
                ", hallId='" + hallId + '\'' +
                ", hallName='" + hallName + '\'' +
                ", showDate='" + showDate + '\'' +
                ", showStartTime='" + showStartTime + '\'' +
                ", showTime='" + showTime + '\'' +
                ", ticketPrice='" + ticketPrice + '\'' +
                ", housefull='" + housefull + '\'' +
                '}';
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowCinemaId() {
        return showCinemaId;
    }

    public void setShowCinemaId(String showCinemaId) {
        this.showCinemaId = showCinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(String showStartTime) {
        this.showStartTime = showStartTime;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }



}
