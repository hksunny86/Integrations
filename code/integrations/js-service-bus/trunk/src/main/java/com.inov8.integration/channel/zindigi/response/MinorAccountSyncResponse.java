package com.inov8.integration.channel.zindigi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ResponseCode",
        "ResponseDescription",
        "PushNotification"
})
public class MinorAccountSyncResponse extends Response implements Serializable {

    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String description;
    @JsonProperty("PushNotification")
    private String pushNotification;

    public String getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(String pushNotification) {
        this.pushNotification = pushNotification;
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
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        i8SBSwitchControllerResponseVO.setPushNotification(this.getPushNotification());

        return i8SBSwitchControllerResponseVO;
    }
}
