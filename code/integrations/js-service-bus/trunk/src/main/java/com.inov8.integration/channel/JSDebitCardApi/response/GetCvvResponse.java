package com.inov8.integration.channel.JSDebitCardApi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processingCode",
        "merchantType",
        "traceNo",
        "dateTime",
        "responseCode",
        "data"
})

public class GetCvvResponse extends Response implements Serializable {

    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("traceNo")
    private String traceNo;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("data")
    private Data1 data;

    /**
     * No args constructor for use in serialization
     */
    public GetCvvResponse() {
    }

    /**
     * @param dateTime
     * @param data
     * @param processingCode
     * @param traceNo
     * @param merchantType
     * @param responseCode
     */
    public GetCvvResponse(String processingCode, String merchantType, String traceNo, String dateTime, String responseCode, Data1 data) {
        super();
        this.processingCode = processingCode;
        this.merchantType = merchantType;
        this.traceNo = traceNo;
        this.dateTime = dateTime;
        this.responseCode = responseCode;
        this.data = data;
    }

    @JsonProperty("processingCode")
    public String getProcessingCode() {
        return processingCode;
    }

    @JsonProperty("processingCode")
    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    @JsonProperty("merchantType")
    public String getMerchantType() {
        return merchantType;
    }

    @JsonProperty("merchantType")
    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
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

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("data")
    public Data1 getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data1 data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if ((this.data.getcCIMessageResponse().getBody().getResponse().getResponseCode().equalsIgnoreCase("00|Success")||this.data.getcCIMessageResponse().getBody().getResponse().getResponseCode().equals("00"))
                && (this.getResponseCode().equalsIgnoreCase("00|Success")||this.getResponseCode().equals("00"))) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setProcessingCode(this.getProcessingCode());
            i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
            i8SBSwitchControllerResponseVO.setTraceNo(this.getTraceNo());
            i8SBSwitchControllerResponseVO.setDateTime(this.getDateTime());
            i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.PROCESSED_00.getValue());
            i8SBSwitchControllerResponseVO.setMessageType(this.data.getcCIMessageResponse().getHeader().getMessageType());
            i8SBSwitchControllerResponseVO.setTranCode(this.data.getcCIMessageResponse().getHeader().getTransactionCode());
            i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.data.getcCIMessageResponse().getHeader().getTransmissionDateTime());
            i8SBSwitchControllerResponseVO.setSTAN(this.data.getcCIMessageResponse().getHeader().getStan());
            i8SBSwitchControllerResponseVO.setUserId(this.data.getcCIMessageResponse().getHeader().getUserId());
            i8SBSwitchControllerResponseVO.setPassword(this.data.getcCIMessageResponse().getHeader().getPassword());
            i8SBSwitchControllerResponseVO.setExpiryDate(this.getData().getcCIMessageResponse().getBody().getResponse().getExpiry());
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.data.getcCIMessageResponse().getBody().getResponse().getResponseCode());
        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CCIMessageResponse"
})
class Data1 {

    @JsonProperty("CCIMessageResponse")
    private CCIMessageResponse1 cCIMessageResponse;

    /**
     * No args constructor for use in serialization
     */
    public Data1() {
    }

    public Data1(CCIMessageResponse1 cCIMessageResponse) {
        this.cCIMessageResponse = cCIMessageResponse;
    }

    /**
     * @param cCIMessageResponse
     */


    public CCIMessageResponse1 getcCIMessageResponse() {
        return cCIMessageResponse;
    }

    public void setcCIMessageResponse(CCIMessageResponse1 cCIMessageResponse) {
        this.cCIMessageResponse = cCIMessageResponse;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Header",
        "Body"
})
class CCIMessageResponse1 {

    @JsonProperty("Header")
    private Header1 header;
    @JsonProperty("Body")
    private Body1 body;

    /**
     * No args constructor for use in serialization
     */
    public CCIMessageResponse1() {
    }

    /**
     * @param header
     * @param body
     */
    public CCIMessageResponse1(Header1 header, Body1 body) {
        super();
        this.header = header;
        this.body = body;
    }

    @JsonProperty("Header")
    public Header1 getHeader() {
        return header;
    }

    @JsonProperty("Header")
    public void setHeader(Header1 header) {
        this.header = header;
    }

