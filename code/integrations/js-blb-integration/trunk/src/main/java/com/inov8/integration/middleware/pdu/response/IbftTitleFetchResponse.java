package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;


@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IbftTitleFetchResponse")
public class IbftTitleFetchResponse implements Serializable {
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "SenderMobileNumber")
    private String senderMobileNumber;
    @XmlElement(name = "recieverAcountNumber")
    private String recieverAcountNumber;
    @XmlElement(name = "SenderAccountTitle")
    private String senderAccountTitle;
    @XmlElement(name = "RecieverAccountTitle")
    private String recieverAccountTitle;
    @XmlElement(name = "Amount")
    private String amount;
    @XmlElement(name = "Charges")
    private String charges;
    @XmlElement(name = "dateTime")
    private String dateTime;
    @XmlElement(name = "AccountBankName")
    private String accountBankName;
    @XmlElement(name = "AccountBranchName")
    private String accountBranchName;
    @XmlElement(name = "BenificieryIBAN")
    private String benificieryIBAN;
    @XmlElement(name = "SourceBankIMD")
    private String sourceBankImd;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "HashData")
    private String hashData;


    public String getSourceBankImd() {
        return sourceBankImd;
    }

    public void setSourceBankImd(String sourceBankImd) {
        this.sourceBankImd = sourceBankImd;
    }

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getBenificieryIBAN() {
        return benificieryIBAN;
    }

    public void setBenificieryIBAN(String benificieryIBAN) {
        this.benificieryIBAN = benificieryIBAN;
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

    public String getSenderMobileNumber() {
        return senderMobileNumber;
    }

    public void setSenderMobileNumber(String senderMobileNumber) {
        this.senderMobileNumber = senderMobileNumber;
    }

    public String getRecieverAcountNumber() {
        return recieverAcountNumber;
    }

    public void setRecieverAcountNumber(String recieverAcountNumber) {
        this.recieverAcountNumber = recieverAcountNumber;
    }

    public String getSenderAccountTitle() {
        return senderAccountTitle;
    }

    public void setSenderAccountTitle(String senderAccountTitle) {
        this.senderAccountTitle = senderAccountTitle;
    }

    public String getRecieverAccountTitle() {
        return recieverAccountTitle;
    }

    public void setRecieverAccountTitle(String recieverAccountTitle) {
        this.recieverAccountTitle = recieverAccountTitle;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
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
}

