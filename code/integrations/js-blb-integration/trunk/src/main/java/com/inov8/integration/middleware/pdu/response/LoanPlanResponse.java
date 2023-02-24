package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.webservice.optasiaVO.*;

import java.io.Serializable;
import java.util.List;

@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "LoanAmount",
        "DueDatePlan",
        "HashData",
})
public class LoanPlanResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("LoanAmount")
    private List<LoanAmount> loanAmountList;
    @JsonProperty("DueDatePlan")
    private List<DueDatePlan> dueDatePlans;
    @JsonProperty("HashData")
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

    public List<LoanAmount> getLoanAmountList() {
        return loanAmountList;
    }

    public void setLoanAmountList(List<LoanAmount> loanAmountList) {
        this.loanAmountList = loanAmountList;
    }

    public List<DueDatePlan> getDueDatePlans() {
        return dueDatePlans;
    }

    public void setDueDatePlans(List<DueDatePlan> dueDatePlans) {
        this.dueDatePlans = dueDatePlans;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}