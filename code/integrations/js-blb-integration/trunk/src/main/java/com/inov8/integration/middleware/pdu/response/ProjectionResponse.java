package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.webservice.optasiaVO.*;

import java.io.Serializable;
import java.util.List;

@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "IdentityValue",
        "IdentityType",
        "OrigSource",
        "ReceivedTimestamp",
        "LoanOffer",
        "MaturityDetails",
        "PeriodsProjection",
        "HashData",
})
public class ProjectionResponse implements Serializable {

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
    @JsonProperty("LoanOffer")
    private List<LoanOffers> loanOffersList;
    @JsonProperty("MaturityDetails")
    private List<MaturityDetails> maturityDetailsList;
    @JsonProperty("PeriodsProjection")
    private List<PeriodsProjection> periodsProjectionList;
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

    public List<LoanOffers> getLoanOffersList() {
        return loanOffersList;
    }

    public void setLoanOffersList(List<LoanOffers> loanOffersList) {
        this.loanOffersList = loanOffersList;
    }

    public List<MaturityDetails> getMaturityDetailsList() {
        return maturityDetailsList;
    }

    public void setMaturityDetailsList(List<MaturityDetails> maturityDetailsList) {
        this.maturityDetailsList = maturityDetailsList;
    }

    public List<PeriodsProjection> getPeriodsProjectionList() {
        return periodsProjectionList;
    }

    public void setPeriodsProjectionList(List<PeriodsProjection> periodsProjectionList) {
        this.periodsProjectionList = periodsProjectionList;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
