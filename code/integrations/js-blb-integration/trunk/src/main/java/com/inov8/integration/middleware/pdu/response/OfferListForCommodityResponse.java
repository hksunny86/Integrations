package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.webservice.optasiaVO.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "IdentityValue",
        "IdentityType",
        "OrigSource",
        "ReceivedTimestamp",
        "EligibilityStatus",
        "LoanOffersByLoanProductGroup",
        "OutstandingStatus"
})
public class OfferListForCommodityResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("IdentityValue")
    private String identityValue;
    @JsonProperty("IdentityType")
    private String identityType;
    @JsonProperty("OrigSource")
    private String origSource;
    @JsonProperty("ReceivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("EligibilityStatus")
    private List<EligibilityStatus> eligibilityStatusList;
    @JsonProperty("LoanOffersByLoanProductGroup")
    private List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroupList;
    @JsonProperty("OutstandingStatus")
    private List<OutstandingStatus> outstandingStatusList;
    @JsonProperty("HashData")
    private String hashData;

    public List<EligibilityStatus> getEligibilityStatusList() {
        return eligibilityStatusList;
    }

    public void setEligibilityStatusList(List<EligibilityStatus> eligibilityStatusList) {
        this.eligibilityStatusList = eligibilityStatusList;
    }

    public List<LoanOffersByLoanProductGroup> getLoanOffersByLoanProductGroupList() {
        return loanOffersByLoanProductGroupList;
    }

    public void setLoanOffersByLoanProductGroupList(List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroupList) {
        this.loanOffersByLoanProductGroupList = loanOffersByLoanProductGroupList;
    }

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


    public String getIdentityValue() {
        return identityValue;
    }

    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getOrigSource() {
        return origSource;
    }

    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    public String getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public void setReceivedTimestamp(String receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }


    public List<OutstandingStatus> getOutstandingStatusList() {
        return outstandingStatusList;
    }

    public void setOutstandingStatusList(List<OutstandingStatus> outstandingStatusList) {
        this.outstandingStatusList = outstandingStatusList;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
