package com.inov8.integration.channel.APIGEE.response.MPIN;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonPropertyOrder({
        "MessageType",
        "RequestCode",
        "TransmissionDateTime",
        "STAN",
        "ResponseCode"
})
class ApiResponse {
    @JsonProperty("MessageType")
    private String messageType;
    @JsonProperty("RequestCode")
    private String requestCode;
    @JsonProperty("TransmissionDateTime")
    private String transmissionDateTime;
    @JsonProperty("STAN")
    private String sTAN;
    @JsonProperty("ResponseCode")
    private String responseCode;

    public String getMessageType() { return messageType; }

    public void setMessageType(String messageType) { this.messageType = messageType; }

    public String getRequestCode() { return requestCode; }

    public void setRequestCode(String requestCode) { this.requestCode = requestCode; }

    public String getTransmissionDateTime() { return transmissionDateTime; }

    public void setTransmissionDateTime(String transmissionDateTime) { this.transmissionDateTime = transmissionDateTime; }

    public String getsTAN() { return sTAN; }

    public void setsTAN(String sTAN) { this.sTAN = sTAN; }

    public String getResponseCode() { return responseCode; }

    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "message",
        "apiResponse"
})
public class AtmPinGenerationResponse extends Response {

    @JsonProperty("message")
    private String message;
    @JsonProperty("apiResponse")
    private ApiResponse apiResponse;

    public AtmPinGenerationResponse() {
    }

    public AtmPinGenerationResponse(String message, ApiResponse apiResponse) { this.message = message; this.apiResponse = apiResponse; }

    @JsonProperty("message")
    public String getMessage() { return message; }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("apiResponse")
    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    @JsonProperty("apiResponse")
    public void setApiResponse(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getMessage().equalsIgnoreCase("success")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setStatusDesc(this.getMessage());
            i8SBSwitchControllerResponseVO.setMessageType(this.apiResponse.getMessageType());
            i8SBSwitchControllerResponseVO.setRequestCode(this.apiResponse.getRequestCode());
            i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.apiResponse.getTransmissionDateTime());
            i8SBSwitchControllerResponseVO.setSTAN(this.apiResponse.getsTAN());
            i8SBSwitchControllerResponseVO.setResponseCode(this.apiResponse.getResponseCode());
        }

        return i8SBSwitchControllerResponseVO;
    }
}



