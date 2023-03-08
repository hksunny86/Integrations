package com.inov8.integration.channel.tasdeeq.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "statusCode",
        "messageCode",
        "message",
        "data"
})
public class AuthenticateUpdatedResponse extends Response {

    @JsonProperty("statusCode")
    private String statusCode;
    @JsonProperty("messageCode")
    private String messageCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Data data;
    private String responseCode;
    private String responseDescription;

    @JsonProperty("statusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("messageCode")
    public String getMessageCode() {
        return messageCode;
    }

    @JsonProperty("messageCode")
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();

        if (this.getStatusCode().equals("111")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getStatusCode());
        }
        i8SBSwitchControllerResponseVO.setStatusCode(this.getStatusCode());
        i8SBSwitchControllerResponseVO.setMessageCode(this.getMessageCode());
        i8SBSwitchControllerResponseVO.setMessage(this.getMessage());
        i8SBSwitchControllerResponseVO.setAuthToken(this.getData().getAuthToken());
        i8SBSwitchControllerResponseVO.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "auth_token"
})
class Data {

    @JsonProperty("auth_token")
    private String authToken;

    @JsonProperty("auth_token")
    public String getAuthToken() {
        return authToken;
    }

    @JsonProperty("auth_token")
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
