package com.inov8.integration.middleware.bookme.pdu;

/**
 * Created by Umar on 3/9/2016.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Cinema implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("cinema_id")
    private String cinemaId;
    @JsonProperty("cinema_name")
    private String cinemaName;
    @JsonProperty("cinema_type")
    private String cinema_type;

    @Override
    public String toString() {
        return "Cinema{" +
                "cinemaId='" + cinemaId + '\'' +
                ", cinemaName='" + cinemaName + '\'' +
                ", cinema_type=" + cinema_type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cinema cinema = (Cinema) o;

        return cinemaId.equals(cinema.cinemaId);

    }

    @Override
    public int hashCode() {
        return cinemaId.hashCode();
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinema_type() {
        return cinema_type;
    }

    public void setCinema_type(String cinema_type) {
        this.cinema_type = cinema_type;
    }


}
