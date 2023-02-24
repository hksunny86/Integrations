package com.inov8.integration.webservice.optasiaVO;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "periodIndex",
        "periodType",
        "periodStartTimemp",
        "periodEndTimestamp",
        "periodStartDayOfLoanIndex",
        "periodEndDayOfLoanIndex",
        "principal",
        "totalExpenses",
        "totalGross",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "totalOneOffCharges",
        "milestones",
})
public class PeriodsProjection implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("periodIndex")
    private String periodIndex;
    @JsonProperty("periodType")
    private String periodType;
    @JsonProperty("periodStartTimemp")
    private String periodStartTimemp;
    @JsonProperty("periodEndTimestamp")
    private String periodEndTimestamp;
    @JsonProperty("periodStartDayOfLoanIndex")
    private String periodStartDayOfLoanIndex;
    @JsonProperty("periodEndDayOfLoanIndex")
    private String periodEndDayOfLoanIndex;
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
    @JsonProperty("totalOneOffCharges")
    private List<TotalOneOffCharges> totalOneOffChargesList;
    @JsonProperty("milestones")
    private List<Milestones> milestonesList;

    public String getPeriodIndex() {
        return periodIndex;
    }

    public void setPeriodIndex(String periodIndex) {
        this.periodIndex = periodIndex;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getPeriodStartTimemp() {
        return periodStartTimemp;
    }

    public void setPeriodStartTimemp(String periodStartTimemp) {
        this.periodStartTimemp = periodStartTimemp;
    }

    public String getPeriodEndTimestamp() {
        return periodEndTimestamp;
    }

    public void setPeriodEndTimestamp(String periodEndTimestamp) {
        this.periodEndTimestamp = periodEndTimestamp;
    }

    public String getPeriodStartDayOfLoanIndex() {
        return periodStartDayOfLoanIndex;
    }

    public void setPeriodStartDayOfLoanIndex(String periodStartDayOfLoanIndex) {
        this.periodStartDayOfLoanIndex = periodStartDayOfLoanIndex;
    }

    public String getPeriodEndDayOfLoanIndex() {
        return periodEndDayOfLoanIndex;
    }

    public void setPeriodEndDayOfLoanIndex(String periodEndDayOfLoanIndex) {
        this.periodEndDayOfLoanIndex = periodEndDayOfLoanIndex;
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

    public List<TotalOneOffCharges> getTotalOneOffChargesList() {
        return totalOneOffChargesList;
    }

    public void setTotalOneOffChargesList(List<TotalOneOffCharges> totalOneOffChargesList) {
        this.totalOneOffChargesList = totalOneOffChargesList;
    }

    public List<Milestones> getMilestonesList() {
        return milestonesList;
    }

    public void setMilestonesList(List<Milestones> milestonesList) {
        this.milestonesList = milestonesList;
    }
}
