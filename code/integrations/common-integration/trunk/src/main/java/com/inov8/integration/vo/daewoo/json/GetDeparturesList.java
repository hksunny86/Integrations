package com.inov8.integration.vo.daewoo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetDeparturesList {

    @JsonProperty("origin_city_id")
    private String origin_city_id;
    @JsonProperty("origin_city_name")
    private String origin_city_name;

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

    public String getOrigin_city_id() {
        return origin_city_id;
    }

    public void setOrigin_city_id(String origin_city_id) {
        this.origin_city_id = origin_city_id;
    }

    public String getOrigin_city_name() {
        return origin_city_name;
    }

    public void setOrigin_city_name(String origin_city_name) {
        this.origin_city_name = origin_city_name;
    }
}
