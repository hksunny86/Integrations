package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Umar on 3/9/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowDates implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonProperty("date")
    private String bookingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowDates showDates = (ShowDates) o;

        return bookingDate != null ? bookingDate.equals(showDates.bookingDate) : showDates.bookingDate == null;

    }

    @Override
    public int hashCode() {
        return bookingDate != null ? bookingDate.hashCode() : 0;
    }

    @JsonProperty("show_id")

    private String showId;


    @Override
    public String toString() {
        return "ShowDates{" +
                "date='" + bookingDate + '\'' +
                ", show_id=" + showId +
                '}';
    }


    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

}