    @JsonProperty("Body")
    public Body1 getBody() {
        return body;
    }

    @JsonProperty("Body")
    public void setBody(Body1 body) {
        this.body = body;
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Response"
})
class Body1 {

    @JsonProperty("Response")
    private Response1 response;

    /**
     * No args constructor for use in serialization
     */
    public Body1() {
    }

    /**
     * @param response
     */
    public Body1(Response1 response) {
        super();
        this.response = response;
    }

    @JsonProperty("Response")
    public Response1 getResponse() {
        return response;
    }

    @JsonProperty("Response")
    public void setResponse(Response1 response) {
        this.response = response;
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Message_Type",
        "Transaction_Code",
        "Transmission_Date_Time",
        "STAN",
        "UserId",
        "Password"
})
class Header1 {

    @JsonProperty("Message_Type")
    private String messageType;
    @JsonProperty("Transaction_Code")
    private String transactionCode;
    @JsonProperty("Transmission_Date_Time")
    private String transmissionDateTime;
    @JsonProperty("STAN")
    private String stan;
    @JsonProperty("UserId")
    private String userId;
    @JsonProperty("Password")
    private String password;

    /**
     * No args constructor for use in serialization
     */
    public Header1() {
    }

    /**
     * @param transmissionDateTime
     * @param password
     * @param messageType
     * @param stan
     * @param transactionCode
     * @param userId
     */
    public Header1(String messageType, String transactionCode, String transmissionDateTime, String stan, String userId, String password) {
        super();
        this.messageType = messageType;
        this.transactionCode = transactionCode;
        this.transmissionDateTime = transmissionDateTime;
        this.stan = stan;
        this.userId = userId;
        this.password = password;
    }

    @JsonProperty("Message_Type")
    public String getMessageType() {
        return messageType;
    }

    @JsonProperty("Message_Type")
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @JsonProperty("Transaction_Code")
    public String getTransactionCode() {
        return transactionCode;
    }

    @JsonProperty("Transaction_Code")
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @JsonProperty("Transmission_Date_Time")
    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    @JsonProperty("Transmission_Date_Time")
    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    @JsonProperty("STAN")
    public String getStan() {
        return stan;
    }

    @JsonProperty("STAN")
    public void setStan(String stan) {
        this.stan = stan;
    }

    @JsonProperty("UserId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("UserId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("Password")
    public void setPassword(String password) {
        this.password = password;
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CardNumber",
        "CardTitle",
        "Response_Code",
        "Expiry",
        "CVV2",
        "Issue_Date"
})
class Response1 {

    @JsonProperty("CardNumber")
    private String cardNumber;
    @JsonProperty("CardTitle")
    private String cardTitle;
    @JsonProperty("Response_Code")
    private String responseCode;
    @JsonProperty("Expiry")
    private String expiry;
    @JsonProperty("CVV2")
    private String cvv2;
    @JsonProperty("Issue_Date")
    private String issueDate;

    /**
     * No args constructor for use in serialization
     */
    public Response1() {
    }

    /**
     * @param cvv2
     * @param expiry
     * @param issueDate
     * @param cardTitle
     * @param cardNumber
     * @param responseCode
     */
    public Response1(String cardNumber, String cardTitle, String responseCode, String expiry, String cvv2, String issueDate) {
        super();
        this.cardNumber = cardNumber;
        this.cardTitle = cardTitle;
        this.responseCode = responseCode;
        this.expiry = expiry;
        this.cvv2 = cvv2;
        this.issueDate = issueDate;
    }

    @JsonProperty("CardNumber")
    public String getCardNumber() {
        return cardNumber;
    }

    @JsonProperty("CardNumber")
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @JsonProperty("CardTitle")
    public String getCardTitle() {
        return cardTitle;
    }

    @JsonProperty("CardTitle")
    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    @JsonProperty("Response_Code")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("Response_Code")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("Expiry")
    public String getExpiry() {
        return expiry;
    }

    @JsonProperty("Expiry")
    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    @JsonProperty("CVV2")
    public String getCvv2() {
        return cvv2;
    }

    @JsonProperty("CVV2")
    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    @JsonProperty("Issue_Date")
    public String getIssueDate() {
        return issueDate;
    }

    @JsonProperty("Issue_Date")
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}

