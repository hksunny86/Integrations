package com.inov8.integration.vo.careem.response;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemBookingResponseVO extends CareemResponseVO {
    private String booking_id;
    private String surge_multiplier;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getSurge_multiplier() {
        return surge_multiplier;
    }

    public void setSurge_multiplier(String surge_multiplier) {
        this.surge_multiplier = surge_multiplier;
    }
}
