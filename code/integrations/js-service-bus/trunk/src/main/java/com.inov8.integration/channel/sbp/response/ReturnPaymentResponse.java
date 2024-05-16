package com.inov8.integration.channel.sbp.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.sbpVO.ReturnPaymentResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Response",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnPaymentResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(ReturnPaymentResponse.class.getSimpleName());

    @JsonProperty("Response")
    private ReturnPaymentResponsePayload response;

    public ReturnPaymentResponsePayload getResponse() {
        return response;
    }

    public void setResponse(ReturnPaymentResponsePayload response) {
        this.response = response;
    }


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponse() != null) {
            if (this.getResponse().getResponseCode().equalsIgnoreCase("00")) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
                i8SBSwitchControllerResponseVO.setDescription(this.getResponse().getResponseDescription().get(0).toString());
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getResponse().getResponseCode());
                i8SBSwitchControllerResponseVO.setDescription(this.getResponse().getResponseDescription().get(0).toString());
            }
            i8SBSwitchControllerResponseVO.setBatchId(this.getResponse().getBatchId());
        }

        return i8SBSwitchControllerResponseVO;
    }
}