package com.inov8.integration.webservice.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "QR",
        "UUID",
        "UETR",
        "Amount",
        "Expiry",
        "CreatedDate"
})
public class StaticQr implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("QR")
    private String qr;
    @JsonProperty("UUID")
    private String uuid;
    @JsonProperty("UETR")
    private String uetr;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("Expiry")
    private String expiry;
    @JsonProperty("CreatedDate")
    private String createdDate;

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUetr() {
        return uetr;
    }

    public void setUetr(String uetr) {
        this.uetr = uetr;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
