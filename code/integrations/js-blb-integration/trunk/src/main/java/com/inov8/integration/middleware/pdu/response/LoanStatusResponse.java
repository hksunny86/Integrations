package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
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
        "InternalLoanId",
        "ExternalLoanId",
        "LoanState",
        "LoanTimestamp",
        "LoanReason",
        "OfferName",
        "CommodityType",
        "CurrencyCode",
        "PrincipalAmount",
        "SetupFees",
        "LoanPlanId",
        "LoanPlanName",
        "LoanProductGroup",
        "MaturityDuration",
        "RepaymentsCount",
        "Gross",
        "Principal",
        "Interest",
        "InterestVAT",
        "Charges",
        "ChargesVAT",
        "TotalGross",
        "TotalPrincipal",
        "TotalSetupFees",
        "TotalInterest",
        "TotalInterestVAT",
        "TotalCharges",
        "TotalChargesVAT",
        "TotalPendingRecoveries",
        "CurrentPeriod",
        "DaysLeftInPeriod",
        "NextPeriod",
        "HashData",
})
public class LoanStatusResponse implements Serializable {

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
    @JsonProperty("InternalLoanId")
    private String internalLoanId;
    @JsonProperty("ExternalLoanId")
    private String externalLoanId;
    @JsonProperty("LoanState")
    private String loanState;
    @JsonProperty("LoanTimestamp")
    private String LoanTimestamp;
    @JsonProperty("LoanReason")
    private String loanReason;
    @JsonProperty("OfferName")
    private String offerName;
    @JsonProperty("CommodityType")
    private String commodityType;
    @JsonProperty("CurrencyCode")
    private String currencyCode;
    @JsonProperty("PrincipalAmount")
    private String principalAmount;
    @JsonProperty("SetupFees")
    private String setupFees;
    @JsonProperty("LoanPlanId")
    private String loanPlanId;
    @JsonProperty("LoanPlanName")
    private String loanPlanName;
    @JsonProperty("LoanProductGroup")
    private String loanProductGroup;
    @JsonProperty("MaturityDuration")
    private String maturityDuration;
    @JsonProperty("RepaymentsCount")
    private String repaymentsCount;
    @JsonProperty("Gross")
    private String gross;
    @JsonProperty("Principal")
    private String principal;
    @JsonProperty("Interest")
    private String interest;
    @JsonProperty("InterestVAT")
    private String interestVAT;
    @JsonProperty("Charges")
    private String charges;
    @JsonProperty("ChargesVAT")
    private String chargesVAT;
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
    @JsonProperty("TotalPendingRecoveries")
    private String totalPendingRecoveries;
    @JsonProperty("CurrentPeriod")
    private String currentPeriod;
    @JsonProperty("DaysLeftInPeriod")
    private String daysLeftInPeriod;
    @JsonProperty("NextPeriod")
    private String nextPeriod;
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

    public String getInternalLoanId() {
        return internalLoanId;
    }

    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    public String getExternalLoanId() {
        return externalLoanId;
    }

    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanTimestamp() {
        return LoanTimestamp;
    }

    public void setLoanTimestamp(String loanTimestamp) {
        LoanTimestamp = loanTimestamp;
    }

    public String getLoanReason() {
        return loanReason;
    }

    public void setLoanReason(String loanReason) {
        this.loanReason = loanReason;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    public String getSetupFees() {
        return setupFees;
    }

    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
    }

    public String getLoanPlanId() {
        return loanPlanId;
    }

    public void setLoanPlanId(String loanPlanId) {
        this.loanPlanId = loanPlanId;
    }

    public String getLoanPlanName() {
        return loanPlanName;
    }

    public void setLoanPlanName(String loanPlanName) {
        this.loanPlanName = loanPlanName;
    }

    public String getLoanProductGroup() {
        return loanProductGroup;
    }

    public void setLoanProductGroup(String loanProductGroup) {
        this.loanProductGroup = loanProductGroup;
    }

    public String getMaturityDuration() {
        return maturityDuration;
    }

    public void setMaturityDuration(String maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    public String getRepaymentsCount() {
        return repaymentsCount;
    }

    public void setRepaymentsCount(String repaymentsCount) {
        this.repaymentsCount = repaymentsCount;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestVAT() {
        return interestVAT;
    }

    public void setInterestVAT(String interestVAT) {
        this.interestVAT = interestVAT;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getChargesVAT() {
        return chargesVAT;
    }

    public void setChargesVAT(String chargesVAT) {
        this.chargesVAT = chargesVAT;
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

    public String getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    public void setTotalPendingRecoveries(String totalPendingRecoveries) {
        this.totalPendingRecoveries = totalPendingRecoveries;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getDaysLeftInPeriod() {
        return daysLeftInPeriod;
    }

    public void setDaysLeftInPeriod(String daysLeftInPeriod) {
        this.daysLeftInPeriod = daysLeftInPeriod;
    }

    public String getNextPeriod() {
        return nextPeriod;
    }

    public void setNextPeriod(String nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
