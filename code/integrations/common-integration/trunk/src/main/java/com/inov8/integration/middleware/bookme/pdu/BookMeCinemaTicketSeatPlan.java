package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BookMeCinemaTicketSeatPlan
{
	@JsonProperty("show_id")
	private String show_id;
	
	private String hall_id;

    private String hall_name;
	
	private String rows;

    private String cols;

    private seat_plan[] seat_plan;

    public seat_plan[] getSeat_plan() {
        return seat_plan;
    }

    public void setSeat_plan(seat_plan[] seat_plan) {
        this.seat_plan = seat_plan;
    }

//    private String[] booked_seats;


    public String getHall_name ()
    {
        return hall_name;
    }

    public void setHall_name (String hall_name)
    {
        this.hall_name = hall_name;
    }

    public String getCols ()
    {
        return cols;
    }

    public void setCols (String cols)
    {
        this.cols = cols;
    }


  /*  public String[] getBooked_seats ()
    {
        return booked_seats;
    }

    public void setBooked_seats (String[] booked_seats)
    {
        this.booked_seats = booked_seats;
    }*/

    public String getHall_id ()
    {
        return hall_id;
    }

    public void setHall_id (String hall_id)
    {
        this.hall_id = hall_id;
    }

    public String getShow_id ()
    {
        return show_id;
    }
    
    public void setShow_id (String show_id)
    {
        this.show_id = show_id;
    }

    public String getRows ()
    {
        return rows;
    }

    public void setRows (String rows)
    {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "BookMeCinemaTicketSeatPlan{" +
                "show_id='" + show_id + '\'' +
                ", hall_id='" + hall_id + '\'' +
                ", hall_name='" + hall_name + '\'' +
                ", rows='" + rows + '\'' +
                ", cols='" + cols + '\'' +
                ", seat_plan=" + Arrays.toString(seat_plan) +
                '}';
    }
}