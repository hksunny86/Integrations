package com.inov8.integration.channel.JSDebitCardApi.request;

import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import static com.inov8.integration.enums.DateFormatEnum.*;

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
        "operation",
        "expiryDate",
        "card_Emborsing_Name",
        "card_Type_Code",
        "card_Prod_Code"

})
public class CardReissuanceRequest extends Request implements Serializable {

    private String username = PropertyReader.getProperty("jsdebitcatrdapi.username");
    private String pass = PropertyReader.getProperty("jsdebitcatrdapi.password");
    private String operat = PropertyReader.getProperty("jsdebitcatrdapi.operation");

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
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("expiryDate")
    private String expiryDate;
    @JsonProperty("card_Emborsing_Name")
    private String cardEmborsingName;
    @JsonProperty("card_Type_Code")
    private String cardTypeCode;
    @JsonProperty("card_Prod_Code")
    private String cardProdCode;

    @JsonProperty("processingCode")
    public String getProcessingCode() {
        return processingCode;
    }

    @JsonProperty("processingCode")
    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    @JsonProperty("traceNo")
    public String getTraceNo() {
        return traceNo;
    }

    @JsonProperty("traceNo")
    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    @JsonProperty("dateTime")
    public String getDateTime() {
        return dateTime;
    }

    @JsonProperty("dateTime")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @JsonProperty("merchantType")
    public String getMerchantType() {
        return merchantType;
    }

    @JsonProperty("merchantType")
    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    @JsonProperty("messageType")
    public String getMessageType() {
        return messageType;
    }

    @JsonProperty("messageType")
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @JsonProperty("transactionCode")
    public String getTransactionCode() {
        return transactionCode;
    }

    @JsonProperty("transactionCode")
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @JsonProperty("transmissionDateTime")
    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    @JsonProperty("transmissionDateTime")
    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    @JsonProperty("stan")
    public String getStan() {
        return stan;
    }

    @JsonProperty("stan")
    public void setStan(String stan) {
        this.stan = stan;
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("channelId")
    public String getChannelId() {
        return channelId;
    }

    @JsonProperty("channelId")
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @JsonProperty("cardNumber")
    public String getCardNumber() {
        return cardNumber;
    }

    @JsonProperty("cardNumber")
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JsonProperty("expiryDate")
    public String getExpiryDate() {
        return expiryDate;
    }

    @JsonProperty("expiryDate")
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardEmborsingName() {
        return cardEmborsingName;
    }

    public void setCardEmborsingName(String cardEmborsingName) {
        this.cardEmborsingName = cardEmborsingName;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardProdCode() {
        return cardProdCode;
    }

    public void setCardProdCode(String cardProdCode) {
        this.cardProdCode = cardProdCode;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("CardReIssuance");
        this.setTraceNo(i8SBSwitchControllerRequestVO.getSTAN());
        this.setDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setMerchantType("0088");
        this.setMessageType("0200");
        this.setTransactionCode("421");
        this.setTransmissionDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setUserId(username);
        this.setPassword(pass);
        this.setChannelId("1");
        this.setCardNumber(i8SBSwitchControllerRequestVO.getCardNumber());
        this.setOperation(operat);
        this.setExpiryDate(DateUtil.expiryDate(i8SBSwitchControllerRequestVO.getExpiryDate(), DATE_TIME.getValue()));
        this.setCardProdCode(i8SBSwitchControllerRequestVO.getCardProdCode());
        this.setCardTypeCode(i8SBSwitchControllerRequestVO.getCardTypeCode());
        this.setCardEmborsingName(i8SBSwitchControllerRequestVO.getCardEmborsingName());

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
        if (StringUtils.isEmpty(this.getOperation())) {
            throw new I8SBValidationException("[Failed] Operation:" + this.getOperation());
        }
//        if (StringUtils.isEmpty(this.getExpiryDate())) {
//            throw new I8SBValidationException("[Failed] Expiry Dated:" + this.getExpiryDate());
//        }

        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
