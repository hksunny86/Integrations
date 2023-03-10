package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currencyCode",
        "numOutstandingLoans",
        "totalGross",
        "totalPrincipal",
        "totalSetupFees",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "totalPendingLoans",
        "totalPendingRecoveries"
})
public class OutstandingPerCurrency implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("numOutstandingLoans")
    private String numOutstandingLoans;
    @JsonProperty("totalGross")
    private String totalGross;
    @JsonProperty("totalPrincipal")
    private String totalPrincipal;
    @JsonProperty("totalSetupFees")
    private String totalSetupFees;
    @JsonProperty("totalInterest")
    private String totalInterest;
    @JsonProperty("totalInterestVAT")
    private String totalInterestVAT;
    @JsonProperty("totalCharges")
    private String totalCharges;
    @JsonProperty("totalChargesVAT")
    private String totalChargesVAT;
    @JsonProperty("totalPendingLoans")
    private String totalPendingLoans;
    @JsonProperty("totalPendingRecoveries")
    private String totalPendingRecoveries;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("numOutstandingLoans")
    public String getNumOutstandingLoans() {
        return numOutstandingLoans;
    }

    @JsonProperty("numOutstandingLoans")
    public void setNumOutstandingLoans(String numOutstandingLoans) {
        this.numOutstandingLoans = numOutstandingLoans;
    }

    @JsonProperty("totalGross")
    public String getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalPrincipal")
    public String getTotalPrincipal() {
        return totalPrincipal;
    }

    @JsonProperty("totalPrincipal")
    public void setTotalPrincipal(String totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    @JsonProperty("totalSetupFees")
    public String getTotalSetupFees() {
        return totalSetupFees;
    }

    @JsonProperty("totalSetupFees")
    public void setTotalSetupFees(String totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
    }

    @JsonProperty("totalInterest")
    public String getTotalInterest() {
        return totalInterest;
    }

    @JsonProperty("totalInterest")
    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    @JsonProperty("totalInterestVAT")
    public String getTotalInterestVAT() {
        return totalInterestVAT;
    }

    @JsonProperty("totalInterestVAT")
    public void setTotalInterestVAT(String totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    @JsonProperty("totalCharges")
    public String getTotalCharges() {
        return totalCharges;
    }

    @JsonProperty("totalCharges")
    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    @JsonProperty("totalChargesVAT")
    public String getTotalChargesVAT() {
        return totalChargesVAT;
    }

    @JsonProperty("totalChargesVAT")
    public void setTotalChargesVAT(String totalChargesVAT) {
        this.totalChargesVAT = totalChargesVAT;
    }

    @JsonProperty("totalPendingLoans")
    public String getTotalPendingLoans() {
        return totalPendingLoans;
    }

    @JsonProperty("totalPendingLoans")
    public void setTotalPendingLoans(String totalPendingLoans) {
        this.totalPendingLoans = totalPendingLoans;
    }

    @JsonProperty("totalPendingRecoveries")
    public String getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    @JsonProperty("totalPendingRecoveries")
    public void setTotalPendingRecoveries(String totalPendingRecoveries) {
        this.totalPendingRecoveries = totalPendingRecoveries;
    }

}
