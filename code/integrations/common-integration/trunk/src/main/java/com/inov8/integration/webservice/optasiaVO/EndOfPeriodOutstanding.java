package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dueDate",
        "currencyCode",
        "totalGross",
        "totalPrincipal",
        "totalExpenses",
        "totalSetupFees",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "totalOneOffCharges",
        "totalRecurringCharges"
})
public class EndOfPeriodOutstanding implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("dueDate")
    private String dueDate;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("totalGross")
    private Float totalGross;
    @JsonProperty("totalPrincipal")
    private Float totalPrincipal;
    @JsonProperty("totalExpenses")
    private Float totalExpenses;
    @JsonProperty("totalSetupFees")
    private Float totalSetupFees;
    @JsonProperty("totalInterest")
    private Float totalInterest;
    @JsonProperty("totalInterestVAT")
    private Float totalInterestVAT;
    @JsonProperty("totalCharges")
    private Float totalCharges;
    @JsonProperty("totalChargesVAT")
    private Float totalChargesVAT;
    @JsonProperty("totalOneOffCharges")
    private Float totalOneOffCharges;
    @JsonProperty("totalRecurringCharges")
    private Float totalRecurringCharges;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("totalGross")
    public Float getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(Float totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalPrincipal")
    public Float getTotalPrincipal() {
        return totalPrincipal;
    }

    @JsonProperty("totalPrincipal")
    public void setTotalPrincipal(Float totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    @JsonProperty("totalExpenses")
    public Float getTotalExpenses() {
        return totalExpenses;
    }

    @JsonProperty("totalExpenses")
    public void setTotalExpenses(Float totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    @JsonProperty("totalSetupFees")
    public Float getTotalSetupFees() {
        return totalSetupFees;
    }

    @JsonProperty("totalSetupFees")
    public void setTotalSetupFees(Float totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
    }

    @JsonProperty("totalInterest")
    public Float getTotalInterest() {
        return totalInterest;
    }

    @JsonProperty("totalInterest")
    public void setTotalInterest(Float totalInterest) {
        this.totalInterest = totalInterest;
    }

    @JsonProperty("totalInterestVAT")
    public Float getTotalInterestVAT() {
        return totalInterestVAT;
    }

    @JsonProperty("totalInterestVAT")
    public void setTotalInterestVAT(Float totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    @JsonProperty("totalCharges")
    public Float getTotalCharges() {
        return totalCharges;
    }

    @JsonProperty("totalCharges")
    public void setTotalCharges(Float totalCharges) {
        this.totalCharges = totalCharges;
    }

    @JsonProperty("totalChargesVAT")
    public Float getTotalChargesVAT() {
        return totalChargesVAT;
    }

    @JsonProperty("totalChargesVAT")
    public void setTotalChargesVAT(Float totalChargesVAT) {
        this.totalChargesVAT = totalChargesVAT;
    }

    @JsonProperty("dueDate")
    public String getDueDate() {
        return dueDate;
    }

    @JsonProperty("dueDate")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty("totalOneOffCharges")
    public Float getTotalOneOffCharges() {
        return totalOneOffCharges;
    }

    @JsonProperty("totalOneOffCharges")
    public void setTotalOneOffCharges(Float totalOneOffCharges) {
        this.totalOneOffCharges = totalOneOffCharges;
    }


    @JsonProperty("totalRecurringCharges")
    public Float getTotalRecurringCharges() {
        return totalRecurringCharges;
    }

    @JsonProperty("totalRecurringCharges")
    public void setTotalRecurringCharges(Float totalRecurringCharges) {
        this.totalRecurringCharges = totalRecurringCharges;
    }
}
