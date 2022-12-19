package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DestinationCity implements Serializable{

    private static final long serialVersionUID = -6340922030864311382L;

    @JsonProperty("destination_city_id")
	private String cityId;
	
	@JsonProperty("destination_city_name")
	private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "DestinationCity{" +
                "cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
