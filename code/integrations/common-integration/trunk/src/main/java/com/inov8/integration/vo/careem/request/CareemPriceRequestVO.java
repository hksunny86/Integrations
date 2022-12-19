package com.inov8.integration.vo.careem.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemPriceRequestVO implements Serializable {
    private static final long serialVersionUID = 2003437737933146539L;

    @Pattern(regexp = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "Start Latitude is not correct")
    private String start_latitude;
    @Pattern(regexp = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "Start Longitude is not correct")
    private String start_longitude;
    @Pattern(regexp = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "End Latitude is not correct")
    private String end_latitude;
    @Pattern(regexp = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$", message = "End Longitude is not correct")
    private String end_longitude;
    @NotNull(message = "Booking Type cannot be null")
    @Pattern(regexp = "NOW|LATER",message = "Booking Type can only be NOW,LATER ")
    private String booking_type;
    @Min(value = 2, message = "product id cannot be this low")
    private Long product_id;
    @NotNull(message = "Reservation Date cannot be null")
    @Pattern(regexp = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", message = "Date is not well formatted, Acceptable format is dd-MM-yyyy")
    private String reservation_date;
    @NotNull(message = "Reservation Time cannot be null")
    @Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3])-[0-5][0-9]$", message = "Time is not well formatted, Acceptable format is HH-mm")
    private String reservation_time;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Float getStart_latitude() {
        return Float.parseFloat(start_latitude);
    }

    public void setStart_latitude(String start_latitude) {
        this.start_latitude = start_latitude;
    }

    public Float getStart_longitude() {
        return Float.parseFloat(start_longitude);
    }

    public void setStart_longitude(String start_longitude) {
        this.start_longitude = start_longitude;
    }

    public Float getEnd_latitude() {
        return Float.parseFloat(end_latitude);
    }

    public void setEnd_latitude(String end_latitude) {
        this.end_latitude = end_latitude;
    }

    public Float getEnd_longitude() {
        return Float.parseFloat(end_longitude);
    }

    public void setEnd_longitude(String end_longitude) {
        this.end_longitude = end_longitude;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getReservation_date() {
        return reservation_date;
    }

    public void setReservation_date(String reservation_date) {
        this.reservation_date = reservation_date;
    }

    public String getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(String reservation_time) {
        this.reservation_time = reservation_time;
    }

    @JsonProperty("pickup_time")
    public long getPickup() {
        try {
            return new SimpleDateFormat("dd-MM-yyyy-HH-mm").parse(getReservation_date() + "-" + getReservation_time()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
