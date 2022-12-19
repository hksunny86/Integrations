package com.inov8.integration.vo.careem.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CareemResponseVO implements Serializable {
    private static final long serialVersionUID = 2000475637833146539L;

    private String code;
    private String message;

    @JsonProperty(value = "responseCode", access = JsonProperty.Access.READ_ONLY)
    public String getCode() {
        return (code != null) ? code : "00";
    }

    @JsonProperty(value = "code", access = JsonProperty.Access.WRITE_ONLY)
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty(value = "responseDescription", access = JsonProperty.Access.READ_ONLY)
    public String getMessage() {
        return (message != null) ? message : "SUCCESS";
    }

    @JsonProperty(value = "message", access = JsonProperty.Access.WRITE_ONLY)
    public void setMessage(String message) {
        this.message = message;
    }
}
