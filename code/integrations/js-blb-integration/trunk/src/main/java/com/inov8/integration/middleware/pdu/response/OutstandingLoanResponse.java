package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.webservice.optasiaVO.LoansPerState;

import java.io.Serializable;
import java.util.List;

@JsonPropertyOrder({
        "rrn",
        "responseCode",
        "responseDescription",
        "responseDateTime",
        "outStandingAmount",
        "serviceFee",
        "totalAmount",
        "disbursementDate",
        "maturityDate",
        "duration",
        "hashData",
})
public class OutstandingLoanResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private String responseDescription;
    @JsonProperty("responseDateTime")
    private String responseDateTime;
    @JsonProperty("outStandingAmount")
    private String outStandingAmount;
    @JsonProperty("serviceFee")
    private String serviceFee;
    @JsonProperty("totalAmount")
    private String totalAmount;
    @JsonProperty("disbursementDate")
    private String disbursementDate;
    @JsonProperty("maturityDate")
    private String maturityDate;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("hashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(String outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(String disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
