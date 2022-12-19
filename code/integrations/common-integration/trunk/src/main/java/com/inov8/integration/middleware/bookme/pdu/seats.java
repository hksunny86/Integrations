package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class seats
{
    private String seat_id;

	private String seat_row_name;

    private String seat_number;

    private String seat_type;
    private String status;

    @Override
    public String toString() {
        return "seats{" +
                "seat_id='" + seat_id + '\'' +
                ", seat_row_name='" + seat_row_name + '\'' +
                ", seat_number='" + seat_number + '\'' +
                ", seat_type='" + seat_type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getSeat_id ()
    {
        return seat_id;
    }

    public void setSeat_id (String seat_id)
    {
        this.seat_id = seat_id;
    }

    public String getSeat_type ()
    {
        return seat_type;
    }

    public void setSeat_type (String seat_type)
    {
        this.seat_type = seat_type;
    }

    public String getSeat_row_name ()
    {
        return seat_row_name;
    }

    public void setSeat_row_name (String seat_row_name)
    {
        this.seat_row_name = seat_row_name;
    }

    public String getSeat_number ()
    {
        return seat_number;
    }

    public void setSeat_number (String seat_number)
    {
        this.seat_number = seat_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
