package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "interestName",
        "interestType",
        "interestValue",
        "interestVAT",
        "daysOffset",
        "interval"
})
public class Interest implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("interestName")
    private String interestName;
    @JsonProperty("interestType")
    private String interestType;
    @JsonProperty("interestValue")
    private String interestValue;
    @JsonProperty("interestVAT")
    private String interestVAT;
    @JsonProperty("daysOffset")
    private String daysOffset;
    @JsonProperty("interval")
    private String interval;

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getInterestValue() {
        return interestValue;
    }

    public void setInterestValue(String interestValue) {
        this.interestValue = interestValue;
    }

    public String getInterestVAT() {
        return interestVAT;
    }

    public void setInterestVAT(String interestVAT) {
        this.interestVAT = interestVAT;
    }

    public String getDaysOffset() {
        return daysOffset;
    }

    public void setDaysOffset(String daysOffset) {
        this.daysOffset = daysOffset;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
