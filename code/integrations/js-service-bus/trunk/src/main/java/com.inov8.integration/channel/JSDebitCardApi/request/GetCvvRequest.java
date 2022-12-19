package com.inov8.integration.channel.JSDebitCardApi.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processingCode",
        "traceNo",
        "dateTime",
        "merchantType",
        "messageType",
        "transactionCode",
        "transmissionDateTime",
        "stan",
        "userId",
        "password",
        "channelId",
        "cardNumber"
})
public class GetCvvRequest extends Request {

    private String username = PropertyReader.getProperty("jsdebitcatrdapi.username");
    private String pass = PropertyReader.getProperty("jsdebitcatrdapi.password");

    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("traceNo")
    private String traceNo;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("messageType")
    private String messageType;
    @JsonProperty("transactionCode")
    private String transactionCode;
    @JsonProperty("transmissionDateTime")
    private String transmissionDateTime;
    @JsonProperty("stan")
    private String stan;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("password")
    private String password;
    @JsonProperty("channelId")
    private String channelId;
    @JsonProperty("cardNumber")
    private String cardNumber;


    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("GetCVV");
        this.setTraceNo(i8SBSwitchControllerRequestVO.getSTAN());
        this.setDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setMerchantType("0088");
        this.setMessageType("0200");
        this.setTransactionCode("406");
        this.setTransmissionDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setUserId(username);
        this.setPassword(pass);
        this.setChannelId("00");
        this.setCardNumber(i8SBSwitchControllerRequestVO.getCardNumber());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
