package com.inov8.integration.channel.jazz.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "toMobileNumber",
        "text",
        "fromMobileNumber"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendSmsRequest extends Request {

    @JsonProperty("toMobileNumber")
    private String toMobileNumber;
    @JsonProperty("text")
    private String text;
    @JsonProperty("fromMobileNumber")
    private String fromMobileNumber;

    public String getToMobileNumber() {
        return toMobileNumber;
    }

    public void setToMobileNumber(String toMobileNumber) {
        this.toMobileNumber = toMobileNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromMobileNumber() {
        return fromMobileNumber;
    }

    public void setFromMobileNumber(String fromMobileNumber) {
        this.fromMobileNumber = fromMobileNumber;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setToMobileNumber(i8SBSwitchControllerRequestVO.getRecieverMobileNo());
        this.setText(i8SBSwitchControllerRequestVO.getSmsText());
        this.setFromMobileNumber(PropertyReader.getProperty("jazz.from.mobile.number"));
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


