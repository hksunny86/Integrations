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
        "pan",
        "cardAcceptorNameAndLocation",
        "cardAcceptorTermianlId",
        "identifier",
        "transactionAmount",
        "requestTime",
        "transmissionTime",
        "stan",
        "rrn",
        "responseCode",
        "accountTitle",
        "pointOfEntry",
        "accountNo1",
        "accountNo2",
        "purposeOfPayment",
        "toBankImd",
        "merchantType",
        "networdIdentifier",
        "message",
        "dateLocalTransaction",
        "timeLocalTransaction",
        "accountBranchName",
        "accountBankName",
        "benificieryIban",
        "beneficaryId"
})

public class IbftTitleFetchResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(EsbBillInquiryResponse.class.getSimpleName());

    @JsonProperty("pan")
    private String pan;
    @JsonProperty("cardAcceptorNameAndLocation")
    private String cardAcceptorNameAndLocation;
    @JsonProperty("cardAcceptorTermianlId")
    private String cardAcceptorTermianlId;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("transactionAmount")
    private String transactionAmount;
    @JsonProperty("requestTime")
    private String requestTime;
    @JsonProperty("transmissionTime")
    private String transmissionTime;
    @JsonProperty("stan")
    private String stan;
    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("accountTitle")
    private String accountTitle;
    @JsonProperty("pointOfEntry")
    private String pointOfEntry;
    @JsonProperty("accountNo1")
    private String accountNo1;
    @JsonProperty("accountNo2")
    private String accountNo2;
    @JsonProperty("purposeOfPayment")
    private String purposeOfPayment;
    @JsonProperty("toBankImd")
    private String toBankImd;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("networdIdentifier")
    private String networdIdentifier;
    @JsonProperty("message")
    private String message;
    @JsonProperty("dateLocalTransaction")
    private String dateLocalTransaction;
    @JsonProperty("timeLocalTransaction")
    private String timeLocalTransaction;
    @JsonProperty("accountBranchName")
    private String accountBranchName;
    @JsonProperty("accountBankName")
    private String accountBankName;
    @JsonProperty("benificieryIban")
    private String benificieryIban;
    @JsonProperty("beneficaryId")
    private String beneficaryId;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getCardAcceptorTermianlId() {
        return cardAcceptorTermianlId;
    }

    public void setCardAcceptorTermianlId(String cardAcceptorTermianlId) {
        this.cardAcceptorTermianlId = cardAcceptorTermianlId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(String transmissionTime) {
        this.transmissionTime = transmissionTime;
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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getPointOfEntry() {
        return pointOfEntry;
    }

    public void setPointOfEntry(String pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
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

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getToBankImd() {
        return toBankImd;
    }

    public void setToBankImd(String toBankImd) {
        this.toBankImd = toBankImd;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getNetwordIdentifier() {
        return networdIdentifier;
    }

    public void setNetwordIdentifier(String networdIdentifier) {
        this.networdIdentifier = networdIdentifier;
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

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getBenificieryIban() {
        return benificieryIban;
    }

    public void setBenificieryIban(String benificieryIban) {
        this.benificieryIban = benificieryIban;
    }

    public String getBeneficaryId() {
        return beneficaryId;
    }

    public void setBeneficaryId(String beneficaryId) {
        this.beneficaryId = beneficaryId;
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
        i8SBSwitchControllerResponseVO.setPAN(this.getPan());
        i8SBSwitchControllerResponseVO.setCardAcceptorNameAndLocation(this.getCardAcceptorNameAndLocation());
        i8SBSwitchControllerResponseVO.setCardAcceptorTerminalId(this.getCardAcceptorTermianlId());
        i8SBSwitchControllerResponseVO.setIdentifier(this.getIdentifier());
        i8SBSwitchControllerResponseVO.setAmount(this.getTransactionAmount());
        i8SBSwitchControllerResponseVO.setDateTime(this.getRequestTime());
        i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.getTransmissionTime());
        i8SBSwitchControllerResponseVO.setSTAN(this.getStan());
        i8SBSwitchControllerResponseVO.setRrn(this.getRrn());
        i8SBSwitchControllerResponseVO.setAccountTitle(this.getAccountTitle());
        i8SBSwitchControllerResponseVO.setPointOfEntry(this.getPointOfEntry());
        i8SBSwitchControllerResponseVO.setAccountId1(this.getAccountNo1());
        i8SBSwitchControllerResponseVO.setAccountId2(this.getAccountNo2());
        i8SBSwitchControllerResponseVO.setPurposeOfPayment(this.getPurposeOfPayment());
        i8SBSwitchControllerResponseVO.setToBankImd(this.getToBankImd());
        i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
        i8SBSwitchControllerResponseVO.setNetworkIdentifier(this.getNetwordIdentifier());
        i8SBSwitchControllerResponseVO.setMessage(this.getMessage());
        i8SBSwitchControllerResponseVO.setDateLocalTransaction(this.getDateLocalTransaction());
        i8SBSwitchControllerResponseVO.setTimeLocalTransaction(this.getTimeLocalTransaction());
        i8SBSwitchControllerResponseVO.setBranchName(this.getAccountBranchName());
        i8SBSwitchControllerResponseVO.setBankName(this.getAccountBankName());
        i8SBSwitchControllerResponseVO.setBeneficiaryIban(this.getBenificieryIban());
        i8SBSwitchControllerResponseVO.setBeneficiaryId(this.getBeneficaryId());

        return i8SBSwitchControllerResponseVO;
    }
}
