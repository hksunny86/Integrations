package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration"
})

public class MaturityDetails__1 implements Serializable {

    @JsonProperty("maturityDuration")
    private Integer maturityDuration;

    @JsonProperty("maturityDuration")
    public Integer getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(Integer maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

}
