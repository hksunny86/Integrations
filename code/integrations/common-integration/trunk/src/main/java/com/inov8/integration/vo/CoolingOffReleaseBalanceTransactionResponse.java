package com.inov8.integration.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ReleaseBalanceTransactionResponse")
public class CoolingOffReleaseBalanceTransactionResponse implements Serializable {
    private static final long serialVersionUID = -4994580002925333799L;

    @XmlElement(name = "TransactionStatus")
    private String transactionStatus;
    @XmlElement(name = "Balance")
    private String balance;
    @XmlElement(name = "CreditAmount")
    private String creditAmount;
    @XmlElement(name = "DebitAmount")
    private String debitAmount;
    @XmlElement(name = "FromAccount")
    private String fromAccount;
    @XmlElement(name = "ProductName")
    private String productName;
    @XmlElement(name = "ToAccount")
    private String toAccount;
    @XmlElement(name = "ToBank")
    private String toBank;
    @XmlElement(name = "TotalAmount")
    private String totalAmount;
    @XmlElement(name = "Charges")
    private String charges;

    @XmlElement(name = "TransactionId")
    private String transactionId;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
    @XmlElement(name = "TransactionType")
    private String transactionType;

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToBank() {
        return toBank;
    }

    public void setToBank(String toBank) {
        this.toBank = toBank;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
