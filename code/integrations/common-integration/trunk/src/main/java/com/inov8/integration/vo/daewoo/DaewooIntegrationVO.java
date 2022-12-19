package com.inov8.integration.vo.daewoo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.vo.daewoo.json.*;

import java.io.Serializable;

public class DaewooIntegrationVO implements Serializable{

    private static final long serialVersionUID = 2000475637838125088L;

    @JsonProperty("departureCityList")
    private GetDeparturesList[] departuresList;

    @JsonProperty("destinationCityList")
    private GetArrivalsList[] arrivalList;

    private Routes[] routes;
    private SeatsInfo[] seatsInfo;
    private Token[] token;
    private Booking[] booking;
    private Ticketing[] ticket;

    @JsonProperty("cityId")
    private String terminalDeparture;
    private String departureID;
    private String arrivalID;
    private String departureDate;

    private String clientToken;

    private String scheduleRoute;
    private String departureTime;
    private String fare;
    private String scheduleTimeCode;
    private String selectedSeats;
    private String seatsQty;
    private String totalAmount;
    private String gender;
    private String name;
    private String mobileNumber;
    private String seatsFor;
    private String cnic;
    private String remarks;

    private String bookNumber;
    private String transactionID;
    private String transactionAmount;
    private String transactionDate;
    private String transactionTime;

    private String scheduleCode;
    private String departureSeq;
    private String arrivalSeq;
    private String arrivalDate;

    private String responseCode;
    private String responseDescription;

    private String productId;
    private String sharedSession;
    private String latitude;
    private String longitude;
    private String transactionCharges;
    private String microbankTransactionCode;
    private String merchantId;
    private String chargesType;
    private String accountTitle;
    private String accountNumber;
    private String paymentMethod;
    private String deviceTypeId;
    private String sessionId;

    @JsonProperty("serviceId")
    private String serviceID;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getChargesType() {
        return chargesType;
    }

    public void setChargesType(String chargesType) {
        this.chargesType = chargesType;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public GetDeparturesList[] getDeparturesList() {
        return departuresList;
    }

    public void setDeparturesList(GetDeparturesList[] departuresList) {
        this.departuresList = departuresList;
    }

    public GetArrivalsList[] getArrivalList() {
        return arrivalList;
    }

    public void setArrivalList(GetArrivalsList[] arrivalList) {
        this.arrivalList = arrivalList;
    }

    public String getTerminalDeparture() {
        return terminalDeparture;
    }

    public void setTerminalDeparture(String terminalDeparture) {
        this.terminalDeparture = terminalDeparture;
    }

    public String getDepartureID() {
        return departureID;
    }

    public void setDepartureID(String departureID) {
        this.departureID = departureID;
    }

    public String getArrivalID() {
        return arrivalID;
    }

    public void setArrivalID(String arrivalID) {
        this.arrivalID = arrivalID;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public Routes[] getRoutes() {
        return routes;
    }

    public void setRoutes(Routes[] routes) {
        this.routes = routes;
    }

    public SeatsInfo[] getSeatsInfo() {
        return seatsInfo;
    }

    public void setSeatsInfo(SeatsInfo[] seatsInfo) {
        this.seatsInfo = seatsInfo;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getDepartureSeq() {
        return departureSeq;
    }

    public void setDepartureSeq(String departureSeq) {
        this.departureSeq = departureSeq;
    }

    public String getArrivalSeq() {
        return arrivalSeq;
    }

    public void setArrivalSeq(String arrivalSeq) {
        this.arrivalSeq = arrivalSeq;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Token[] getToken() {
        return token;
    }

    public void setToken(Token[] token) {
        this.token = token;
    }

    public Booking[] getBooking() {
        return booking;
    }

    public void setBooking(Booking[] booking) {
        this.booking = booking;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getScheduleRoute() {
        return scheduleRoute;
    }

    public void setScheduleRoute(String scheduleRoute) {
        this.scheduleRoute = scheduleRoute;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getScheduleTimeCode() {
        return scheduleTimeCode;
    }

    public void setScheduleTimeCode(String scheduleTimeCode) {
        this.scheduleTimeCode = scheduleTimeCode;
    }

    public String getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(String selectedSeats) {
        this.selectedSeats = selectedSeats;
    }

    public String getSeatsQty() {
        return seatsQty;
    }

    public void setSeatsQty(String seatsQty) {
        this.seatsQty = seatsQty;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSeatsFor() {
        return seatsFor;
    }

    public void setSeatsFor(String seatsFor) {
        this.seatsFor = seatsFor;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Ticketing[] getTicket() {
        return ticket;
    }

    public void setTicket(Ticketing[] ticket) {
        this.ticket = ticket;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSharedSession() {
        return sharedSession;
    }

    public void setSharedSession(String sharedSession) {
        this.sharedSession = sharedSession;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTransactionCharges() {
        return transactionCharges;
    }

    public void setTransactionCharges(String transactionCharges) {
        this.transactionCharges = transactionCharges;
    }


    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
