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
        "relationshipNum",
        "accountNum",
        "cardEmborsingName",
        "cardTypeCode",
        "cardProdCode",
        "cardBranchCode",
        "primaryNic",
        "issuedDate",
        "requestType"
})
public class ImportCardRequest extends Request implements Serializable {

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
    @JsonProperty("relationshipNum")
    private String relationshipNum;
    @JsonProperty("accountNum")
    private String accountNum;
    @JsonProperty("cardEmborsingName")
    private String cardEmborsingName;
    @JsonProperty("cardTypeCode")
    private String cardTypeCode;
    @JsonProperty("cardProdCode")
    private String cardProdCode;
    @JsonProperty("cardBranchCode")
    private String cardBranchCode;
    @JsonProperty("primaryNic")
    private String primaryNic;
    @JsonProperty("issuedDate")
    private String issuedDate;
    @JsonProperty("requestType")
    private String requestType;

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

    public String getRelationshipNum() {
        return relationshipNum;
    }

    public void setRelationshipNum(String relationshipNum) {
        this.relationshipNum = relationshipNum;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
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

    public String getCardBranchCode() {
        return cardBranchCode;
    }

    public void setCardBranchCode(String cardBranchCode) {
        this.cardBranchCode = cardBranchCode;
    }

    public String getPrimaryNic() {
        return primaryNic;
    }

    public void setPrimaryNic(String primaryNic) {
        this.primaryNic = primaryNic;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode("ImportCard");
        this.setTraceNo(i8SBSwitchControllerRequestVO.getSTAN());
        this.setDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setMerchantType("0088");
        this.setMessageType("0200");
        this.setTransactionCode("403");
        this.setTransmissionDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setStan(i8SBSwitchControllerRequestVO.getSTAN());
        this.setUserId(username);
        this.setPassword(pass);
        this.setChannelId("8");
        this.setRelationshipNum(i8SBSwitchControllerRequestVO.getRelationshipNumber());
        this.setAccountNum(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setCardEmborsingName(i8SBSwitchControllerRequestVO.getCardEmborsingName());
        this.setCardTypeCode(i8SBSwitchControllerRequestVO.getCardTypeCode());
        this.setCardProdCode(i8SBSwitchControllerRequestVO.getCardProdCode());
        this.setCardBranchCode(i8SBSwitchControllerRequestVO.getCardBranchCode());
        this.setPrimaryNic(i8SBSwitchControllerRequestVO.getRelationshipNumber());
        this.setIssuedDate(i8SBSwitchControllerRequestVO.getIssuedDate());
        this.setRequestType(i8SBSwitchControllerRequestVO.getRequestId());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getRelationshipNum())) {
            throw new I8SBValidationException("[Failed] Relationship Number:" + this.getRelationshipNum());
        }
        if (StringUtils.isEmpty(this.getAccountNum())) {
            throw new I8SBValidationException("[Failed] Account Number:" + this.getAccountNum());
        }
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

        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
