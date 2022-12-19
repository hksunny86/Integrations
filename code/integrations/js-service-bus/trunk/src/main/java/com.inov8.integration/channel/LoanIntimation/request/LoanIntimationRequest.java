package com.inov8.integration.channel.LoanIntimation.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanIntimationRequest extends Request {
    @JsonProperty("Mobile")
    private String mobileNo;
    @JsonProperty("Amount")
    private String amount;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
