package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportTime implements Serializable {

    @JsonProperty("time_id")
    private String timeId;

    @JsonProperty("time")
    private String departureTime;

    @JsonProperty("departure_city_id")
    private String departureCityId;

    @JsonProperty("departure_city_name")
    private String departureCityName;


    @JsonProperty("arrival_city_id")

    private String arrivalCityId;

    @JsonProperty("arrival_city_name")
    private String arrivalCityName;

    @JsonProperty("service_id")
    private String serviceId;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("fare")
    private String fare;

    @JsonProperty("thumb")
    private String thumb;

    @JsonProperty("arrtime")
    private String arrivalTime;

    @JsonProperty("schedule_id")
    private String scheduleId;

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("seats")
    private String seats;

    @JsonProperty("busname")
    private String busName;

    @JsonProperty("bustype")
    private String busType;

    @JsonProperty("facilities")
    private Facilities[] facilities;

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Facilities[] getFacilities() {
        return facilities;
    }

    public void setFacilities(Facilities[] facilities) {
        this.facilities = facilities;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
    /* @JsonProperty("seat_info")
    private SeatInfo seatInfo;

    public SeatInfo getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(SeatInfo seatInfo) {
        this.seatInfo = seatInfo;
    }*/

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureCityId() {
        return departureCityId;
    }

    public void setDepartureCityId(String departureCityId) {
        this.departureCityId = departureCityId;
    }

    public String getDepartureCityName() {
        return departureCityName;
    }

    public void setDepartureCityName(String departureCityName) {
        this.departureCityName = departureCityName;
    }

    public String getArrivalCityId() {
        return arrivalCityId;
    }

    public void setArrivalCityId(String arrivalCityId) {
        this.arrivalCityId = arrivalCityId;
    }

    public String getArrivalCityName() {
        return arrivalCityName;
    }

    public void setArrivalCityName(String arrivalCityName) {
        this.arrivalCityName = arrivalCityName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        return "TransportTime{" +
                "departure_city_id='" + departureCityId + '\'' +
                "departure_city_name='" + departureCityName + '\'' +
                "arrival_city_id='" + arrivalCityId + '\'' +
                "arrival_city_name='" + arrivalCityName + '\'' +
                "service_id='" + serviceId + '\'' +
                "service_name='" + serviceName + '\'' +
                "time_id='" + timeId + '\'' +
                "time='" + departureTime + '\'' +
                "fare='" + fare + '\'' +
                ", thumb='" + thumb + '\'' +
                ", arrtime='" + arrivalTime + '\'' +
                ", schedule_id='" + scheduleId + '\'' +
                ", route_id='" + routeId + '\'' +
//                ", seat_info=" + seatInfo +
                '}';
    }
}
