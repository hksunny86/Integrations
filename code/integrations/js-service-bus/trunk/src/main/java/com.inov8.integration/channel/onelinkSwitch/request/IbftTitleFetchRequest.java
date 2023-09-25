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
        "transactionDateTime",
        "merchantType",
        "pointOfEntry",
        "networkIdentifier",
        "cardAcceptorTerminalId",
        "cardAcceptorIdentificationCode",
        "cardAcceptorNameAndLocation",
        "purposeOfPayment",
        "currencyCode",
        "accountNo1",
        "accountNo2",
        "toBankImd",
        "stan",
        "rrn"
})
public class IbftTitleFetchRequest extends Request {

    @JsonProperty("pan")
    private String pan;
    @JsonProperty("transactionAmount")
    private String transactionAmount;
    @JsonProperty("transactionDateTime")
    private String transactionDateTime;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("pointOfEntry")
    private String pointOfEntry;
    @JsonProperty("networkIdentifier")
    private String networkIdentifier;
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
    @JsonProperty("toBankImd")
    private String toBankImd;
    @JsonProperty("stan")
    private String stan;
    @JsonProperty("rrn")
    private String rrn;

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

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
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

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
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

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setPan(i8SBSwitchControllerRequestVO.getCNIC() + i8SBSwitchControllerRequestVO.getSTAN());
        this.setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setTransactionDateTime(String.valueOf(new Date()));
        this.setMerchantType("");
        this.setPointOfEntry("");
        this.setNetworkIdentifier("RDV");
        this.setCardAcceptorTerminalId("00000000");
        this.setCardAcceptorIdentificationCode("000000");
        this.setCardAcceptorNameAndLocation("JSBL Branchless Banking Channel Pakistan");
        this.setPurposeOfPayment(i8SBSwitchControllerRequestVO.getTransferPurpose());
        this.setCurrencyCode("586");
        this.setAccountNo1(i8SBSwitchControllerRequestVO.getAccountId1());
        this.setAccountNo2(i8SBSwitchControllerRequestVO.getAccountId2());
        this.setToBankImd(i8SBSwitchControllerRequestVO.getToBankIMD());
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
