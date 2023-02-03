package com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashWithdrawlResponse extends Response {
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("TransactionNum")
    private String transactionNumber;
    @JsonProperty("CustomerAccTitle")
    private String customerAccountTitle;
    @JsonProperty("SessionIDNADRA")
    private String sessionIdNadra;
    @JsonProperty("SessionIDBAFL")
    private String sessionId;
    @JsonProperty("CurrentBalance")
    private String currentBalance;
    @JsonProperty("WalletAccountID")
    private String walletAccountID;
    @JsonProperty("WalletAccountBalance")
    private String walletAccountBalance;



    public String getWalletAccountID() {
        return walletAccountID;
    }

    public void setWalletAccountID(String walletAccountID) {
        this.walletAccountID = walletAccountID;
    }

    public String getWalletAccountBalance() {
        return walletAccountBalance;
    }

    public void setWalletAccountBalance(String walletAccountBalance) {
        this.walletAccountBalance = walletAccountBalance;
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

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getCustomerAccountTitle() {
        return customerAccountTitle;
    }

    public void setCustomerAccountTitle(String customerAccountTitle) {
        this.customerAccountTitle = customerAccountTitle;
    }

    public String getSessionIdNadra() {
        return sessionIdNadra;
    }

    public void setSessionIdNadra(String sessionIdNadra) {
        this.sessionIdNadra = sessionIdNadra;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
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

    @JsonProperty("FingerIndex")
    private String fingerIndex;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("Reserved3")
    private String reserved3;
    @JsonProperty("Reserved4")
    private String reserved4;
    @JsonProperty("Reserved5")
    private String reserved5;


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setTransactionNumber(this.getTransactionNumber());
        i8SBSwitchControllerResponseVO.setCustomerAccNumber(this.getCustomerAccountTitle());
        i8SBSwitchControllerResponseVO.setSessionId(this.getSessionId());
        i8SBSwitchControllerResponseVO.setSessionIdNadra(this.getSessionIdNadra());
        i8SBSwitchControllerResponseVO.setAccountBalance(this.getCurrentBalance());
        i8SBSwitchControllerResponseVO.setFingerIndex(this.getFingerIndex());
        i8SBSwitchControllerResponseVO.setWalletAccountId(this.getWalletAccountID());
        i8SBSwitchControllerResponseVO.setWalletBalance(this.getWalletAccountBalance());
        return i8SBSwitchControllerResponseVO;
    }
}