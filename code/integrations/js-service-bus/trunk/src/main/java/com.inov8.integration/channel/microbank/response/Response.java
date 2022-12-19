package com.inov8.integration.channel.microbank.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.LinkedHashMap;

/**
 * Created by inov8 on 4/23/2018.
 */
abstract public class Response {

    private String RRN;
    private String ResponseDateTime;
    private String ResponseCode;
    private String ResponseDescription;
    private String CheckSum;

    public String getCheckSum() {
        return CheckSum;
    }

    public void setCheckSum(String checkSum) {
        CheckSum = checkSum;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getResponseDateTime() {
        return ResponseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        ResponseDateTime = responseDateTime;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseDescription() {
        return ResponseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        ResponseDescription = responseDescription;
    }

    public abstract I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException;
    public LinkedHashMap<String, Object> DobilloPopulate()
    {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("RRN",this.getRRN());
        result.put("ResponseDateTime",this.getResponseDateTime());
        result.put("ResponseCode",this.getResponseCode());
        result.put("ResponseDescription",this.getResponseDescription());
        result.put("CheckSum",this.getCheckSum());
        return result;
    }
}
