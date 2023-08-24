package com.inov8.integration.channel.debitCard.pdu.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Mobile",
        "NameApproved",
        "StreetNoApproved",
        "HouseNoApproved",
        "Status"
})
public class DebitCardDiscrepancyStatusRequest extends Request {

    @JsonProperty("Mobile")
    private String mobileNumber;
    @JsonProperty("NameApproved")
    private boolean nameApproved;
    @JsonProperty("StreetNoApproved")
    private boolean streetNoApproved;
    @JsonProperty("HouseNoApproved")
    private boolean houseNoApproved;
    @JsonProperty("Status")
    private String status;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isNameApproved() {
        return nameApproved;
    }

    public void setNameApproved(boolean nameApproved) {
        this.nameApproved = nameApproved;
    }

    public boolean isStreetNoApproved() {
        return streetNoApproved;
    }

    public void setStreetNoApproved(boolean streetNoApproved) {
        this.streetNoApproved = streetNoApproved;
    }

    public boolean isHouseNoApproved() {
        return houseNoApproved;
    }

    public void setHouseNoApproved(boolean houseNoApproved) {
        this.houseNoApproved = houseNoApproved;
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
        if (i8SBSwitchControllerRequestVO.getNameApproved().equalsIgnoreCase("Approved")) {
            this.setNameApproved(true);
        } else {
            this.setNameApproved(false);
        }
        if (i8SBSwitchControllerRequestVO.getStreetNoApproved().equalsIgnoreCase("Approved")) {
            this.setStreetNoApproved(true);
        } else {
            this.setStreetNoApproved(false);
        }
        if (i8SBSwitchControllerRequestVO.getHouseNoApproved().equalsIgnoreCase("Approved")) {
            this.setHouseNoApproved(true);
        } else {
            this.setHouseNoApproved(false);
        }
        this.setStatus(i8SBSwitchControllerRequestVO.getStatus());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMobileNumber())) {
            throw new I8SBValidationException("[Failed] Mobile Number:" + this.getMobileNumber());
        }
        return true;
    }
}
