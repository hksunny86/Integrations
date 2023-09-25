package com.inov8.integration.channel.onelinkSwitch.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.esb.response.EsbBillInquiryResponse;
import com.inov8.integration.channel.tasdeeq.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rrn",
        "transmissionTime",
        "transDateTime",
        "responseCode",
        "stan",
        "transactionAmount",
        "accountTitle",
        "accountNo1",
        "accountNo2",
        "cardAcceptorIdentificationCode",
        "cardAcceptorTerminalId",
        "purposeOfPayment",
        "senderName",
        "accountBankName",
        "accountBranchName",
        "toBankImd",
        "cardAcceptorNameAndLocation",
        "authIdResp",
        "networkIdentifier",
        "merchantType",
        "senderId",
        "receiverId",
        "pan",
        "message",
        "dateLocalTransaction",
        "timeLocalTransaction",
        "identifier",
})

public class IbftAdviceResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(EsbBillInquiryResponse.class.getSimpleName());

    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("transmissionTime")
    private String transmissionTime;
    @JsonProperty("transDateTime")
    private String transDateTime;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("stan")
    private String stan;
    @JsonProperty("transactionAmount")
    private String transactionAmount;
    @JsonProperty("accountTitle")
    private String accountTitle;
    @JsonProperty("accountNo1")
    private String accountNo1;
    @JsonProperty("accountNo2")
    private String accountNo2;
    @JsonProperty("cardAcceptorIdentificationCode")
    private String cardAcceptorIdentificationCode;
    @JsonProperty("cardAcceptorTerminalId")
    private String cardAcceptorTerminalId;
    @JsonProperty("purposeOfPayment")
    private String purposeOfPayment;
    @JsonProperty("senderName")
    private String senderName;
    @JsonProperty("accountBankName")
    private String accountBankName;
    @JsonProperty("accountBranchName")
    private String accountBranchName;
    @JsonProperty("toBankImd")
    private String toBankImd;
    @JsonProperty("cardAcceptorNameAndLocation")
    private String cardAcceptorNameAndLocation;
    @JsonProperty("authIdResp")
    private String authIdResp;
    @JsonProperty("networkIdentifier")
    private String networkIdentifier;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("senderId")
    private String senderId;
    @JsonProperty("receiverId")
    private String receiverId;
    @JsonProperty("pan")
    private String pan;
    @JsonProperty("message")
    private String message;
    @JsonProperty("dateLocalTransaction")
    private String dateLocalTransaction;
    @JsonProperty("timeLocalTransaction")
    private String timeLocalTransaction;
    @JsonProperty("identifier")
    private String identifier;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(String transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

    public String getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(String transDateTime) {
        this.transDateTime = transDateTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
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

    public String getCardAcceptorIdentificationCode() {
        return cardAcceptorIdentificationCode;
    }

    public void setCardAcceptorIdentificationCode(String cardAcceptorIdentificationCode) {
        this.cardAcceptorIdentificationCode = cardAcceptorIdentificationCode;
    }

    public String getCardAcceptorTerminalId() {
        return cardAcceptorTerminalId;
    }

    public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) {
        this.cardAcceptorTerminalId = cardAcceptorTerminalId;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public String getToBankImd() {
        return toBankImd;
    }

    public void setToBankImd(String toBankImd) {
        this.toBankImd = toBankImd;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getAuthIdResp() {
        return authIdResp;
    }

    public void setAuthIdResp(String authIdResp) {
        this.authIdResp = authIdResp;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(String dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("00")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        }
        i8SBSwitchControllerResponseVO.setRrn(this.getRrn());
        i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.getTransmissionTime());
        i8SBSwitchControllerResponseVO.setDateTime(this.getTransDateTime());
        i8SBSwitchControllerResponseVO.setSTAN(this.getStan());
        i8SBSwitchControllerResponseVO.setAmount(this.getTransactionAmount());
        i8SBSwitchControllerResponseVO.setAccountTitle(this.getAccountTitle());
        i8SBSwitchControllerResponseVO.setAccountId1(this.getAccountNo1());
        i8SBSwitchControllerResponseVO.setAccountId2(this.getAccountNo2());
        i8SBSwitchControllerResponseVO.setCardAcceptorIdCode(this.getCardAcceptorIdentificationCode());
        i8SBSwitchControllerResponseVO.setCardAcceptorTerminalId(this.getCardAcceptorTerminalId());
        i8SBSwitchControllerResponseVO.setPurposeOfPayment(this.getPurposeOfPayment());
        i8SBSwitchControllerResponseVO.setSenderName(this.getSenderName());
        i8SBSwitchControllerResponseVO.setBankName(this.getAccountBankName());
        i8SBSwitchControllerResponseVO.setBranchName(this.getAccountBranchName());
        i8SBSwitchControllerResponseVO.setToBankImd(this.getToBankImd());
        i8SBSwitchControllerResponseVO.setCardAcceptorNameAndLocation(this.getCardAcceptorNameAndLocation());
        i8SBSwitchControllerResponseVO.setTranAuthID(this.getAuthIdResp());
        i8SBSwitchControllerResponseVO.setNetworkIdentifier(this.getNetworkIdentifier());
        i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
        i8SBSwitchControllerResponseVO.setSenderId(this.getSenderId());
        i8SBSwitchControllerResponseVO.setReceiverId(this.getReceiverId());
        i8SBSwitchControllerResponseVO.setPAN(this.getPan());
        i8SBSwitchControllerResponseVO.setMessage(this.getMessage());
        i8SBSwitchControllerResponseVO.setDateLocalTransaction(this.getDateLocalTransaction());
        i8SBSwitchControllerResponseVO.setTimeLocalTransaction(this.getTimeLocalTransaction());
        i8SBSwitchControllerResponseVO.setIdentifier(this.getIdentifier());

        return i8SBSwitchControllerResponseVO;
    }
}
