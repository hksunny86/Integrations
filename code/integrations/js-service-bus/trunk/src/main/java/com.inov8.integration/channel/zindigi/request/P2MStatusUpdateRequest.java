package com.inov8.integration.channel.zindigi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Mobile",
        "BusinessName",
        "BusinessAddress",
        "CNICFront",
        "CNICBack",
        "Selfie",
        "Status"
}
)
public class P2MStatusUpdateRequest extends Request implements Serializable {

    @JsonProperty("Mobile")
    private String mobileNumber;
    @JsonProperty("BusinessName")
    private boolean businessName;
    @JsonProperty("BusinessAddress")
    private boolean businessAddress;
    @JsonProperty("CNICFront")
    private boolean cnicFront;
    @JsonProperty("CNICBack")
    private boolean cnicBack;
    @JsonProperty("Selfie")
    private boolean selfie;
    @JsonProperty("Status")
    private String status;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isBusinessName() {
        return businessName;
    }

    public void setBusinessName(boolean businessName) {
        this.businessName = businessName;
    }

    public boolean isBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(boolean businessAddress) {
        this.businessAddress = businessAddress;
    }

    public boolean isCnicFront() {
        return cnicFront;
    }

    public void setCnicFront(boolean cnicFront) {
        this.cnicFront = cnicFront;
    }

    public boolean isCnicBack() {
        return cnicBack;
    }

    public void setCnicBack(boolean cnicBack) {
        this.cnicBack = cnicBack;
    }

    public boolean isSelfie() {
        return selfie;
    }

    public void setSelfie(boolean selfie) {
        this.selfie = selfie;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        if (i8SBSwitchControllerRequestVO.getName().equalsIgnoreCase("Approved")) {
            this.setBusinessName(true);
        } else {
            this.setBusinessName(false);
        }
        if (i8SBSwitchControllerRequestVO.getAddress().equalsIgnoreCase("Approved")) {
            this.setBusinessAddress(true);
        } else {
            this.setBusinessAddress(false);
        }
        if (i8SBSwitchControllerRequestVO.getCnicFrontPic().equalsIgnoreCase("Approved")) {
            this.setCnicFront(true);
        } else {
            this.setCnicFront(false);
        }
        if (i8SBSwitchControllerRequestVO.getCnicBackPic().equalsIgnoreCase("Approved")) {
            this.setCnicBack(true);
        } else {
            this.setCnicBack(false);
        }
        if (i8SBSwitchControllerRequestVO.getCustomerPic().equalsIgnoreCase("Approved")) {
            this.setSelfie(true);
        } else {
            this.setSelfie(false);
        }
        this.setStatus(i8SBSwitchControllerRequestVO.getStatus());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMobileNumber())) {
            throw new I8SBValidationException("[Failed] Mobile Number :" + this.getMobileNumber());
        }
        if (StringUtils.isEmpty(this.getStatus())) {
            throw new I8SBValidationException("[Failed] Status :" + this.getStatus());
        }
        return true;
    }
}