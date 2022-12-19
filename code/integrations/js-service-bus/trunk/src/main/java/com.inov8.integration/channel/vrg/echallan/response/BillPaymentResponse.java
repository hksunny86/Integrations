package com.inov8.integration.channel.vrg.echallan.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BillPaymentResponse")
public class BillPaymentResponse extends Response {

    @XmlElement(name = "Response_Code")
    private String responseCode;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode("00");
        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void parseResponse(I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        if (getReturnedString().length() >= 2) {
            this.setResponseCode("00");
        }
    }
}