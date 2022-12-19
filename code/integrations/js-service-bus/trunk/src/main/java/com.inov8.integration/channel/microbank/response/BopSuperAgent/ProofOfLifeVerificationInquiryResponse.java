package com.inov8.integration.channel.microbank.response.BopSuperAgent;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

public class ProofOfLifeVerificationInquiryResponse extends Response
{

    private String responseCode;
    private String responseDescription;
    private String rrn;


    @Override
    public String getResponseCode() {
        return responseCode;
    }

    @Override
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String getResponseDescription() {
        return responseDescription;
    }

    @Override
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
        return null;
    }
}
