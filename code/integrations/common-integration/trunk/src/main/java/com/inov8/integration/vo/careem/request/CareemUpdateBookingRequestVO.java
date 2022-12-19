package com.inov8.integration.vo.careem.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemUpdateBookingRequestVO implements Serializable {
    private static final long serialVersionUID = 2003436637833146539L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Booking ID cannot be null")
    private String booking_id;

    @NotNull(message = "Pickup Details cannot be null")
    private com.inov8.integration.vo.careem.helper.pickup_details pickup_details;
    @NotNull(message = "Dropoff Details cannot be null")
    private com.inov8.integration.vo.careem.helper.dropoff_details dropoff_details;

    private String driver_notes;
    @NotNull(message = "Reservation Date cannot be null")
    @Pattern(regexp = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", message = "Date is not well formatted, Acceptable format is dd-MMM-yyyy")
    private String reservation_date;
    @NotNull(message = "Reservation Time cannot be null")
    @Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3])-[0-5][0-9]$", message = "Time is not well formatted, Acceptable format is HH-mm")
    private String reservation_time;

    private String promo_code;


    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public com.inov8.integration.vo.careem.helper.pickup_details getPickup_details() {
        return pickup_details;
    }

    public void setPickup_details(com.inov8.integration.vo.careem.helper.pickup_details pickup_details) {
        this.pickup_details = pickup_details;
    }

    public com.inov8.integration.vo.careem.helper.dropoff_details getDropoff_details() {
        return dropoff_details;
    }

    public void setDropoff_details(com.inov8.integration.vo.careem.helper.dropoff_details dropoff_details) {
        this.dropoff_details = dropoff_details;
    }

    public String getDriver_notes() {
        return driver_notes;
    }

    public void setDriver_notes(String driver_notes) {
        this.driver_notes = driver_notes;
    }


    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
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
    public long getPickup() throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy-HH-mm").parse(getReservation_date() + "-" + getReservation_time()).getTime();
    }
}
