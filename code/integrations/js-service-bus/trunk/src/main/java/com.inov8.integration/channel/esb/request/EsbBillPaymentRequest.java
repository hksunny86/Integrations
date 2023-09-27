package com.inov8.integration.channel.esb.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.request.Request;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processingCode",
        "dateTime",
        "traceNo",
        "merchantType",
        "userName",
        "password",
        "consumerNumber",
        "tranAuthId",
        "transactionAmount",
        "tranDate",
        "tranTime",
        "bankMnemonic",
        "reserved"
})
public class EsbBillPaymentRequest extends Request {

    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("traceNo")
    private String traceNo;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("password")
    private String password;
    @JsonProperty("consumerNumber")
    private String consumerNumber;
    @JsonProperty("tranAuthId")
    private String tranAuthId;
    @JsonProperty("transactionAmount")
    private String transactionAmount;
    @JsonProperty("tranDate")
    private String tranDate;
    @JsonProperty("tranTime")
    private String tranTime;
    @JsonProperty("bankMnemonic")
    private String bankMnemonic;
    @JsonProperty("reserved")
    private String reserved;

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

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

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getTranAuthId() {
        return tranAuthId;
    }

    public void setTranAuthId(String tranAuthId) {
        this.tranAuthId = tranAuthId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getBankMnemonic() {
        return bankMnemonic;
    }

    public void setBankMnemonic(String bankMnemonic) {
        this.bankMnemonic = bankMnemonic;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("RCDPbillInquiry");
        this.setDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.setTraceNo(i8SBSwitchControllerRequestVO.getSTAN());
        this.setMerchantType(PropertyReader.getProperty("esb.merchant.Type"));
        this.setUserName(PropertyReader.getProperty("esb.username"));
        this.setPassword(PropertyReader.getProperty("esb.password"));
        this.setConsumerNumber(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setTranAuthId(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setTranDate(i8SBSwitchControllerRequestVO.getTransactionDate());
        this.setTranTime(i8SBSwitchControllerRequestVO.getTransactionDateTime());
        this.setBankMnemonic(PropertyReader.getProperty("esb.bankMnemonic"));
        this.setReserved(i8SBSwitchControllerRequestVO.getReserved1());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getConsumerNumber())) {
            throw new I8SBValidationException("[Failed] Consumer Number:" + this.getConsumerNumber());
        }
        return true;
    }
}
