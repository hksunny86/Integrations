package com.inov8.integration.channel.fcy.response;

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
        "msgId",
        "statusFlag",
        "currencyValue"
})

public class FcyConversionResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(EsbBillInquiryResponse.class.getSimpleName());

    @JsonProperty("msgId")
    private String msgId;
    @JsonProperty("statusFlag")
    private String statusFlag;
    @JsonProperty("currencyValue")
    private String currencyValue;
    private String responseCode;
    private String description;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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
        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        }
        i8SBSwitchControllerResponseVO.setMessageId(this.getMsgId());
        i8SBSwitchControllerResponseVO.setCardAcceptorNameAndLocation(this.getStatusFlag());
        i8SBSwitchControllerResponseVO.setCardAcceptorTerminalId(this.getCurrencyValue());

        return i8SBSwitchControllerResponseVO;
    }
}
