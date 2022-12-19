package com.inov8.integration.vo;

import java.io.Serializable;

public class StatementRowVO implements Serializable
{
    private static final long serialVersionUID = -5082807909376814924L;
    private String date;
    private String time;
    private String description;
    private String type;
    private String creditAmountCurrency;
    private String creditAmount;
    private String debitAmountCurrency;
    private String debitAmount;
    private String amountCurrency;
    private String amount;
    private String balance;
    private String instrumentNo;
    private String transactionStatus;
    private String transactionReferenceNumber;

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getAmount()
    {
        return this.amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getBalance()
    {
        return this.balance;
    }

    public void setBalance(String balance)
    {
        this.balance = balance;
    }


    public String getCreditAmountCurrency() {
        return creditAmountCurrency;
    }

    public void setCreditAmountCurrency(String creditAmountCurrency) {
        this.creditAmountCurrency = creditAmountCurrency;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getDebitAmountCurrency() {
        return debitAmountCurrency;
    }

    public void setDebitAmountCurrency(String debitAmountCurrency) {
        this.debitAmountCurrency = debitAmountCurrency;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getInstrumentNo() {
        return instrumentNo;
    }

    public void setInstrumentNo(String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    @Override
    public String toString() {
        return "StatementRowVO{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", creditAmountCurrency='" + creditAmountCurrency + '\'' +
                ", creditAmount='" + creditAmount + '\'' +
                ", debitAmountCurrency='" + debitAmountCurrency + '\'' +
                ", debitAmount='" + debitAmount + '\'' +
                ", amountCurrency='" + amountCurrency + '\'' +
                ", amount='" + amount + '\'' +
                ", balance='" + balance + '\'' +
                ", instrumentNo='" + instrumentNo + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", transactionReferenceNumber='" + transactionReferenceNumber + '\'' +
                '}';
    }
}
