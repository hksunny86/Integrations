package com.inov8.integration.channel.optasia.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanEvent",
        "loanEventStatus",
        "origSource",
        "sourceRequestId"
})
public class CallBackRequest extends Request {

    @JsonProperty("loanEvent")
    private String loanEvent;
    @JsonProperty("loanEventStatus")
    private String loanEventStatus;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("sourceRequestId")
    private String sourceRequestId;

    public String getLoanEvent() {
        return loanEvent;
    }

    public void setLoanEvent(String loanEvent) {
        this.loanEvent = loanEvent;
    }

    public String getLoanEventStatus() {
        return loanEventStatus;
    }

    public void setLoanEventStatus(String loanEventStatus) {
        this.loanEventStatus = loanEventStatus;
    }

    public String getOrigSource() {
        return origSource;
    }

    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    public String getSourceRequestId() {
        return sourceRequestId;
    }

    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setLoanEvent(i8SBSwitchControllerRequestVO.getLoanEvent());
        this.setLoanEventStatus(i8SBSwitchControllerRequestVO.getLoanEventStatus());
        this.setOrigSource(i8SBSwitchControllerRequestVO.getOrigSource());
        this.setSourceRequestId(i8SBSwitchControllerRequestVO.getSourceRequestId());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getLoanEvent())) {
            throw new I8SBValidationException("[Failed] Loan Event:" + this.getLoanEvent());
        }
        if (StringUtils.isEmpty(this.getLoanEventStatus())) {
            throw new I8SBValidationException("[Failed] Message Loan Event Status:" + this.getLoanEventStatus());
        }
        if (StringUtils.isEmpty(this.getSourceRequestId())) {
            throw new I8SBValidationException("[Failed] Transaction Source Request Id:" + this.getSourceRequestId());
        }
        if (StringUtils.isEmpty(this.getOrigSource())) {
            throw new I8SBValidationException("[Failed] Message External Orig Source:" + this.getOrigSource());
        }
        return true;
    }
}
