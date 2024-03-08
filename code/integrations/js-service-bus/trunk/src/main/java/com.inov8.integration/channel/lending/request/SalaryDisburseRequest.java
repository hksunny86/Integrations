package com.inov8.integration.channel.lending.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.lendingVO.GetActiveLoanData;
import com.inov8.integration.webservice.lendingVO.GetActiveLoanRequestPayload;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "mobileNumber",
        "cnic",
        "salary",
})
public class SalaryDisburseRequest extends Request {

    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("cnic")
    private String cnic;
    @JsonProperty("salary")
    private String salary;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setSalary(i8SBSwitchControllerRequestVO.getSalary());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


