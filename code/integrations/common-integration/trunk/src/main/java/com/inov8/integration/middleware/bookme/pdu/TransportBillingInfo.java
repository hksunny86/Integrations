package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportBillingInfo implements Serializable {

    @JsonProperty("net_amount")
    private String netAmount;

    @JsonProperty("handling_charges")
    private String handlingCharges;

    @JsonProperty("total_amount")
    private String totalAmount;

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getHandlingCharges() {
        return handlingCharges;
    }

    public void setHandlingCharges(String handlingCharges) {
        this.handlingCharges = handlingCharges;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "TransportBillingInfo{" +
                "netAmount='" + netAmount + '\'' +
                ", handlingCharges='" + handlingCharges + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                '}';
    }
}
