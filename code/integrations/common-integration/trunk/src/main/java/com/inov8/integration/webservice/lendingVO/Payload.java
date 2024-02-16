package com.inov8.integration.webservice.lendingVO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String mobileNumber;
    private String transactionId;
    private String amount;
    private String totalOutstanding;
    private String principalOutstanding;
    private String markupOutstanding;
    private String lpChargesOutstanding;
    private String epChargesOutstanding;
    private String dateTime;
    private String loanAmount;
    private String fee;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(String totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public String getPrincipalOutstanding() {
        return principalOutstanding;
    }

    public void setPrincipalOutstanding(String principalOutstanding) {
        this.principalOutstanding = principalOutstanding;
    }

    public String getMarkupOutstanding() {
        return markupOutstanding;
    }

    public void setMarkupOutstanding(String markupOutstanding) {
        this.markupOutstanding = markupOutstanding;
    }

    public String getLpChargesOutstanding() {
        return lpChargesOutstanding;
    }

    public void setLpChargesOutstanding(String lpChargesOutstanding) {
        this.lpChargesOutstanding = lpChargesOutstanding;
    }

    public String getEpChargesOutstanding() {
        return epChargesOutstanding;
    }

    public void setEpChargesOutstanding(String epChargesOutstanding) {
        this.epChargesOutstanding = epChargesOutstanding;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
