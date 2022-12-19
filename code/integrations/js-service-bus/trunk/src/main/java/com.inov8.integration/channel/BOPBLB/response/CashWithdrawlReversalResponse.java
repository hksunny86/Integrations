package com.inov8.integration.channel.BOPBLB.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlElement;

public class CashWithdrawlReversalResponse extends Response {

//    @XmlElement(name = "ResponseCode")
//    protected String responseCode;
//    @XmlElement(name = "ResponseDescription")
//    protected String responseDescription;
//    @XmlElement(name = "RRN")
//    protected String rrn;
//    @XmlElement(name = "StatusCode")
//    protected String statusCode;
//    @XmlElement(name = "Status")
//    protected String status;

    @JsonProperty("ResponseCode")
    protected String responseCode;
    @JsonProperty("ResponseDescription")
    protected String responseDescription;
    @JsonProperty("RRN")
    protected String rrn;
    @JsonProperty("StatusCode")
    protected String statusCode;
    @JsonProperty("Status")
    protected String status;


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

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO=new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setStatus(this.getStatus());

        return i8SBSwitchControllerResponseVO;    }
}
