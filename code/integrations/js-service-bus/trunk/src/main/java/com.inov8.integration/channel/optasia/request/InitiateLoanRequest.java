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
        "offerName",
        "loanAmount",
        "upToPeriod"
})
public class InitiateLoanRequest extends Request {

    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("loanAmount")
    private String loanAmount;
    @JsonProperty("upToPeriod")
    private String upToPeriod;

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

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getUpToPeriod() {
        return upToPeriod;
    }

    public void setUpToPeriod(String upToPeriod) {
        this.upToPeriod = upToPeriod;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setIdentityType(i8SBSwitchControllerRequestVO.getIdentityType());
        this.setIdentityValue(i8SBSwitchControllerRequestVO.getIdentityValue());
        this.setOrigSource(i8SBSwitchControllerRequestVO.getOrigSource());
        this.setOfferName(i8SBSwitchControllerRequestVO.getOfferName());
        this.setLoanAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setUpToPeriod(i8SBSwitchControllerRequestVO.getUpToPeriod());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

//        if (StringUtils.isEmpty(this.getIdentityType())) {
//            throw new I8SBValidationException("[Failed] Identity Type:" + this.getIdentityType());
//        }
//        if (StringUtils.isEmpty(this.getIdentityValue())) {
//            throw new I8SBValidationException("[Failed] Identity Value:" + this.getIdentityValue());
//        }
//        if (StringUtils.isEmpty(this.getOrigSource())) {
//            throw new I8SBValidationException("[Failed] Orig Source:" + this.getOrigSource());
//        }
//        if (StringUtils.isEmpty(this.getOfferName())) {
//            throw new I8SBValidationException("[Failed] Offer Name:" + this.getOfferName());
//        }
//        if (StringUtils.isEmpty(this.getLoanAmount())) {
//            throw new I8SBValidationException("[Failed] Loan Amount:" + this.getLoanAmount());
//        }
//        if (StringUtils.isEmpty(this.getUpToPeriod())) {
//            throw new I8SBValidationException("[Failed] Up To Period:" + this.getUpToPeriod());
//        }

        return true;
    }
}
