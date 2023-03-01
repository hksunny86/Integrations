package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currentPeriod",
        "daysLeftInPeriod",
        "nextPeriod"
})
public class Plan implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("currentPeriod")
    private String currentPeriod;
    @JsonProperty("daysLeftInPeriod")
    private String daysLeftInPeriod;
    @JsonProperty("nextPeriod")
    private String nextPeriod;

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getDaysLeftInPeriod() {
        return daysLeftInPeriod;
    }

    public void setDaysLeftInPeriod(String daysLeftInPeriod) {
        this.daysLeftInPeriod = daysLeftInPeriod;
    }

    public String getNextPeriod() {
        return nextPeriod;
    }

    public void setNextPeriod(String nextPeriod) {
        this.nextPeriod = nextPeriod;
    }
}
