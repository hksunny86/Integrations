package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inov8.integration.webservice.dynamicQRPaymentVO.DynamicQRPaymentInquiry;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicQRPaymentInquiryResponse implements Serializable {

    private static final long serialVersionUID = 6009165415929861808L;

    private String rrn;
    private String responseCode;
    private String responseDescription;
    private String responseDateTime;
    private List<DynamicQRPaymentInquiry> transactionStatus;
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

    public List<DynamicQRPaymentInquiry> getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(List<DynamicQRPaymentInquiry> transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
