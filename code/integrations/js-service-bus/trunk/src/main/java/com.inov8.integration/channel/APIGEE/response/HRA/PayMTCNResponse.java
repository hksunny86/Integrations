package com.inov8.integration.channel.APIGEE.response.HRA;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by inov8 on 5/28/2018.
 */

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayMTCNResponse extends Response {

    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;
    @JsonProperty("result")
    private Result result;
    @JsonProperty("errors")
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        @JsonProperty("rrn")
        private String rrn;
        @JsonProperty("dateTime")
        private String dateTime;
        @JsonProperty("mesage")
        private String messge;

        public String getRrn() {
            return rrn;
        }

        public void setRrn(String rrn) {
            this.rrn = rrn;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getMessge() {
            return messge;
        }

        public void setMessge(String messge) {
            this.messge = messge;
        }
    }

    public static class Error {
        @JsonProperty("request")
        private String request;

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.code);
        i8SBSwitchControllerResponseVO.setDescription(this.description);
        if (this.getResult() != null) {
            i8SBSwitchControllerResponseVO.setTransmissionDateAndTime(this.getResult().getDateTime());
            i8SBSwitchControllerResponseVO.setStatus(this.getResult().getMessge());
            i8SBSwitchControllerResponseVO.setSTAN(this.getResult().getRrn());
        }
        if (this.getError()!=null){
            i8SBSwitchControllerResponseVO.setStatus(this.getError().getRequest());
        }
        return i8SBSwitchControllerResponseVO;
    }
}
