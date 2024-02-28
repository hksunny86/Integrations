package com.inov8.integration.channel.fcy.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.esb.response.EsbBillInquiryResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.DateFormatEnum;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.middleware.util.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "msgId",
        "statusFlag",
        "channelId",
        "requestDate",
        "currencyId",
        "ResponseCode",
        "ResponseDescription",
        "currencyValue"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class FcyConversionResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(EsbBillInquiryResponse.class.getSimpleName());

    @JsonProperty("msgId")
    private String msgId;
    @JsonProperty("channelId")
    private String channelId;
    @JsonProperty("requestDate")
    private String requestDate;
    @JsonProperty("currencyId")
    private String currencyId;
    @JsonProperty("statusFlag")
    private String statusFlag;
    @JsonProperty("currencyValue")
    private String currencyValue;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String description;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode() != null && !this.getResponseCode().equals("00")) {
            i8SBSwitchControllerResponseVO.setMessageId(this.getMsgId());
            i8SBSwitchControllerResponseVO.setCurrencyCode(this.getCurrencyId());
            i8SBSwitchControllerResponseVO.setChannelId(this.getChannelId());
            i8SBSwitchControllerResponseVO.setDateTime(this.getRequestDate());
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
            i8SBSwitchControllerResponseVO.setMessageId(this.getMsgId());
            i8SBSwitchControllerResponseVO.setStatusFlag(this.getStatusFlag());
            i8SBSwitchControllerResponseVO.setCurrencyValue(this.getCurrencyValue());
        }

        return i8SBSwitchControllerResponseVO;
    }
}
