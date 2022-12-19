package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportBooking implements Serializable {

    private static final long serialVersionUID = -5578815662493432416L;

    private String status;

    @JsonProperty("dep")
    private String departure;
    @JsonProperty("arr")
    private String arrival;

    @JsonProperty("booking_id")
    private String order;

    @JsonProperty("reference_booking_id")
    private String referenceBookingId;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("deperature_city")
    private String departureCity;

    @JsonProperty("arrival_city")
    private String arrivalCity;

    @JsonProperty("departure_date")
    private String departureDate;

    @JsonProperty("departure_time")
    private String departureTime;



    @JsonProperty("total_seats")

    private String totalSeats;

    @JsonProperty("seat_numbers_male")
    private String seatNumbersMale;

    @JsonProperty("seat_numbers_female")
    private String seatNumbersFemale;

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

    @Override
    public String toString() {
        return "TransportBooking{" +
                "status='" + status + '\'' +
                ", order='" + order + '\'' +
                ", referenceBookingId='" + referenceBookingId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", departureCity='" + departureCity + '\'' +
                ", arrivalCity='" + arrivalCity + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", totalSeats='" + totalSeats + '\'' +
                ", seatNumbersMale='" + seatNumbersMale + '\'' +
                ", seatNumbersFemale='" + seatNumbersFemale + '\'' +
                ", netAmount='" + netAmount + '\'' +
                ", handlingCharges='" + handlingCharges + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getReferenceBookingId() {
        return referenceBookingId;
    }

    public void setReferenceBookingId(String referenceBookingId) {
        this.referenceBookingId = referenceBookingId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(String totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getSeatNumbersMale() {
        return seatNumbersMale;
    }

    public void setSeatNumbersMale(String seatNumbersMale) {
        this.seatNumbersMale = seatNumbersMale;
    }

    public String getSeatNumbersFemale() {
        return seatNumbersFemale;
    }

    public void setSeatNumbersFemale(String seatNumbersFemale) {
        this.seatNumbersFemale = seatNumbersFemale;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
