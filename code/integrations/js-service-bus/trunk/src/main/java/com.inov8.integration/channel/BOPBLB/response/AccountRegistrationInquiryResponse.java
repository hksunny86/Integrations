package com.inov8.integration.channel.BOPBLB.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

public class AccountRegistrationInquiryResponse extends Response {
    private String responseCode;
    private String responseDescription;
    private String rrn;




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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setTransactionReferenceNumber(this.getRrn());

        return i8SBSwitchControllerResponseVO;
    }
}
