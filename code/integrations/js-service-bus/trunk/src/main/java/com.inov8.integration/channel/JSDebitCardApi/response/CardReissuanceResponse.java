package com.inov8.integration.channel.JSDebitCardApi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
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
public class CardReissuanceResponse extends Response implements Serializable {

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
    private Data data;

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
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.data.getCCIMessageResponse().getBody().getResponse().getResponseCode().equalsIgnoreCase("00|Success")
                && this.getResponseCode().equalsIgnoreCase("00")) {

            i8SBSwitchControllerResponseVO.setProcessingCode(this.getProcessingCode());
            i8SBSwitchControllerResponseVO.setMerchantType(this.getMerchantType());
            i8SBSwitchControllerResponseVO.setTraceNo(this.getTraceNo());
            i8SBSwitchControllerResponseVO.setDateTime(this.getDateTime());
            i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.PROCESSED_00.getValue());
            i8SBSwitchControllerResponseVO.setMessageType(this.data.getCCIMessageResponse().getHeader().getMessageType());
            i8SBSwitchControllerResponseVO.setTranCode(this.data.getCCIMessageResponse().getHeader().getTransactionCode());
            i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.data.getCCIMessageResponse().getHeader().getTransmissionDateTime());
            i8SBSwitchControllerResponseVO.setSTAN(this.data.getCCIMessageResponse().getHeader().getStan());
            i8SBSwitchControllerResponseVO.setUserId(this.data.getCCIMessageResponse().getHeader().getUserId());
            i8SBSwitchControllerResponseVO.setPassword(this.data.getCCIMessageResponse().getHeader().getPassword());

        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.data.getCCIMessageResponse().getBody().getResponse().getResponseCode());
        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CCIMessageResponse"
})
class Data {

    @JsonProperty("CCIMessageResponse")
    private CCIMessageResponse cCIMessageResponse;

    @JsonProperty("CCIMessageResponse")
    public CCIMessageResponse getCCIMessageResponse() {
        return cCIMessageResponse;
    }

    @JsonProperty("CCIMessageResponse")
    public void setCCIMessageResponse(CCIMessageResponse cCIMessageResponse) {
        this.cCIMessageResponse = cCIMessageResponse;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Header",
        "Body"
})
class CCIMessageResponse {

    @JsonProperty("Header")
    private Header header;

    @JsonProperty("Body")
    private Body body;

    @JsonProperty("Header")
    public Header getHeader() {
        return header;
    }

    @JsonProperty("Header")
    public void setHeader(Header header) {
        this.header = header;
    }

    @JsonProperty("Body")
    public Body getBody() {
        return body;
    }

    @JsonProperty("Body")
    public void setBody(Body body) {
        this.body = body;
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
class Header {

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
        "Response"
})
class Body {

    @JsonProperty("Response")
    private ResponseStatus response;

    @JsonProperty("Response")
    public ResponseStatus getResponse() {
        return response;
    }

    @JsonProperty("Response")
    public void setResponse(ResponseStatus response) {
        this.response = response;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ResponseCode"
})
class ResponseStatus {

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("ResponseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

}