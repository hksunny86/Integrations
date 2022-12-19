package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;

/**
 * Created by Inov8 on 8/29/2019.
 */
@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CashOutResponse")
public class CashOutResponse {
    private static final long serialVersionUID = -1156481809344525232L;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "TransactionID")
    private String transactionId;
    @XmlElement(name = "RRN")
    private String rrn;
    @XmlElement(name = "TransactionCode")
    private String transactionCode;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
