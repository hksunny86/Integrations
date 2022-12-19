package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentIbftPaymentRequest")
public class AgentIbftPaymentRequest implements Serializable {

    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "RRN")
    private String rrn;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "TerminalId")
    private String terminalID;
    @XmlElement(name = "Mpin")
    private String mpin;
    @XmlElement(name = "OTP")
    private String otp;
    @XmlElement(name = "AgentId")
    private String agentId;
    @XmlElement(name = "ProductId")
    private String productId;
    @XmlElement(name = "CoreAccountId")
    private String coreAccountId;
    @XmlElement(name = "CoreAccountTitle")
    private String coreAccountTitle;
    @XmlElement(name = "BankIMD")
    private String bankIMD;
    @XmlElement(name = "PaymentPurpose")
    private String paymentPurpose;
    @XmlElement(name = "BeneficiaryBankName")
    private String BeneficiaryBankName;
    @XmlElement(name = "BeneficiaryBranchName")
    private String beneficiaryiciaryBranchName;
    @XmlElement(name = "BeneficiaryIBAN")
    private String beneficiaryIban;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
    @XmlElement(name = "TotalAmount")
    private String totalAmount;
    @XmlElement(name = "Reserved1")
    private String reserved1;
    @XmlElement(name = "Reserved2")
    private String reserved2;
    @XmlElement(name = "Reserved3")
    private String reserved3;
    @XmlElement(name = "Reserved4")
    private String reserved4;
    @XmlElement(name = "Reserved5")
    private String reserved5;
    @XmlElement(name = "Data_Hash")
    private String hashData;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCoreAccountId() {
        return coreAccountId;
    }

    public void setCoreAccountId(String coreAccountId) {
        this.coreAccountId = coreAccountId;
    }

    public String getCoreAccountTitle() {
        return coreAccountTitle;
    }

    public void setCoreAccountTitle(String coreAccountTitle) {
        this.coreAccountTitle = coreAccountTitle;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }

    public String getBeneficiaryBankName() {
        return BeneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        BeneficiaryBankName = beneficiaryBankName;
    }

    public String getBeneficiaryiciaryBranchName() {
        return beneficiaryiciaryBranchName;
    }

    public void setBeneficiaryiciaryBranchName(String beneficiaryiciaryBranchName) {
        this.beneficiaryiciaryBranchName = beneficiaryiciaryBranchName;
    }

    public String getBeneficiaryIban() {
        return beneficiaryIban;
    }

    public void setBeneficiaryIban(String beneficiaryIban) {
        this.beneficiaryIban = beneficiaryIban;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
