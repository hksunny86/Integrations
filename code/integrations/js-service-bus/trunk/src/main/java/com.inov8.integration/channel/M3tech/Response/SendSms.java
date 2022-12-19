package com.inov8.integration.channel.M3tech.Response;

import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSms extends Response {
    @XmlElement(name = "SendSMSResult")
    protected String sendSMSResult;

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getSendSMSResult().equalsIgnoreCase("0")) {
            i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.PROCESSED_00.getValue());
        }else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getSendSMSResult());
        }
        return i8SBSwitchControllerResponseVO;
    }

    public String getSendSMSResult() {
        return sendSMSResult;
    }

    public void setSendSMSResult(String sendSMSResult) {
        this.sendSMSResult = sendSMSResult;
    }
}
