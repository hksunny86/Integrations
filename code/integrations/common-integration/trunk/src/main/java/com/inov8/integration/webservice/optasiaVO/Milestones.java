package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dayOfLoan",
        "date",
        "interestAdjustment",
        "chargeAdjustments",
        "principal",
        "totalExpenses",
        "totalGross",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT"
})
public class Milestones implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("dayOfLoan")
    private String dayOfLoan;
    @JsonProperty("date")
    private String date;
    @JsonProperty("interestAdjustment")
    private List<InterestAdjustment> interestAdjustmentList;
    @JsonProperty("chargeAdjustments")
    private List<ChargeAdjustments> chargeAdjustmentsList;
    @JsonProperty("principal")
    private String principal;
    @JsonProperty("totalExpenses")
    private String totalExpenses;
    @JsonProperty("totalGross")
    private String totalGross;
    @JsonProperty("totalInterest")
    private String totalInterest;
    @JsonProperty("totalInterestVAT")
    private String totalInterestVAT;
    @JsonProperty("totalCharges")
    private String totalCharges;
    @JsonProperty("totalChargesVAT")
    private String totalChargesVAT;

    public String getDayOfLoan() {
        return dayOfLoan;
    }

    public void setDayOfLoan(String dayOfLoan) {
        this.dayOfLoan = dayOfLoan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<InterestAdjustment> getInterestAdjustmentList() {
        return interestAdjustmentList;
    }

    public void setInterestAdjustmentList(List<InterestAdjustment> interestAdjustmentList) {
        this.interestAdjustmentList = interestAdjustmentList;
    }

    public List<ChargeAdjustments> getChargeAdjustmentsList() {
        return chargeAdjustmentsList;
    }

    public void setChargeAdjustmentsList(List<ChargeAdjustments> chargeAdjustmentsList) {
        this.chargeAdjustmentsList = chargeAdjustmentsList;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(String totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public String getTotalGross() {
        return totalGross;
    }

    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
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
}
