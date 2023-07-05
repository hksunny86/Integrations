package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "ProofOfProfession",
        "SourceOfIncome",
        "HashData"
})
public class CustomerTransactionVerificationResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("ProofOfProfession")
    private String proofOfProfession;
    @JsonProperty("SourceOfIncome")
    private String sourceOfIncome;
    @JsonProperty("HashData")
    private String hashData;

    @JsonProperty("Rrn")
    public String getRrn() {
        return rrn;
    }

    @JsonProperty("Rrn")
    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @JsonProperty("ResponseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("ResponseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("ResponseDescription")
    public String getResponseDescription() {
        return responseDescription;
    }

    @JsonProperty("ResponseDescription")
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @JsonProperty("ResponseDateTime")
    public String getResponseDateTime() {
        return responseDateTime;
    }

    @JsonProperty("ResponseDateTime")
    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    @JsonProperty("ProofOfProfession")
    public String getProofOfProfession() {
        return proofOfProfession;
    }

    @JsonProperty("ProofOfProfession")
    public void setProofOfProfession(String proofOfProfession) {
        this.proofOfProfession = proofOfProfession;
    }

    @JsonProperty("SourceOfIncome")
    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    @JsonProperty("SourceOfIncome")
    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    @JsonProperty("HashData")
    public String getHashData() {
        return hashData;
    }

    @JsonProperty("HashData")
    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

}