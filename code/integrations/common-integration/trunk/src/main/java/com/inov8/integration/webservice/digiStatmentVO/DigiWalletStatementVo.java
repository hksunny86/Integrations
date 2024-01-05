package com.inov8.integration.webservice.digiStatmentVO;


import java.io.Serializable;

public class DigiWalletStatementVo implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String mobileNumber;
    private String product;
    private String transactionTime;
    private String transactionId;
    private String param1, param2, param3, param4;
    private String value1, value2, value3, value4;
//    private String fromAccount;
//    private String fromTitle;
//    private String toAccount;
//    private String toTitle;
    private String fromEntity;
    private String toEntity;
    private String transactionAmount;
    private String transactionType;
    private String debitAmount;
    private String creditAmount;
    private String wht;
    private String fed;
    private String tax;
    private String feeInclusive;
    private String feeExclusive;
    private String totalAmount;
    private String balanceAfterTransaction;
    private String isReversal;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    //    public String getFromAccount() {
//        return fromAccount;
//    }
//
//    public void setFromAccount(String fromAccount) {
//        this.fromAccount = fromAccount;
//    }
//
//    public String getFromTitle() {
//        return fromTitle;
//    }
//
//    public void setFromTitle(String fromTitle) {
//        this.fromTitle = fromTitle;
//    }
//
//    public String getToAccount() {
//        return toAccount;
//    }
//
//    public void setToAccount(String toAccount) {
//        this.toAccount = toAccount;
//    }
//
//    public String getToTitle() {
//        return toTitle;
//    }
//
//    public void setToTitle(String toTitle) {
//        this.toTitle = toTitle;
//    }

    public String getFromEntity() {
        return fromEntity;
    }

    public void setFromEntity(String fromEntity) {
        this.fromEntity = fromEntity;
    }

    public String getToEntity() {
        return toEntity;
    }

    public void setToEntity(String toEntity) {
        this.toEntity = toEntity;
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

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getWht() {
        return wht;
    }

    public void setWht(String wht) {
        this.wht = wht;
    }

    public String getFed() {
        return fed;
    }

    public void setFed(String fed) {
        this.fed = fed;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getFeeInclusive() {
        return feeInclusive;
    }

    public void setFeeInclusive(String feeInclusive) {
        this.feeInclusive = feeInclusive;
    }

    public String getFeeExclusive() {
        return feeExclusive;
    }

    public void setFeeExclusive(String feeExclusive) {
        this.feeExclusive = feeExclusive;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(String balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public String getIsReversal() {
        return isReversal;
    }

    public void setIsReversal(String isReversal) {
        this.isReversal = isReversal;
    }
}
