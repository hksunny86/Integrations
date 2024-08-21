package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BalanceInquiryResponseV2")
public class BalanceInquiryResponseV2 implements Serializable {

    private static final long serialVersionUID = -4121786524311764695L;

    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "Balance")
    private String balance;

    @XmlElement(name = "BalanceInProcess")
    private String balanceInProcess;
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

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceInProcess() {
        return balanceInProcess;
    }

    public void setBalanceInProcess(String balanceInProcess) {
        this.balanceInProcess = balanceInProcess;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
