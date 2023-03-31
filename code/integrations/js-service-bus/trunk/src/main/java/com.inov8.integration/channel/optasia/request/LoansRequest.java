package com.inov8.integration.channel.optasia.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identityType",
        "origSource",
        "identityValue",
        "filterCommodityType",
        "filterLoanState"
})
public class LoansRequest extends Request {

    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("filterCommodityType")
    private String filterCommodityType;
    @JsonProperty("filterLoanState")
    private String filterLoanState;

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getOrigSource() {
        return origSource;
    }

    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    public String getIdentityValue() {
        return identityValue;
    }

    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    public String getFilterCommodityType() {
        return filterCommodityType;
    }

    public void setFilterCommodityType(String filterCommodityType) {
        this.filterCommodityType = filterCommodityType;
    }

    public String getFilterLoanState() {
        return filterLoanState;
    }

    public void setFilterLoanState(String filterLoanState) {
        this.filterLoanState = filterLoanState;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setIdentityType(i8SBSwitchControllerRequestVO.getIdentityType());
        this.setIdentityValue(i8SBSwitchControllerRequestVO.getIdentityValue());
        this.setOrigSource(i8SBSwitchControllerRequestVO.getOrigSource());
        this.setFilterCommodityType(i8SBSwitchControllerRequestVO.getFilterCommodityType());
        this.setFilterLoanState(i8SBSwitchControllerRequestVO.getFilterLoanState());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getIdentityType())) {
            throw new I8SBValidationException("[Failed] Identity Type:" + this.getIdentityType());
        }
        if (StringUtils.isEmpty(this.getIdentityValue())) {
            throw new I8SBValidationException("[Failed] Identity Value:" + this.getIdentityValue());
        }
        if (StringUtils.isEmpty(this.getOrigSource())) {
            throw new I8SBValidationException("[Failed] Orig Source:" + this.getOrigSource());
        }

        return true;
    }
}
