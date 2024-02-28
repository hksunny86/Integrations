package com.inov8.integration.channel.fcy.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.enums.DateFormatEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.middleware.util.DateTools;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "msgId",
        "channelId",
        "requestDate",
        "currencyId"
})
public class FcyConversionRequest extends Request {

    @JsonProperty("msgId")
    private String msgId;
    @JsonProperty("channelId")
    private String channelId;
    @JsonProperty("requestDate")
    private String requestDate;
    @JsonProperty("currencyId")
    private String currencyId;

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

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMsgId(i8SBSwitchControllerRequestVO.getMessageId());
        this.setChannelId(PropertyReader.getProperty("fcy.channel.id"));
        this.setRequestDate(DateTools.dateToString(new Date(), DateFormatEnum.DATE_LOCAL_TRANSACTION.getValue()));
        this.setCurrencyId(i8SBSwitchControllerRequestVO.getCurrencyCode());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


