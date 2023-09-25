package com.inov8.integration.channel.onelinkSwitch.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pan",
        "transactionAmount",
        "merchantType",
        "pointOfEntry",
        "authIdResp",
        "cardAcceptorTerminalId",
        "cardAcceptorIdentificationCode",
        "cardAcceptorNameAndLocation",
        "purposeOfPayment",
        "currencyCode",
        "accountNo1",
        "accountNo2",
        "accountTitle",
        "senderName",
        "toBankImd",
        "stan",
        "rrn",
        "beneficiaryId",
        "transactionDateTime",
        "accountBankName",
        "accountBranchName",
        "senderId",
        "networkIdentifier",

})
public class IbftAdviceRequest extends Request {

    @JsonProperty("pan")
    private String pan;
    @JsonProperty("transactionAmount")
    private String transactionAmount;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("pointOfEntry")
    private String pointOfEntry;
    @JsonProperty("authIdResp")
    private String authIdResp;
    @JsonProperty("cardAcceptorTerminalId")
    private String cardAcceptorTerminalId;
    @JsonProperty("cardAcceptorIdentificationCode")
    private String cardAcceptorIdentificationCode;
    @JsonProperty("cardAcceptorNameAndLocation")
    private String cardAcceptorNameAndLocation;
    @JsonProperty("purposeOfPayment")
    private String purposeOfPayment;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("accountNo1")
    private String accountNo1;
    @JsonProperty("accountNo2")
    private String accountNo2;
    @JsonProperty("accountTitle")
    private String accountTitle;
    @JsonProperty("senderName")
    private String senderName;
    @JsonProperty("toBankImd")
    private String toBankImd;
    @JsonProperty("stan")
    private String stan;
    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("beneficiaryId")
    private String beneficiaryId;
    @JsonProperty("transactionDateTime")
    private String transactionDateTime;
    @JsonProperty("accountBankName")
    private String accountBankName;
    @JsonProperty("accountBranchName")
    private String accountBranchName;
    @JsonProperty("senderId")
    private String senderId;
    @JsonProperty("networkIdentifier")
    private String networkIdentifier;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getPointOfEntry() {
        return pointOfEntry;
    }

    public void setPointOfEntry(String pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
    }

    public String getAuthIdResp() {
        return authIdResp;
    }

    public void setAuthIdResp(String authIdResp) {
        this.authIdResp = authIdResp;
    }

    public String getCardAcceptorTerminalId() {
        return cardAcceptorTerminalId;
    }

    public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) {
        this.cardAcceptorTerminalId = cardAcceptorTerminalId;
    }

    public String getCardAcceptorIdentificationCode() {
        return cardAcceptorIdentificationCode;
    }

    public void setCardAcceptorIdentificationCode(String cardAcceptorIdentificationCode) {
        this.cardAcceptorIdentificationCode = cardAcceptorIdentificationCode;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getAccountNo2() {
        return accountNo2;
    }

    public void setAccountNo2(String accountNo2) {
        this.accountNo2 = accountNo2;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getToBankImd() {
        return toBankImd;
    }

    public void setToBankImd(String toBankImd) {
        this.toBankImd = toBankImd;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setPan("");
        this.setTransactionAmount("");
        this.setMerchantType("");
        this.setPointOfEntry("");
        this.setAuthIdResp("");
        this.setCardAcceptorTerminalId("00000000");
        this.setCardAcceptorIdentificationCode("000000");
        this.setCardAcceptorNameAndLocation("JSBL Branchless Banking Channel Pakistan");
        this.setPurposeOfPayment("");
        this.setCurrencyCode("586");
        this.setAccountNo1(i8SBSwitchControllerRequestVO.getAccountId1());
        this.setAccountNo2(i8SBSwitchControllerRequestVO.getAccountId2());
        this.setAccountTitle(i8SBSwitchControllerRequestVO.getAccountTitle());
        this.setSenderName("");
        this.setToBankImd("");
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setBeneficiaryId("");
        this.setTransactionDateTime(String.valueOf(new Date()));
        this.setAccountBankName(i8SBSwitchControllerRequestVO.getBankName());
        this.setAccountBranchName(i8SBSwitchControllerRequestVO.getBranchName());
        this.setSenderId("");
        this.setNetworkIdentifier("RDV");
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
