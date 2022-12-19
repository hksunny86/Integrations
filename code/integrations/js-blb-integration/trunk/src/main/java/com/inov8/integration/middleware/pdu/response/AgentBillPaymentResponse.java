package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentBillPaymentResponse")
public class AgentBillPaymentResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "BillAmount")
    private String billAmount;
    @XmlElement(name = "CommissionAmount")
    private String commissionAmount;
    @XmlElement(name = "ConsumerNo")
    private String consumerNo;
    @XmlElement(name = "ConsumerMobileNumber")
    private String consumerMobileNumber;
    @XmlElement(name = "PaymentDateTime")
    private String paymentDateTime;
    @XmlElement(name = "LateBillAmount")
    private String lateBillAmount;
    @XmlElement(name = "ProductName")
    private String productName;
    @XmlElement(name = "TotalAmount")
    private String amount;
    @XmlElement(name = "TransactionID")
    private String transactionId;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
    @XmlElement(name = "RemainingBalance")
    private String remainingBalance;
    @XmlElement(name = "HashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getConsumerMobileNumber() {
        return consumerMobileNumber;
    }

    public void setConsumerMobileNumber(String consumerMobileNumber) {
        this.consumerMobileNumber = consumerMobileNumber;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getLateBillAmount() {
        return lateBillAmount;
    }

    public void setLateBillAmount(String lateBillAmount) {
        this.lateBillAmount = lateBillAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
