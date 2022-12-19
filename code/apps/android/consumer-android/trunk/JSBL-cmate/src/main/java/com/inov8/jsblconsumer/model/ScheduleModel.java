package com.inov8.jsblconsumer.model;

import java.io.Serializable;

public class ScheduleModel implements Serializable {
    public int flowId;
    public String strProductId, rcmob, strAccountNumber, strAccountTitle,
            bankImd, strCommissionAmount, strCommissionAmountF, strCharges,
            strChargesF, strTotalAmount, strTotalAmountF, strAmount, strAmountF,
            bankName, branchName, iban, crDr, coreactl, purposeCode, strReceiverMobileNumber,
            strSenderMobileNumber, transactionPuropseName, currency;

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public String getStrProductId() {
        return strProductId;
    }

    public void setStrProductId(String strProductId) {
        this.strProductId = strProductId;
    }

    public String getRcmob() {
        return rcmob;
    }

    public void setRcmob(String rcmob) {
        this.rcmob = rcmob;
    }

    public String getStrAccountNumber() {
        return strAccountNumber;
    }

    public void setStrAccountNumber(String strAccountNumber) {
        this.strAccountNumber = strAccountNumber;
    }

    public String getStrAccountTitle() {
        return strAccountTitle;
    }

    public void setStrAccountTitle(String strAccountTitle) {
        this.strAccountTitle = strAccountTitle;
    }

    public String getBankImd() {
        return bankImd;
    }

    public void setBankImd(String bankImd) {
        this.bankImd = bankImd;
    }

    public String getStrCommissionAmount() {
        return strCommissionAmount;
    }

    public void setStrCommissionAmount(String strCommissionAmount) {
        this.strCommissionAmount = strCommissionAmount;
    }

    public String getStrCommissionAmountF() {
        return strCommissionAmountF;
    }

    public void setStrCommissionAmountF(String strCommissionAmountF) {
        this.strCommissionAmountF = strCommissionAmountF;
    }

    public String getStrCharges() {
        return strCharges;
    }

    public void setStrCharges(String strCharges) {
        this.strCharges = strCharges;
    }

    public String getStrChargesF() {
        return strChargesF;
    }

    public void setStrChargesF(String strChargesF) {
        this.strChargesF = strChargesF;
    }

    public String getStrTotalAmount() {
        return strTotalAmount;
    }

    public void setStrTotalAmount(String strTotalAmount) {
        this.strTotalAmount = strTotalAmount;
    }

    public String getStrTotalAmountF() {
        return strTotalAmountF;
    }

    public void setStrTotalAmountF(String strTotalAmountF) {
        this.strTotalAmountF = strTotalAmountF;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }

    public String getStrAmountF() {
        return strAmountF;
    }

    public void setStrAmountF(String strAmountF) {
        this.strAmountF = strAmountF;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCrDr() {
        return crDr;
    }

    public void setCrDr(String crDr) {
        this.crDr = crDr;
    }

    public String getCoreactl() {
        return coreactl;
    }

    public void setCoreactl(String coreactl) {
        this.coreactl = coreactl;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getStrReceiverMobileNumber() {
        return strReceiverMobileNumber;
    }

    public void setStrReceiverMobileNumber(String strReceiverMobileNumber) {
        this.strReceiverMobileNumber = strReceiverMobileNumber;
    }

    public String getStrSenderMobileNumber() {
        return strSenderMobileNumber;
    }

    public void setStrSenderMobileNumber(String strSenderMobileNumber) {
        this.strSenderMobileNumber = strSenderMobileNumber;
    }

    public String getTransactionPuropseName() {
        return transactionPuropseName;
    }

    public void setTransactionPuropseName(String transactionPuropseName) {
        this.transactionPuropseName = transactionPuropseName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
