package com.inov8.integration.webservice.sbpVO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnPaymentResponsePayload implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private List responseDescription;
    @JsonProperty("BatchId")
    private String batchId;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(List responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
