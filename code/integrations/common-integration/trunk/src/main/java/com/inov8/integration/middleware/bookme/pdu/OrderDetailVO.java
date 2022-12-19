package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetailVO implements Serializable {

    private static final long serialVersionUID = 8185075273100981832L;


    @JsonProperty("handling_amount")
    private String handlingCharges;

    @JsonProperty("response")
    private String response;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("service")
    private String service_name;
    @JsonProperty("departure_city")
    private String sourceCity;
    @JsonProperty("arrival_city")
    private String destinationCity;
    @JsonProperty("booking_date")
    private String bookingDate;
    @JsonProperty("departure_time")
    private String departureTime;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("number_of_seats")
    private String numberOfSeats;
    @JsonProperty("date_created")
    private String dateCreated;
    @JsonProperty("status")
    private String status;
    @JsonProperty("title")
    private String title;
    @JsonProperty("cinema")
    private String cinemaName;
    @JsonProperty("name")
    private String name;
    @JsonProperty("contact_no")
    private String contactNo;
    @JsonProperty("email")
    private String email;
    @JsonProperty("cnic")
    private String cnic;
    @JsonProperty("address")
    private String address;
    @JsonProperty("city")
    private String city;

    @JsonProperty("screen")
    private String screen;

    @JsonProperty("movie")
    private String movie;

    @JsonProperty("show_time")
    private String showTime;


    @JsonProperty("total_amount")
    private String totalAmount;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("code")
    private String code;

    @JsonProperty("event_id")
    private String event_id;

    @JsonProperty("event_name")
    private String event_name;

    @JsonProperty("event_date")
    private String event_date;

    @JsonProperty("eticket")
    private String eTicket;

    public String getHandling_charges() {
        return handling_charges;
    }

    public void setHandling_charges(String handling_charges) {
        this.handling_charges = handling_charges;
    }

    @JsonProperty("handling_charges")
    private String handling_charges;

    @JsonProperty("easypay_handling_charges")
    private String easyPayHandlingCharges;

    @JsonProperty("easypay_handling_charges_per_pass")
    private String easyPayHandlingChargePerPass;

    @JsonProperty("pass_discount")
    private String passDiscount;

    @JsonProperty("ticket_discount")
    private String ticketDiscount;

    public String getEasyPayHandlingCharges() {
        return easyPayHandlingCharges;
    }

    public void setEasyPayHandlingCharges(String easyPayHandlingCharges) {
        this.easyPayHandlingCharges = easyPayHandlingCharges;
    }

    public String getEasyPayHandlingChargePerPass() {
        return easyPayHandlingChargePerPass;
    }

    public void setEasyPayHandlingChargePerPass(String easyPayHandlingChargePerPass) {
        this.easyPayHandlingChargePerPass = easyPayHandlingChargePerPass;
    }

    public String getPassDiscount() {
        return passDiscount;
    }

    public void setPassDiscount(String passDiscount) {
        this.passDiscount = passDiscount;
    }

    public String getTicketDiscount() {
        return ticketDiscount;
    }

    public void setTicketDiscount(String ticketDiscount) {
        this.ticketDiscount = ticketDiscount;
    }

    public String geteTicket() {
        return eTicket;
    }

    public void seteTicket(String eTicket) {
        this.eTicket = eTicket;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    @JsonProperty("cod")
    private String cod;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(String numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @JsonProperty(value = "seats")
    public void seats(String seats) {
        this.numberOfSeats = seats;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    @Override
    public String toString() {
        return "OrderDetailVO{" +
                "handlingCharges='" + handlingCharges + '\'' +
                ", response='" + response + '\'' +
                ", orderId='" + orderId + '\'' +
                ", service_name='" + service_name + '\'' +
                ", sourceCity='" + sourceCity + '\'' +
                ", destinationCity='" + destinationCity + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", amount='" + amount + '\'' +
                ", numberOfSeats='" + numberOfSeats + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", cinemaName='" + cinemaName + '\'' +
                ", name='" + name + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", cnic='" + cnic + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", screen='" + screen + '\'' +
                ", movie='" + movie + '\'' +
                ", showTime='" + showTime + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", code='" + code + '\'' +
                ", event_id='" + event_id + '\'' +
                ", event_name='" + event_name + '\'' +
                ", event_date='" + event_date + '\'' +
                '}';
    }
}
