package com.inov8.integration.middleware.pdu.response;


import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentBillPaymentInquiryResponse")
public class AgentBillPaymentInquiryResponse implements Serializable {

    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ProductName")
    private String productName;
    @XmlElement(name = "ConsumerMobileNumber")
    private String consumerMobileNumber;
    @XmlElement(name = "BillAmount")
    private String billAmount;
    @XmlElement(name = "LateBillAmount")
    private String lateBillAmount;
    @XmlElement(name = "BillPaid")
    private String billPaid;
    @XmlElement(name = "DueDate")
    private String dueDate;
    @XmlElement(name = "OverDue")
    private String overDue;
    @XmlElement(name = "HashData")
    private String hashData;

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

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getConsumerMobileNumber() {
        return consumerMobileNumber;
    }

    public void setConsumerMobileNumber(String consumerMobileNumber) {
        this.consumerMobileNumber = consumerMobileNumber;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getLateBillAmount() {
        return lateBillAmount;
    }

    public void setLateBillAmount(String lateBillAmount) {
        this.lateBillAmount = lateBillAmount;
    }

    public String getBillPaid() {
        return billPaid;
    }

    public void setBillPaid(String billPaid) {
        this.billPaid = billPaid;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getOverDue() {
        return overDue;
    }

    public void setOverDue(String overDue) {
        this.overDue = overDue;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
