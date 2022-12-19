package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieCity implements Serializable{

    private static final long serialVersionUID = -7868991586757070099L;

    @JsonProperty("city_id")
    private String cityId;

    @JsonProperty("city_name")
    private String cityName;

    @Override
    public String toString() {
        return "MovieCity{" +
                "cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieCity movieCity = (MovieCity) o;

        return cityId.equals(movieCity.cityId);

    }

    @Override
    public int hashCode() {
        return cityId.hashCode();
    }
}
