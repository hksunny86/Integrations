package com.inov8.integration.channel.merchant.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.merchant.StaticQr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "staticQrResponses"
})
public class StaticQrResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(StaticQrResponse.class.getSimpleName());

    private String responseCode;
    private String responseMessage;
    private String traceId;
    private StaticQr body;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public StaticQr getBody() {
        return body;
    }

    public void setBody(StaticQr body) {
        this.body = body;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
            i8SBSwitchControllerResponseVO.setStaticQrList(this.getStaticQrResponses());
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
            i8SBSwitchControllerResponseVO.setStaticQrList(this.getStaticQrResponses());

        }
        return i8SBSwitchControllerResponseVO;
    }
}
