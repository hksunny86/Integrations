package com.inov8.integration.debitCard.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.vo.CardType;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "MobileNumber",
        "Cnic",
        "Charges",
        "TransactionType",
        "CardTypes",
        "Reserved1",
        "Reserved2",
        "HashData"
})
public class AppRebrandDebitCardIssuanceInquiryResponse implements Serializable {

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("MobileNumber")
    private String mobileNumber;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("Charges")
    private String charges;
    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("CardTypes")
    private List<CardType> cardTypes;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("HashData")
    private String hashData;


    public List<CardType> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(List<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
