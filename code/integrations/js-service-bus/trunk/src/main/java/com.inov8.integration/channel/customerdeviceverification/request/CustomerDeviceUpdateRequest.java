package com.inov8.integration.channel.customerdeviceverification.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDeviceUpdateRequest extends Request {
    @JsonProperty("Mobile")
    private String mobileNo;
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Status")
    private String status;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setId(i8SBSwitchControllerRequestVO.getIdCode());
        this.setStatus(i8SBSwitchControllerRequestVO.getStatus());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}

