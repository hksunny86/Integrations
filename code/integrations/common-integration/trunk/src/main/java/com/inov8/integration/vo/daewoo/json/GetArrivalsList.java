package com.inov8.integration.vo.daewoo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetArrivalsList {

    @JsonProperty("destination_city_id")
    private String destinationCityID;
    @JsonProperty("destination_city_name")
    private String destinationCityName;

    @JsonProperty("TERMINAL_CODE")
    private String terminalCode;
    @JsonProperty("TERMINAL_NAME")
    private String terminalName;
    @JsonProperty("API_OUT")
    private String apiOut;

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getApiOut() {
        return apiOut;
    }

    public void setApiOut(String apiOut) {
        this.apiOut = apiOut;
    }

    public String getDestinationCityID() {
        return destinationCityID;
    }

    public void setDestinationCityID(String destinationCityID) {
        this.destinationCityID = destinationCityID;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }
}
