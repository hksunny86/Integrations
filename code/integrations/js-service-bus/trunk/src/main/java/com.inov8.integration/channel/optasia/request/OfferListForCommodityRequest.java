package com.inov8.integration.channel.optasia.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sourceRequestId",
        "offerName",
        "amount",
        "additionalInfo"
})
public class OfferListForCommodityRequest extends Request {

    @JsonProperty("sourceRequestId")
    private String sourceRequestId;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("additionalInfo")
    private AdditionalInfo additionalInfo;
    private String identityType;
    private String identityValue;
    private String origSource;
    private String commodityType;

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentityValue() {
        return identityValue;
    }

    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    public String getOrigSource() {
        return origSource;
    }

    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    @JsonProperty("sourceRequestId")
    public String getSourceRequestId() {
        return sourceRequestId;
    }

    @JsonProperty("sourceRequestId")
    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    @JsonProperty("offerName")
    public String getOfferName() {
        return offerName;
    }

    @JsonProperty("offerName")
    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("additionalInfo")
    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    @JsonProperty("additionalInfo")
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setIdentityType(i8SBSwitchControllerRequestVO.getIdentityType());
        this.setIdentityValue(i8SBSwitchControllerRequestVO.getIdentityValue());
        this.setOrigSource(i8SBSwitchControllerRequestVO.getOrigSource());
        this.setCommodityType(i8SBSwitchControllerRequestVO.getCommodityType());
        this.setSourceRequestId(i8SBSwitchControllerRequestVO.getSourceRequestId());
        this.setOfferName(i8SBSwitchControllerRequestVO.getOfferName());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
//        this.getAdditionalInfo().setFed(i8SBSwitchControllerRequestVO.getFed());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

//        if (StringUtils.isEmpty(this.getSourceRequestId())) {
//            throw new I8SBValidationException("[Failed] Source Request Id:" + this.getSourceRequestId());
//        }
//        if (StringUtils.isEmpty(this.getOfferName())) {
//            throw new I8SBValidationException("[Failed] Message Offer Name:" + this.getOfferName());
//        }
//        if (StringUtils.isEmpty(this.getAmount())) {
//            throw new I8SBValidationException("[Failed] Transaction Amount:" + this.getAmount());
//        }
        return true;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "FED"
})
class AdditionalInfo {

    @JsonProperty("FED")
    private String fed;

    @JsonProperty("FED")
    public String getFed() {
        return fed;
    }

    @JsonProperty("FED")
    public void setFed(String fed) {
        this.fed = fed;
    }

}