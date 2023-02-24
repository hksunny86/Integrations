package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.io.Serializable;

@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "IdentityValue",
        "IdentityType",
        "OrigSource",
        "ReceivedTimestamp",
        "OfferClass",
        "OfferName",
        "CurrencyCode",
        "PrincipalFrom",
        "PrincipalTo",
        "SetupFees",
        "CommodityType",
        "LoanPlanId",
        "LoanPlanName",
        "LoanProductGroup",
        "MaturityDuration",
        "InterestType",
        "InterestValue",
        "InterestVAT",
        "DaysOffset",
        "Interval",
        "ChargeName",
        "ChargeType",
        "ChargeValue",
        "ChargeVAT",
        "PeriodIndex",
        "PeriodType",
        "PeriodStartTimestamp",
        "PeriodEndTimestamp",
        "PeriodStartDayOfLoanIndex",
        "PeriodEndDayOfLoanIndex",
        "Principal",
        "TotalExpenses",
        "TotalGross",
        "TotalInterest",
        "TotalInterestVAT",
        "TotalCharges",
        "TotalChargesVAT",
        "ChargeAmount",
        "ChargesVAT",
        "DayOfLoan",
        "Date",
        "Gross",
        "Net",
        "Vat",
        "Name",
        "HashData",
})
public class OutstandingResponse implements Serializable {

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
    @JsonProperty("CurrencyCode")
    private String currencyCode;
    @JsonProperty("NumOutstandingLoans")
    private String numOutstandingLoans;
    @JsonProperty("TotalGross")
    private String totalGross;
    @JsonProperty("TotalPrincipal")
    private String totalPrincipal;
    @JsonProperty("TotalSetupFees")
    private String totalSetupFees;
    @JsonProperty("TotalInterest")
    private String totalInterest;
    @JsonProperty("TotalInterestVAT")
    private String totalInterestVAT;
    @JsonProperty("TotalCharges")
    private String totalCharges;
    @JsonProperty("TotalChargesVAT")
    private String totalChargesVAT;
    @JsonProperty("TotalPendingLoans")
    private String totalPendingLoans;
    @JsonProperty("TotalPendingRecoveries")
    private String totalPendingRecoveries;
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

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
