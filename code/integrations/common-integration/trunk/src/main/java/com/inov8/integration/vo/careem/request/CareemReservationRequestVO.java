package com.inov8.integration.vo.careem.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.vo.careem.helper.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemReservationRequestVO implements Serializable {
    private static final long serialVersionUID = 2003437737833146539L;

    @Min(value = 2, message = "product id cannot be this low")
    private int product_id;
    @NotNull(message = "Pickup Details cannot be null")
    private pickup_details pickup_details;
    @NotNull(message = "Dropoff Details cannot be null")
    private dropoff_details dropoff_details;
    private String driver_notes;
    @NotNull(message = "Booking Type cannot be null")
    @Pattern(regexp = "NOW|LATER", message = "Booking Type can only be NOW,LATER ")
    private String booking_type;
    @NotNull(message = "Reservation Date cannot be null")
    @Pattern(regexp = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", message = "Date is not well formatted, Acceptable format is dd-MM-yyyy")
    private String reservation_date;
    @NotNull(message = "Reservation Time cannot be null")
    @Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3])-[0-5][0-9]$", message = "Time is not well formatted, Acceptable format is HH-mm")
    private String reservation_time;

    private String promo_code;
    @NotNull(message = "Customer Details cannot be null")
    private customer_details customer_details;

    private String surge_confirmation_id;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public pickup_details getPickup_details() {
        return pickup_details;
    }

    public void setPickup_details(pickup_details pickup_details) {
        this.pickup_details = pickup_details;
    }

    public dropoff_details getDropoff_details() {
        return dropoff_details;
    }

    public void setDropoff_details(dropoff_details dropoff_details) {
        this.dropoff_details = dropoff_details;
    }

    public String getDriver_notes() {
        return driver_notes;
    }

    public void setDriver_notes(String driver_notes) {
        this.driver_notes = driver_notes;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public customer_details getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(customer_details customer_details) {
        this.customer_details = customer_details;
    }

    public String getSurge_confirmation_id() {
        return surge_confirmation_id;
    }

    public void setSurge_confirmation_id(String surge_confirmation_id) {
        this.surge_confirmation_id = surge_confirmation_id;
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
