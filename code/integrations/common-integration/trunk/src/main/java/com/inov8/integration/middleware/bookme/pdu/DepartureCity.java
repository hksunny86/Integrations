package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartureCity implements Serializable{

	@JsonProperty("origin_city_id")
	private String cityId;
	
	@JsonProperty("origin_city_name")
	private String cityName;

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
    public String toString() {
        return "DepartureCity{" +
                "cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
