package com.inov8.integration.vo.careem.request;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemBookingRequestVO implements Serializable
{
    private static final long serialVersionUID = 2003436637833146539L;


    @NotNull(message = "Booking ID cannot be null")
    private String booking_id;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }
}
