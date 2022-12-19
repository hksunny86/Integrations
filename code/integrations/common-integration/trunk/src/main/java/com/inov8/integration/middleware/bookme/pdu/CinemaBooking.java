package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CinemaBooking implements Serializable {

    private static final long serialVersionUID = -2934617269517800426L;

    private String done;

    @JsonProperty("booking_id")
    private String order;

    @JsonProperty("bookme_booking_id")
    private String bookmeBookingId;

    @JsonProperty("booking_reference")
    private String bookingReference;

    private String cinema;
    private String screen;
    private String movie;

    private String seats;

    @JsonProperty("net_amount")
    private String netAmount;

    @JsonProperty("handling_charges")
    private String handlingCharges;

    @JsonProperty("total_amount")
    private String totalAmount;

    private String name;
    private String phone;
    private String email;
    private String city;
    private String address;

    @JsonProperty("seat_numbers")
    private String seatNumbers;

    private String date;
    private String time;

    @Override
    public String toString() {
        return "CinemaBooking{" +
                "done='" + done + '\'' +
                ", order='" + order + '\'' +
                ", bookmeBookingId='" + bookmeBookingId + '\'' +
                ", bookingReference='" + bookingReference + '\'' +
                ", cinema='" + cinema + '\'' +
                ", screen='" + screen + '\'' +
                ", movie='" + movie + '\'' +
                ", seats='" + seats + '\'' +
                ", netAmount='" + netAmount + '\'' +
                ", handlingCharges='" + handlingCharges + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", seatNumbers='" + seatNumbers + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getBookmeBookingId() {
        return bookmeBookingId;
    }

    public void setBookmeBookingId(String bookmeBookingId) {
        this.bookmeBookingId = bookmeBookingId;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getHandlingCharges() {
        return handlingCharges;
    }

    public void setHandlingCharges(String handlingCharges) {
        this.handlingCharges = handlingCharges;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
