package com.inov8.integration.channel.zindigi.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Mobile",
        "Selfie",
        "BFormPic",
        "ParentCNIC",
        "ParentCNICBack",
        "MinorCNIC",
        "MinorCNICback",
        "Status"
})
public class MinorAccountSyncRequest extends Request {

    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("Selfie")
    private Boolean selfie;
    @JsonProperty("BFormPic")
    private Boolean bFormPic;
    @JsonProperty("ParentCNIC")
    private Boolean parentCnic;
    @JsonProperty("ParentCNICBack")
    private Boolean parentCnicBack;
    @JsonProperty("MinorCNIC")
    private Boolean minorCnic;
    @JsonProperty("MinorCNICback")
    private Boolean minorCnicBack;
    @JsonProperty("Status")
    private String status;


    public Boolean getbFormPic() {
        return bFormPic;
    }

    public void setbFormPic(Boolean bFormPic) {
        this.bFormPic = bFormPic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getSelfie() {
        return selfie;
    }

    public void setSelfie(Boolean selfie) {
        this.selfie = selfie;
    }

    public Boolean getParentCnic() {
        return parentCnic;
    }

    public void setParentCnic(Boolean parentCnic) {
        this.parentCnic = parentCnic;
    }

    public Boolean getParentCnicBack() {
        return parentCnicBack;
    }

    public void setParentCnicBack(Boolean parentCnicBack) {
        this.parentCnicBack = parentCnicBack;
    }

    public Boolean getMinorCnic() {
        return minorCnic;
    }

    public void setMinorCnic(Boolean minorCnic) {
        this.minorCnic = minorCnic;
    }

    public Boolean getMinorCnicBack() {
        return minorCnicBack;
    }

    public void setMinorCnicBack(Boolean minorCnicBack) {
        this.minorCnicBack = minorCnicBack;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMobile(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setParentCnic((!i8SBSwitchControllerRequestVO.getFatherCnicPic().equalsIgnoreCase("0")));
        this.setMinorCnic((!i8SBSwitchControllerRequestVO.getMinorCnicPic().equalsIgnoreCase("0")));
        this.setSelfie((!i8SBSwitchControllerRequestVO.getCustomerPic().equalsIgnoreCase("0")));
        this.setParentCnicBack((!i8SBSwitchControllerRequestVO.getCnicBackPic().equalsIgnoreCase("0")));
        this.setMinorCnicBack((!i8SBSwitchControllerRequestVO.getMinorCnicBackPic().equalsIgnoreCase("0")));
        this.setbFormPic((!i8SBSwitchControllerRequestVO.getReserved1().equalsIgnoreCase("0")));
        if (i8SBSwitchControllerRequestVO.getStatus().equals("Discripent")) {
            this.setStatus("R");

        } else if (i8SBSwitchControllerRequestVO.getStatus().equals("Approved")) {
            this.setStatus("A");

        } else {
            this.setStatus("R");

        }
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
