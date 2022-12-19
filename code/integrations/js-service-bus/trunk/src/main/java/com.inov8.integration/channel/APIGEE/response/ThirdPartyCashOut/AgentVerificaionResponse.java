package com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

public class AgentVerificaionResponse extends Response {

    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescrition;
    @JsonProperty("SessionIDNADRA")
    private String sessionIDNADRA;
    @JsonProperty("SessionIDBAFL")
    private String sessionIDBAFL;
    @JsonProperty("FingerIndex")
    private String fingerIndex;
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("CustomerAccTitle")
    private String customerAccTitle;
    @JsonProperty("SessionID")
    private String sessionID;
    @JsonProperty("CurrentBalance")
    private String currentBalance;
    @JsonProperty("WalletAccountID")
    private String walletAccountID;
    @JsonProperty("WalletAccountBalance")
    private String walletAccountBalance;
    @JsonProperty("TerminalID")
    private String terminalID;
    @JsonProperty("MobileNumber")
    private String mobileNumber;
    @JsonProperty("AvailableLimit")
    private String availableLimit;
    @JsonProperty("TransactionNum")
    private String transactionNum;
    @JsonProperty("MobileWalletNumber")
    private String mobileWalletNumber;
    @JsonProperty("CustomerAccNum")
    private String customerAccNum;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("Reserved3")
    private String reserved3;
    @JsonProperty("Reserved4")
    private String reserved4;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescrition() {
        return responseDescrition;
    }

    public void setResponseDescrition(String responseDescrition) {
        this.responseDescrition = responseDescrition;
    }

    public String getSessionIDNADRA() {
        return sessionIDNADRA;
    }

    public void setSessionIDNADRA(String sessionIDNADRA) {
        this.sessionIDNADRA = sessionIDNADRA;
    }

    public String getSessionIDBAFL() {
        return sessionIDBAFL;
    }

    public void setSessionIDBAFL(String sessionIDBAFL) {
        this.sessionIDBAFL = sessionIDBAFL;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getCustomerAccTitle() {
        return customerAccTitle;
    }

    public void setCustomerAccTitle(String customerAccTitle) {
        this.customerAccTitle = customerAccTitle;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

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

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(String availableLimit) {
        this.availableLimit = availableLimit;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getMobileWalletNumber() {
        return mobileWalletNumber;
    }

    public void setMobileWalletNumber(String mobileWalletNumber) {
        this.mobileWalletNumber = mobileWalletNumber;
    }

    public String getCustomerAccNum() {
        return customerAccNum;
    }

    public void setCustomerAccNum(String customerAccNum) {
        this.customerAccNum = customerAccNum;
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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescrition());
        i8SBSwitchControllerResponseVO.setSessionIdNadra(this.getSessionIDNADRA());
        i8SBSwitchControllerResponseVO.setSessionIDBAFL(this.getSessionIDBAFL());
        i8SBSwitchControllerResponseVO.setFingerIndex(this.getFingerIndex());
        i8SBSwitchControllerResponseVO.setTransactionId(this.getTransactionID());
        i8SBSwitchControllerResponseVO.setAccountTitle(this.getCustomerAccTitle());
        i8SBSwitchControllerResponseVO.setSessionId(this.getSessionID());
        i8SBSwitchControllerResponseVO.setAvailableBalance(this.getCurrentBalance());
        i8SBSwitchControllerResponseVO.setWalletAccountId(this.getWalletAccountID());
        i8SBSwitchControllerResponseVO.setWalletBalance(this.getWalletAccountBalance());
        i8SBSwitchControllerResponseVO.setTerminalId(this.getTerminalID());
        i8SBSwitchControllerResponseVO.setMobilePhone(this.getMobileNumber());
        i8SBSwitchControllerResponseVO.setAvailableLimit(this.getAvailableLimit());
        i8SBSwitchControllerResponseVO.setTransactionNumber(this.getTransactionNum());
        i8SBSwitchControllerResponseVO.setMobileWalletNumber(this.getMobileWalletNumber());
        i8SBSwitchControllerResponseVO.setCustomerAccountNumber(this.getCustomerAccNum());
        i8SBSwitchControllerResponseVO.setReserved1(this.getReserved1());
        i8SBSwitchControllerResponseVO.setReserved2(this.getReserved2());
        i8SBSwitchControllerResponseVO.setReserved3(this.getReserved3());
        i8SBSwitchControllerResponseVO.setReserved4(this.getReserved4());
        return i8SBSwitchControllerResponseVO;
    }
}
