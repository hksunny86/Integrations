package com.inov8.integration.channel.JSDebitCardApi.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import static com.inov8.integration.enums.DateFormatEnum.EXPIRY_DATE;
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
        "cardNumber",
        "status"

})
public class UpdateCardStatusRequest extends Request implements Serializable {

    private String username =  PropertyReader.getProperty("updateCardStatus.username");
    private String pass = PropertyReader.getProperty("updateCardStatus.password");
    private String cardStatus = PropertyReader.getProperty("updatedCardStatus.cardStatus");

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
    @JsonProperty("status")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("UpdateCardStatus");
        this.setTraceNo(i8SBSwitchControllerRequestVO.getSTAN());
        this.setDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setMerchantType("0088");
        this.setMessageType("0200");
        this.setTransactionCode("407");
        this.setTransmissionDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setUserId(username);
        this.setPassword(pass);
        this.setChannelId("00");
        this.setCardNumber(i8SBSwitchControllerRequestVO.getCardNumber());
        this.setStatus(cardStatus);

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getProcessingCode())) {
            throw new I8SBValidationException("[Failed] Processing Code:" + this.getProcessingCode());
        }
        if (StringUtils.isEmpty(this.getTraceNo())) {
            throw new I8SBValidationException("[Failed] Trace No:" + this.getTraceNo());
        }
        if (StringUtils.isEmpty(this.getMerchantType())) {
            throw new I8SBValidationException("[Failed] Merchant Type:" + this.getMerchantType());
        }
        if (StringUtils.isEmpty(this.getMessageType())) {
            throw new I8SBValidationException("[Failed] Message Type:" + this.getMessageType());
        }
        if (StringUtils.isEmpty(this.getTransactionCode())) {
            throw new I8SBValidationException("[Failed] Transaction Code:" + this.getTransactionCode());
        }
        if (StringUtils.isEmpty(this.getTransmissionDateTime())) {
            throw new I8SBValidationException("[Failed] Transmission Date Time :" + this.getTransmissionDateTime());
        }
        if (StringUtils.isEmpty(this.getStan())) {
            throw new I8SBValidationException("[Failed] STAN :" + this.getStan());
        }
        if (StringUtils.isEmpty(this.getChannelId())) {
            throw new I8SBValidationException("[Failed] Channel ID:" + this.getChannelId());
        }
        if (StringUtils.isEmpty(this.getCardNumber())) {
            throw new I8SBValidationException("[Failed] Card Number:" + this.getCardNumber());
        }
        if (StringUtils.isEmpty(this.getStatus())) {
            throw new I8SBValidationException("[Failed] Status:" + this.getStatus());
        }

        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
