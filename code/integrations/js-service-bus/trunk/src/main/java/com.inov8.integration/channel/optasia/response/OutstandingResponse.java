package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identityValue",
        "identityType",
        "origSource",
        "receivedTimestamp",
        "outstandingPerCurrency"
})
public class OutstandingResponse extends Response {

    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("outstandingPerCurrency")
    private List<OutstandingPerCurrency> outstandingPerCurrency;
    private String responseCode;
    private String responseDescription;
    private Map<String, List<?>> collectionOfList = new HashMap();

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

    @JsonProperty("identityValue")
    public String getIdentityValue() {
        return identityValue;
    }

    @JsonProperty("identityValue")
    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    @JsonProperty("identityType")
    public String getIdentityType() {
        return identityType;
    }

    @JsonProperty("identityType")
    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    @JsonProperty("origSource")
    public String getOrigSource() {
        return origSource;
    }

    @JsonProperty("origSource")
    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    @JsonProperty("receivedTimestamp")
    public String getReceivedTimestamp() {
        return receivedTimestamp;
    }

    @JsonProperty("receivedTimestamp")
    public void setReceivedTimestamp(String receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    @JsonProperty("outstandingPerCurrency")
    public List<OutstandingPerCurrency> getOutstandingPerCurrency() {
        return outstandingPerCurrency;
    }

    @JsonProperty("outstandingPerCurrency")
    public void setOutstandingPerCurrency(List<OutstandingPerCurrency> outstandingPerCurrency) {
        this.outstandingPerCurrency = outstandingPerCurrency;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        i8SBSwitchControllerResponseVO.setResponseCode("00");
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());
        for (int i = 0; i < outstandingPerCurrency.size(); i++) {
            i8SBSwitchControllerResponseVO.setCurrencyCode(outstandingPerCurrency.get(i).getCurrencyCode());
            i8SBSwitchControllerResponseVO.setNumOutstandingLoans(outstandingPerCurrency.get(i).getNumOutstandingLoans());
            i8SBSwitchControllerResponseVO.setTotalGross(outstandingPerCurrency.get(i).getTotalGross());
            i8SBSwitchControllerResponseVO.setTotalPrincipal(outstandingPerCurrency.get(i).getTotalPrincipal());
            i8SBSwitchControllerResponseVO.setTotalSetupFees(outstandingPerCurrency.get(i).getTotalSetupFees());
            i8SBSwitchControllerResponseVO.setTotalInterest(outstandingPerCurrency.get(i).getTotalInterest());
            i8SBSwitchControllerResponseVO.setTotalInterestVAT(outstandingPerCurrency.get(i).getTotalInterestVAT());
            i8SBSwitchControllerResponseVO.setTotalCharges(outstandingPerCurrency.get(i).getTotalCharges());
            i8SBSwitchControllerResponseVO.setTotalChargesVAT(outstandingPerCurrency.get(i).getTotalChargesVAT());
            i8SBSwitchControllerResponseVO.setTotalPendingLoans(outstandingPerCurrency.get(i).getTotalPendingLoans());
            i8SBSwitchControllerResponseVO.setTotalPendingRecoveries(outstandingPerCurrency.get(i).getTotalPendingRecoveries());
            collectionOfList.put("OutstandingPerCurrency", outstandingPerCurrency);
            i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

        }
        return i8SBSwitchControllerResponseVO;
    }
}

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
class OutstandingPerCurrency {

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