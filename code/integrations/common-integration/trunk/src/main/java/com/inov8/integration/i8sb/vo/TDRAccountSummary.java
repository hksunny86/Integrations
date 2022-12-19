package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("TDRAccountSummary")
public class TDRAccountSummary implements Serializable {

    private final static long serialVersionUID = 1L;

    private String branchName;
    private String acctNumber;
    private String acctTitle;
    private String productType;
    private String tenure;
    private String amount;
    private String profitRate;
    private String bookingDate;
    private String maturityDate;
    private String currency;
    private String maturityOption;
    private String payeeAccNr;
    private String expectedProfit;
    private String nextProfitDisbursementDate;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAcctNumber() {
        return acctNumber;
    }

    public void setAcctNumber(String acctNumber) {
        this.acctNumber = acctNumber;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAcctTitle() {
        return acctTitle;
    }

    public void setAcctTitle(String acctTitle) {
        this.acctTitle = acctTitle;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMaturityOption() {
        return maturityOption;
    }

    public void setMaturityOption(String maturityOption) {
        this.maturityOption = maturityOption;
    }

    public String getPayeeAccNr() {
        return payeeAccNr;
    }

    public void setPayeeAccNr(String payeeAccNr) {
        this.payeeAccNr = payeeAccNr;
    }

    public String getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(String expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public String getNextProfitDisbursementDate() {
        return nextProfitDisbursementDate;
    }

    public void setNextProfitDisbursementDate(String nextProfitDisbursementDate) {
        this.nextProfitDisbursementDate = nextProfitDisbursementDate;
    }
}
