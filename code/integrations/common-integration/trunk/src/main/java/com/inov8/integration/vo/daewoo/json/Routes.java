package com.inov8.integration.vo.daewoo.json;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Routes {

    @JsonProperty("RESERVATIONDATE")
    private String reservationDate;
    @JsonProperty("SCHEDULE_CODE")
    private String scheduleCode;
    @JsonProperty("DEPARTURE_SEQ")
    private String departureSeq;
    @JsonProperty("ARRIVAL_SEQ")
    private String arrivalSeq;
    @JsonProperty("SCHEDULE_ROUTE")
    private String scheduleRoute;
    @JsonProperty("SCHEDULE_ROUTE_NAME")
    private String scheduleRouteName;
    @JsonProperty("SCHEDULE_DEPARTURE_TIME")
    private String scheduleDepartureTime;
    @JsonProperty("SCHEDULE_ARRIVAL_TIME")
    private String scheduleArrivalTime;
    @JsonProperty("SCHEDULE_TIMECODE")
    private String scheduleTimeCode;
    @JsonProperty("FARE_FARE")
    private String fareFare;
    @JsonProperty("BUSTYPE_NAME")
    private String busTypeName;
    @JsonProperty("BUSTYPE_SEATS")
    private String busTypeSeats;
    @JsonProperty("STAFF_SEAT")
    private String staffSeats;
    @JsonProperty("AVAILABLE")
    private String available;
    @JsonProperty("TRIP_STATUS")
    private String tripStatus;

    @JsonProperty("API_OUT")
    private String apiOut;

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
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

    public String getScheduleRoute() {
        return scheduleRoute;
    }

    public void setScheduleRoute(String scheduleRoute) {
        this.scheduleRoute = scheduleRoute;
    }

    public String getScheduleRouteName() {
        return scheduleRouteName;
    }

    public void setScheduleRouteName(String scheduleRouteName) {
        this.scheduleRouteName = scheduleRouteName;
    }

    public String getScheduleDepartureTime() {
        return scheduleDepartureTime;
    }

    public void setScheduleDepartureTime(String scheduleDepartureTime) {
        this.scheduleDepartureTime = scheduleDepartureTime;
    }

    public String getScheduleArrivalTime() {
        return scheduleArrivalTime;
    }

    public void setScheduleArrivalTime(String scheduleArrivalTime) {
        this.scheduleArrivalTime = scheduleArrivalTime;
    }

    public String getScheduleTimeCode() {
        return scheduleTimeCode;
    }

    public void setScheduleTimeCode(String scheduleTimeCode) {
        this.scheduleTimeCode = scheduleTimeCode;
    }

    public String getFareFare() {
        return fareFare;
    }

    public void setFareFare(String fareFare) {
        this.fareFare = fareFare;
    }

    public String getBusTypeName() {
        return busTypeName;
    }

    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public String getBusTypeSeats() {
        return busTypeSeats;
    }

    public void setBusTypeSeats(String busTypeSeats) {
        this.busTypeSeats = busTypeSeats;
    }

    public String getStaffSeats() {
        return staffSeats;
    }

    public void setStaffSeats(String staffSeats) {
        this.staffSeats = staffSeats;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getApiOut() {
        return apiOut;
    }

    public void setApiOut(String apiOut) {
        this.apiOut = apiOut;
    }
}
