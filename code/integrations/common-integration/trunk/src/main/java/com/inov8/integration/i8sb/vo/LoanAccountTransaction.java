package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("LoanAccountTransaction")
public class LoanAccountTransaction implements Serializable {


    private static final long serialVersionUID = -490834030421490664L;

    private String transactionDate;
    private String totalRepayment;
    private String principleAmount;
    private String interestAmount;
    private String LDAmount;
    private String currency;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(String totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public String getPrincipleAmount() {
        return principleAmount;
    }

    public void setPrincipleAmount(String principleAmount) {
        this.principleAmount = principleAmount;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getLDAmount() {
        return LDAmount;
    }

    public void setLDAmount(String LDAmount) {
        this.LDAmount = LDAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
