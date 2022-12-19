package com.inov8.integration.middleware.pdu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Beneficiary implements Serializable {

    @JsonProperty("cnic")
    private String cnic;

    @JsonProperty("status")
    private String status;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("system_type_id")
    private String systemTypeId;

    @JsonProperty("bop_timestamp")
    private String bopTimestamp;

    @JsonProperty("visit_timestamp")
    private String visitTimestamp;

    @JsonIgnore
    private String statusDescription;

    public String getCnic() { return cnic; }

    public void setCnic(String cnic) { this.cnic = cnic; }

    public String getStatusCode() { return statusCode; }

    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }

    public String getSystemTypeId() { return systemTypeId; }

    public void setSystemTypeId(String systemTypeId) { this.systemTypeId = systemTypeId; }

    public String getBopTimestamp() { return bopTimestamp; }

    public void setBopTimestamp(String bopTimestamp) { this.bopTimestamp = bopTimestamp; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisitTimestamp() {
        return visitTimestamp;
    }

    public void setVisitTimestamp(String visitTimestamp) {
        this.visitTimestamp = visitTimestamp;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
