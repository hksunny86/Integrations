package com.inov8.integration.channel.optasia.request;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.loyalty.Ad;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.ocsp.Req;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sourceRequestId",
        "offerName",
        "amount",
        "externalLoanId",
        "additionalIds"
})
@Generated("jsonschema2pojo")
public class LoanOfferRequest extends Request {

    @JsonProperty("sourceRequestId")
    private String sourceRequestId;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("externalLoanId")
    private String externalLoanId;
    @JsonProperty("additionalIds")
    private List<AdditionalId> additionalIds;
    private List<AdditionalId> additionalIdList;
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

    @JsonProperty("externalLoanId")
    public String getExternalLoanId() {
        return externalLoanId;
    }

    @JsonProperty("externalLoanId")
    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
    }

    @JsonProperty("additionalIds")
    public List<AdditionalId> getAdditionalIds() {
        return additionalIds;
    }

    @JsonProperty("additionalIds")
    public void setAdditionalIds(List<AdditionalId> additionalIds) {
        this.additionalIds = additionalIds;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        additionalIdList = new ArrayList<>();
        AdditionalId request = new AdditionalId();

        request.setMerchantId(i8SBSwitchControllerRequestVO.getMerchantId());
        request.setFed(i8SBSwitchControllerRequestVO.getFed());
        request.setLoanPurpose(i8SBSwitchControllerRequestVO.getLoanPurpose());

        additionalIdList.add(request);

        this.setSourceRequestId(i8SBSwitchControllerRequestVO.getSourceRequestId());
        this.setOfferName(i8SBSwitchControllerRequestVO.getOfferName());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setExternalLoanId(i8SBSwitchControllerRequestVO.getExternalLoanId());
        this.setAdditionalIds(additionalIdList);
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
//        if (StringUtils.isEmpty(this.getExternalLoanId())) {
//            throw new I8SBValidationException("[Failed] Message External Loan Id:" + this.getExternalLoanId());
//        }

        return true;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "merchantId",
        "FED",
        "loanPurpose"
})
class AdditionalId {

    @JsonProperty("merchantId")
    private String merchantId;
    @JsonProperty("FED")
    private String fed;
    @JsonProperty("loanPurpose")
    private String loanPurpose;

    @JsonProperty("merchantId")
    public String getMerchantId() {
        return merchantId;
    }

    @JsonProperty("merchantId")
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @JsonProperty("FED")
    public String getFed() {
        return fed;
    }

    @JsonProperty("FED")
    public void setFed(String fed) {
        this.fed = fed;
    }

    @JsonProperty("loanPurpose")
    public String getLoanPurpose() {
        return loanPurpose;
    }

    @JsonProperty("loanPurpose")
    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

}