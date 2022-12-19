package com.inov8.integration.channel.merchantDiscountCamping.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

public class TransactionUpdateResponse extends Response {
    private String responsecode;
    private String messages;
    private String data;



    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponsecode().equals("1")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        }else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponsecode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        }

        return i8SBSwitchControllerResponseVO;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
