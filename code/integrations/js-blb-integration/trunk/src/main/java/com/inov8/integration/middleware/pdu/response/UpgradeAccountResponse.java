package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UpgradeAccountResponse")
public class UpgradeAccountResponse implements Serializable {

    private static final long serialVersionUID = 6009165415929861808L;

    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "TransactionCode")
    private String transactionCode;
    @XmlElement(name = "HashData")
    private String hashData;

    @XmlElement(name = "TrustedFlag")
    private Boolean trustedFlag;

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

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Boolean getTrustedFlag() {
        return trustedFlag;
    }

    public void setTrustedFlag(Boolean trustedFlag) {
        this.trustedFlag = trustedFlag;
    }
}
