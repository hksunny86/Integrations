package com.inov8.integration.channel.raast.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.raastVO.QrRegistration;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseCode",
        "responseMessage",
        "traceId",
        "body"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class QrRastRegistrationResponse extends Response {
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseMessage")
    private String responseMessage;
    @JsonProperty("traceId")
    private String traceId;
    @JsonProperty("body")
    private QrRegistration body;

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

    public QrRegistration getBody() {
        return body;
    }

    public void setBody(QrRegistration body) {
        this.body = body;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("00")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseMessage());
            i8SBSwitchControllerResponseVO.setTraceNo(this.getTraceId());
            if (this.getBody() != null) {
                i8SBSwitchControllerResponseVO.setMerchantId(this.getBody().getTill());
                i8SBSwitchControllerResponseVO.setQr(this.getBody().getQrText());
            }

        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseMessage());
            i8SBSwitchControllerResponseVO.setTraceNo(this.getTraceId());
            if (this.getBody() != null) {
                i8SBSwitchControllerResponseVO.setMerchantId(this.getBody().getTill());
                i8SBSwitchControllerResponseVO.setQr(this.getBody().getQrText());
            }
        }
        return i8SBSwitchControllerResponseVO;
    }
}
