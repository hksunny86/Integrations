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
public class OutstandingStatus implements Serializable {

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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getNumOutstandingLoans() {
        return numOutstandingLoans;
    }

    public void setNumOutstandingLoans(String numOutstandingLoans) {
        this.numOutstandingLoans = numOutstandingLoans;
    }

    public String getTotalGross() {
        return totalGross;
    }

    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    public String getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(String totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public String getTotalSetupFees() {
        return totalSetupFees;
    }

    public void setTotalSetupFees(String totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getTotalInterestVAT() {
        return totalInterestVAT;
    }

    public void setTotalInterestVAT(String totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    public String getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    public String getTotalChargesVAT() {
        return totalChargesVAT;
    }

    public void setTotalChargesVAT(String totalChargesVAT) {
        this.totalChargesVAT = totalChargesVAT;
    }

    public String getTotalPendingLoans() {
        return totalPendingLoans;
    }

    public void setTotalPendingLoans(String totalPendingLoans) {
        this.totalPendingLoans = totalPendingLoans;
    }

    public String getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    public void setTotalPendingRecoveries(String totalPendingRecoveries) {
        this.totalPendingRecoveries = totalPendingRecoveries;
    }
}
