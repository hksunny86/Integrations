package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userApp"
})

public class EventReasonDetail implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("userApp")
    private String userApp;

    @JsonProperty("userApp")
    public String getUserApp() {
        return userApp;
    }

    @JsonProperty("userApp")
    public void setUserApp(String userApp) {
        this.userApp = userApp;
    }

}
