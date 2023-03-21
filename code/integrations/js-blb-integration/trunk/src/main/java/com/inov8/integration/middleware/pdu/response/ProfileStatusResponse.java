package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@JsonPropertyOrder({
        "rrn",
        "responseCode",
        "responseDescription",
        "dateTime",
        "walletType",
        "walletStatus",
        "taxRegime",
        "lienStatus",
        "hashData",
})
public class ProfileStatusResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private String responseDescription;
    @JsonProperty("dateTime")
    private String responseDateTime;
    @JsonProperty("walletType")
    private String walletType;
    @JsonProperty("walletStatus")
    private String walletStatus;
    @JsonProperty("taxRegime")
    private String taxRegime;
    @JsonProperty("lienStatus")
    private String lienStatus;
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

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(String walletStatus) {
        this.walletStatus = walletStatus;
    }

    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(String taxRegime) {
        this.taxRegime = taxRegime;
    }

    public String getLienStatus() {
        return lienStatus;
    }

    public void setLienStatus(String lienStatus) {
        this.lienStatus = lienStatus;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
