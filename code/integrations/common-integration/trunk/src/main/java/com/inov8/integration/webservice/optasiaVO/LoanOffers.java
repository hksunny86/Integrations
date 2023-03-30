package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "offerClass",
        "offerName",
        "currencyCode",
        "principalFrom",
        "principalTo",
        "setupFees",
        "commodityType",
        "loanPlanId",
        "loanPlanName",
        "loanProductGroup",
        "advanceOfferId",
        "principalAmount",
        "maturityDetails"
})
public class LoanOffers implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("offerClass")
    private String offerClass;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("principalFrom")
    private String principalFrom;
    @JsonProperty("principalTo")
    private String principalTo;
    @JsonProperty("setupFees")
    private String setupFees;
    @JsonProperty("commodityType")
    private String commodityType;
    @JsonProperty("loanPlanId")
    private String loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("advanceOfferId")
    private String advanceOfferId;
    @JsonProperty("principalAmount")
    private String principalAmount;
    @JsonProperty("maturityDetails")
    private List<MaturityDetails> maturityDetailsList;

    public List<MaturityDetails> getMaturityDetailsList() {
        return maturityDetailsList;
    }

    public void setMaturityDetailsList(List<MaturityDetails> maturityDetailsList) {
        this.maturityDetailsList = maturityDetailsList;
    }

    public String getAdvanceOfferId() {
        return advanceOfferId;
    }

    public void setAdvanceOfferId(String advanceOfferId) {
        this.advanceOfferId = advanceOfferId;
    }

    public String getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    public String getOfferClass() {
        return offerClass;
    }

    public void setOfferClass(String offerClass) {
        this.offerClass = offerClass;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPrincipalFrom() {
        return principalFrom;
    }

    public void setPrincipalFrom(String principalFrom) {
        this.principalFrom = principalFrom;
    }

    public String getPrincipalTo() {
        return principalTo;
    }

    public void setPrincipalTo(String principalTo) {
        this.principalTo = principalTo;
    }

    public String getSetupFees() {
        return setupFees;
    }

    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
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
}
