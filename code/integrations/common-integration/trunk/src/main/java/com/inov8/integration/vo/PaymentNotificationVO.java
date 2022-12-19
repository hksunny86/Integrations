package com.inov8.integration.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentNotificationVO implements Serializable {

    private static final long serialVersionUID = 7563869840030624352L;
    @JsonProperty("payment_token")
    public String paymentToken;
    @JsonProperty("token_expiry_datetime")
    public String tokenExpiryDatetime;
    @JsonProperty("store_name")
    public String storeName;
    @JsonProperty("response_code")
    public String responseCode;
    @JsonProperty("order_id")
    public String orderId;
    @JsonProperty("order_datetime")
    public String orderDatetime;
    @JsonProperty("paid_datetime")
    public String paidDatetime;
    @JsonProperty("transaction_status")
    public String transactionStatus;
    @JsonProperty("transaction_amount")
    public String transactionAmount;
    @JsonProperty("store_id")
    public String storeId;
    @JsonProperty("msisdn")
    public String msisdn;
    @JsonProperty("payment_method")
    public String paymentMethod;
    @JsonProperty("account_number")
    public String accountNumber;
    @JsonProperty("description")
    public String description;
    @JsonProperty("transaction_id")
    public String transactionId;

    public Object getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getTokenExpiryDatetime() {
        return tokenExpiryDatetime;
    }

    public void setTokenExpiryDatetime(String tokenExpiryDatetime) {
        this.tokenExpiryDatetime = tokenExpiryDatetime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDatetime() {
        return orderDatetime;
    }

    public void setOrderDatetime(String orderDatetime) {
        this.orderDatetime = orderDatetime;
    }

    public String getPaidDatetime() {
        return paidDatetime;
    }

    public void setPaidDatetime(String paidDatetime) {
        this.paidDatetime = paidDatetime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "PaymentNotificationVO{" +
                "paymentToken='" + paymentToken + '\'' +
                ", tokenExpiryDatetime='" + tokenExpiryDatetime + '\'' +
                ", storeName='" + storeName + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderDatetime='" + orderDatetime + '\'' +
                ", paidDatetime='" + paidDatetime + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", transactionAmount='" + transactionAmount + '\'' +
                ", storeId='" + storeId + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", description='" + description + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
