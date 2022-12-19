package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;

/**
 * Created by Inov8 on 8/29/2019.
 */
@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CashOutInquiryResponse")
public class CashOutInquiryResponse {
    private static final long serialVersionUID = -1156481809344525232L;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "RRN")
    private String rrn;
    @XmlElement(name = "CustomerName")
    private String customerName;
    @XmlElement(name = "CustomerMobile")
    private String mobileNumber;
    @XmlElement(name = "CNIC")
    private String cnic;
    @XmlElement(name = "Amount")
    private String amount;
    @XmlElement(name = "Data_Hash")
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

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
