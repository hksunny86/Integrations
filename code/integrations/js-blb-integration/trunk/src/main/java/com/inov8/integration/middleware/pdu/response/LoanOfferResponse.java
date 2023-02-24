package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "Code",
        "Message",
        "IdentityValue",
        "IdentityType",
        "OrigSource",
        "ReceivedTimestamp",
        "SourceRequestId",
        "ExternalLoanId",
        "InternalLoanId",
        "AdvanceOfferId",
        "OfferName",
        "HashData",
})
public class LoanOfferResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("IdentityValue")
    private String identityValue;
    @JsonProperty("IdentityType")
    private String identityType;
    @JsonProperty("OrigSource")
    private String origSource;
    @JsonProperty("ReceivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("SourceRequestId")
    private String sourceRequestId;
    @JsonProperty("ExternalLoanId")
    private String externalLoanId;
    @JsonProperty("InternalLoanId")
    private String internalLoanId;
    @JsonProperty("AdvanceOfferId")
    private String advanceOfferId;
    @JsonProperty("OfferName")
    private String offerName;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getSourceRequestId() {
        return sourceRequestId;
    }

    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    public String getExternalLoanId() {
        return externalLoanId;
    }

    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
    }

    public String getInternalLoanId() {
        return internalLoanId;
    }

    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    public String getAdvanceOfferId() {
        return advanceOfferId;
    }

    public void setAdvanceOfferId(String advanceOfferId) {
        this.advanceOfferId = advanceOfferId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
