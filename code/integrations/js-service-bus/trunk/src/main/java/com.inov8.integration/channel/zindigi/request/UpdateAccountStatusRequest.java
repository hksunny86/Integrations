package com.inov8.integration.channel.zindigi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;

import javax.xml.bind.annotation.XmlRootElement;

import static com.inov8.integration.enums.DateFormatEnum.CNIC_FORMAT;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Mobile",
        "Status"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAccountStatusRequest extends Request {

    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("Status")
    private String status;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        String status = i8SBSwitchControllerRequestVO.getStatus();

        this.setMobile(i8SBSwitchControllerRequestVO.getMobileNumber());
        if (status != null && status.startsWith("App")) {
            this.setStatus("A");
        } else if (status != null && status.startsWith("Dis")) {
            this.setStatus("D");
        } else if (status != null && status.startsWith("Rej")) {
            this.setStatus("R");
        }

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}

