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
public class BalanceInquiryResponse extends Response {
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("CurrentBalance")
    private String currentBalance;
    @JsonProperty("AvailableBalance")
    private String availableBalance;
    @JsonProperty("SessionID")
    private String sessioId;
    @JsonProperty("CustomerAccNum")
    private String customerAccountNumber;
    @JsonProperty("CustomerAccTitle")
    private String customerAccountTitle;
    @JsonProperty("MobileNumber")
    private String mobileNumber;
    @JsonProperty("AvailableLimit")
    private String availableLimit;
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
    @JsonProperty("WalletAccountId")
    private String walletAccountID;
    @JsonProperty("WalletAccountBalance")
    private String walletAccountBalance;
    @JsonProperty("LastFingerIndex")
    private String lastFingerIndex;

    public String getLastFingerIndex() {
        return lastFingerIndex;
    }

    public void setLastFingerIndex(String lastFingerIndex) {
        this.lastFingerIndex = lastFingerIndex;
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

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getSessioId() {
        return sessioId;
    }

    public void setSessioId(String sessioId) {
        this.sessioId = sessioId;
    }

    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public String getCustomerAccountTitle() {
        return customerAccountTitle;
    }

    public void setCustomerAccountTitle(String customerAccountTitle) {
        this.customerAccountTitle = customerAccountTitle;
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


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setAccountBalance(this.getCurrentBalance());
        i8SBSwitchControllerResponseVO.setAvailableBalance(this.getAvailableBalance());
        i8SBSwitchControllerResponseVO.setAvailableLimit(this.getAvailableLimit());
        i8SBSwitchControllerResponseVO.setAccountTitle(this.getCustomerAccountTitle());
        i8SBSwitchControllerResponseVO.setConsumerNumber(this.getCustomerAccountNumber());
        i8SBSwitchControllerResponseVO.setSessionId(this.getSessioId());
        i8SBSwitchControllerResponseVO.setMobilePhone(this.getMobileNumber());
        i8SBSwitchControllerResponseVO.setWalletBalance(this.getWalletAccountBalance());
        i8SBSwitchControllerResponseVO.setWalletAccountId(this.getWalletAccountID());
        i8SBSwitchControllerResponseVO.setLastFingerIndex(this.getLastFingerIndex());
        i8SBSwitchControllerResponseVO.setReserved1(this.getReserved1());
        i8SBSwitchControllerResponseVO.setReserved2(this.getReserved2());
        i8SBSwitchControllerResponseVO.setReserved3(this.getReserved3());
        i8SBSwitchControllerResponseVO.setReserved4(this.getReserved4());
        return i8SBSwitchControllerResponseVO;

    }
}
