package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.optasiaVO.EligibilityStatus;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identityValue",
        "identityType",
        "origSource",
        "receivedTimestamp",
        "eligibilityStatus",
        "loanOffersByLoanProductGroup",
        "outstandingStatus"
})
public class OfferListForCommodityResponse extends Response{

    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("eligibilityStatus")
    private List<OfferEligibilitystatus> eligibilityStatus;
    @JsonProperty("loanOffersByLoanProductGroup")
    private List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroup;
    @JsonProperty("outstandingStatus")
    private List<OfferOutstandingstatus> outstandingStatus;
    private String responseCode;
    private String responseDescription;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @JsonProperty("identityValue")
    public String getIdentityValue() {
        return identityValue;
    }

    @JsonProperty("identityValue")
    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    @JsonProperty("identityType")
    public String getIdentityType() {
        return identityType;
    }

    @JsonProperty("identityType")
    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    @JsonProperty("origSource")
    public String getOrigSource() {
        return origSource;
    }

    @JsonProperty("origSource")
    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    @JsonProperty("receivedTimestamp")
    public String getReceivedTimestamp() {
        return receivedTimestamp;
    }

    @JsonProperty("receivedTimestamp")
    public void setReceivedTimestamp(String receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    @JsonProperty("eligibilityStatus")
    public List<OfferEligibilitystatus> getEligibilityStatus() {
        return eligibilityStatus;
    }

    @JsonProperty("eligibilityStatus")
    public void setEligibilityStatus(List<OfferEligibilitystatus> eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    @JsonProperty("loanOffersByLoanProductGroup")
    public List<LoanOffersByLoanProductGroup> getLoanOffersByLoanProductGroup() {
        return loanOffersByLoanProductGroup;
    }

    @JsonProperty("loanOffersByLoanProductGroup")
    public void setLoanOffersByLoanProductGroup(List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroup) {
        this.loanOffersByLoanProductGroup = loanOffersByLoanProductGroup;
    }

    @JsonProperty("outstandingStatus")
    public List<OfferOutstandingstatus> getOutstandingStatus() {
        return outstandingStatus;
    }

    @JsonProperty("outstandingStatus")
    public void setOutstandingStatus(List<OfferOutstandingstatus> outstandingStatus) {
        this.outstandingStatus = outstandingStatus;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        i8SBSwitchControllerResponseVO.setResponseCode("00");
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceptionTimestamp(this.getReceivedTimestamp());
        i8SBSwitchControllerResponseVO.setEligible(this.getEligibilityStatus().get(0).getIsEligible());
        i8SBSwitchControllerResponseVO.setEligibilityStatus(this.getEligibilityStatus().get(0).getEligibilityStatus());
        i8SBSwitchControllerResponseVO.setLoanProductGroup(this.getLoanOffersByLoanProductGroup().get(0).getLoanProductGroup());
        i8SBSwitchControllerResponseVO.setOfferClass(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getOfferClass());
        i8SBSwitchControllerResponseVO.setOfferName(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getOfferName());
        i8SBSwitchControllerResponseVO.setCurrencyCode(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getCurrencyCode());
        i8SBSwitchControllerResponseVO.setPrincipalFrom(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getPrincipalFrom()));
        i8SBSwitchControllerResponseVO.setPrincipalTo(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getPrincipalTo()));
        i8SBSwitchControllerResponseVO.setSetupFees(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getSetupFees()));
        i8SBSwitchControllerResponseVO.setCommodityType(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getCommodityType());
        i8SBSwitchControllerResponseVO.setLoanPlanId(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getLoanPlanId()));
        i8SBSwitchControllerResponseVO.setLoanPlanName(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getLoanPlanName());
        i8SBSwitchControllerResponseVO.setLoanProductGroup(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getLoanProductGroup());
        i8SBSwitchControllerResponseVO.setMaturityDuration(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getMaturityDuration()));
        i8SBSwitchControllerResponseVO.setInterestName(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getInterest().getInterestName());
        i8SBSwitchControllerResponseVO.setInterestType(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getInterest().getInterestType());
        i8SBSwitchControllerResponseVO.setInterestValue(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getInterest().getInterestValue()));
        i8SBSwitchControllerResponseVO.setInterestVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getInterest().getInterestVAT()));
        i8SBSwitchControllerResponseVO.setDaysOffset(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getInterest().getDaysOffset()));
        i8SBSwitchControllerResponseVO.setInterval(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getInterest().getInterval()));
        i8SBSwitchControllerResponseVO.setChargeName(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getOneOffCharges().get(0).getChargeName());
        i8SBSwitchControllerResponseVO.setChargeType(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getOneOffCharges().get(0).getChargeType());
        i8SBSwitchControllerResponseVO.setChargeValue(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getOneOffCharges().get(0).getChargeValue()));
        i8SBSwitchControllerResponseVO.setChargeVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getOneOffCharges().get(0).getChargeVAT()));
        i8SBSwitchControllerResponseVO.setChargeVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getOneOffCharges().get(0).getChargeVAT()));
        i8SBSwitchControllerResponseVO.setChargeName(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getRecurringCharges().get(0).getChargeName());
        i8SBSwitchControllerResponseVO.setChargeType(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getRecurringCharges().get(0).getChargeType());
        i8SBSwitchControllerResponseVO.setChargeValue(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getRecurringCharges().get(0).getChargeValue()));
        i8SBSwitchControllerResponseVO.setChargeVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getRecurringCharges().get(0).getChargeVAT()));
        i8SBSwitchControllerResponseVO.setChargeVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getRecurringCharges().get(0).getChargeVAT()));
        i8SBSwitchControllerResponseVO.setDaysOffset(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getRecurringCharges().get(0).getDaysOffset()));
        i8SBSwitchControllerResponseVO.setInterval(String.valueOf(this.getLoanOffersByLoanProductGroup().get(0).getLoanOffers().get(0).getMaturityDetails().getRecurringCharges().get(0).getInterval()));
        i8SBSwitchControllerResponseVO.setCurrencyCode(this.getOutstandingStatus().get(0).getCurrencyCode());
        i8SBSwitchControllerResponseVO.setNumOutstandingLoans(String.valueOf(this.getOutstandingStatus().get(0).getNumOutstandingLoans()));
        i8SBSwitchControllerResponseVO.setTotalGross(String.valueOf(this.getOutstandingStatus().get(0).getTotalGross()));
        i8SBSwitchControllerResponseVO.setTotalSetupFees(String.valueOf(this.getOutstandingStatus().get(0).getTotalSetupFees()));
        i8SBSwitchControllerResponseVO.setTotalInterest(String.valueOf(this.getOutstandingStatus().get(0).getTotalInterest()));
        i8SBSwitchControllerResponseVO.setTotalInterestVAT(String.valueOf(this.getOutstandingStatus().get(0).getTotalInterestVAT()));
        i8SBSwitchControllerResponseVO.setTotalCharges(String.valueOf(this.getOutstandingStatus().get(0).getTotalCharges()));
        i8SBSwitchControllerResponseVO.setTotalChargesVAT(String.valueOf(this.getOutstandingStatus().get(0).getTotalChargesVAT()));
        i8SBSwitchControllerResponseVO.setTotalPendingLoans(String.valueOf(this.getOutstandingStatus().get(0).getTotalPendingLoans()));
        i8SBSwitchControllerResponseVO.setTotalPendingRecoveries(String.valueOf(this.getOutstandingStatus().get(0).getTotalPendingRecoveries()));

        return i8SBSwitchControllerResponseVO;
    }
}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isEligible",
        "eligibilityStatus"
})
@Generated("jsonschema2pojo")
 class OfferEligibilitystatus {

    @JsonProperty("isEligible")
    private Boolean isEligible;
    @JsonProperty("eligibilityStatus")
    private String eligibilityStatus;

    @JsonProperty("isEligible")
    public Boolean getIsEligible() {
        return isEligible;
    }

    @JsonProperty("isEligible")
    public void setIsEligible(Boolean isEligible) {
        this.isEligible = isEligible;
    }

    @JsonProperty("eligibilityStatus")
    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    @JsonProperty("eligibilityStatus")
    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "interestName",
        "interestType",
        "interestValue",
        "interestVAT",
        "daysOffset",
        "interval"
})
 class OfferInterest {

    @JsonProperty("interestName")
    private String interestName;
    @JsonProperty("interestType")
    private String interestType;
    @JsonProperty("interestValue")
    private Float interestValue;
    @JsonProperty("interestVAT")
    private Integer interestVAT;
    @JsonProperty("daysOffset")
    private Integer daysOffset;
    @JsonProperty("interval")
    private Integer interval;

    @JsonProperty("interestName")
    public String getInterestName() {
        return interestName;
    }

    @JsonProperty("interestName")
    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    @JsonProperty("interestType")
    public String getInterestType() {
        return interestType;
    }

    @JsonProperty("interestType")
    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    @JsonProperty("interestValue")
    public Float getInterestValue() {
        return interestValue;
    }

    @JsonProperty("interestValue")
    public void setInterestValue(Float interestValue) {
        this.interestValue = interestValue;
    }

    @JsonProperty("interestVAT")
    public Integer getInterestVAT() {
        return interestVAT;
    }

    @JsonProperty("interestVAT")
    public void setInterestVAT(Integer interestVAT) {
        this.interestVAT = interestVAT;
    }

    @JsonProperty("daysOffset")
    public Integer getDaysOffset() {
        return daysOffset;
    }

    @JsonProperty("daysOffset")
    public void setDaysOffset(Integer daysOffset) {
        this.daysOffset = daysOffset;
    }

    @JsonProperty("interval")
    public Integer getInterval() {
        return interval;
    }

    @JsonProperty("interval")
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "offerClass",
        "offerName",
        "currencyCode",
        "principalFrom",
        "principalTo",
        "setupFees",
        "commodityType",
        "loanPlanId",
        "loanPlanName",
        "loanProductGroup",
        "maturityDetails"
})
 class OfferLoanOffer {

    @JsonProperty("offerClass")
    private String offerClass;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("principalFrom")
    private Integer principalFrom;
    @JsonProperty("principalTo")
    private Integer principalTo;
    @JsonProperty("setupFees")
    private Integer setupFees;
    @JsonProperty("commodityType")
    private String commodityType;
    @JsonProperty("loanPlanId")
    private Integer loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("maturityDetails")
    private OfferMaturityDetails maturityDetails;

    @JsonProperty("offerClass")
    public String getOfferClass() {
        return offerClass;
    }

    @JsonProperty("offerClass")
    public void setOfferClass(String offerClass) {
        this.offerClass = offerClass;
    }

    @JsonProperty("offerName")
    public String getOfferName() {
        return offerName;
    }

    @JsonProperty("offerName")
    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("principalFrom")
    public Integer getPrincipalFrom() {
        return principalFrom;
    }

    @JsonProperty("principalFrom")
    public void setPrincipalFrom(Integer principalFrom) {
        this.principalFrom = principalFrom;
    }

    @JsonProperty("principalTo")
    public Integer getPrincipalTo() {
        return principalTo;
    }

    @JsonProperty("principalTo")
    public void setPrincipalTo(Integer principalTo) {
        this.principalTo = principalTo;
    }

    @JsonProperty("setupFees")
    public Integer getSetupFees() {
        return setupFees;
    }

    @JsonProperty("setupFees")
    public void setSetupFees(Integer setupFees) {
        this.setupFees = setupFees;
    }

    @JsonProperty("commodityType")
    public String getCommodityType() {
        return commodityType;
    }

    @JsonProperty("commodityType")
    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    @JsonProperty("loanPlanId")
    public Integer getLoanPlanId() {
        return loanPlanId;
    }

    @JsonProperty("loanPlanId")
    public void setLoanPlanId(Integer loanPlanId) {
        this.loanPlanId = loanPlanId;
    }

    @JsonProperty("loanPlanName")
    public String getLoanPlanName() {
        return loanPlanName;
    }

    @JsonProperty("loanPlanName")
    public void setLoanPlanName(String loanPlanName) {
        this.loanPlanName = loanPlanName;
    }

    @JsonProperty("loanProductGroup")
    public String getLoanProductGroup() {
        return loanProductGroup;
    }

    @JsonProperty("loanProductGroup")
    public void setLoanProductGroup(String loanProductGroup) {
        this.loanProductGroup = loanProductGroup;
    }

    @JsonProperty("maturityDetails")
    public OfferMaturityDetails getMaturityDetails() {
        return maturityDetails;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(OfferMaturityDetails maturityDetails) {
        this.maturityDetails = maturityDetails;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanProductGroup",
        "loanOffers"
})
 class LoanOffersByLoanProductGroup {

    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("loanOffers")
    private List<OfferLoanOffer> loanOffers;

    @JsonProperty("loanProductGroup")
    public String getLoanProductGroup() {
        return loanProductGroup;
    }

    @JsonProperty("loanProductGroup")
    public void setLoanProductGroup(String loanProductGroup) {
        this.loanProductGroup = loanProductGroup;
    }

    @JsonProperty("loanOffers")
    public List<OfferLoanOffer> getLoanOffers() {
        return loanOffers;
    }

    @JsonProperty("loanOffers")
    public void setLoanOffers(List<OfferLoanOffer> loanOffers) {
        this.loanOffers = loanOffers;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration",
        "interest",
        "oneOffCharges",
        "recurringCharges"
})
 class OfferMaturityDetails {

    @JsonProperty("maturityDuration")
    private Integer maturityDuration;
    @JsonProperty("interest")
    private OfferInterest interest;
    @JsonProperty("oneOffCharges")
    private List<OfferOneOffCharge> oneOffCharges;
    @JsonProperty("recurringCharges")
    private List<OfferRecurringCharge> recurringCharges;

    @JsonProperty("maturityDuration")
    public Integer getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(Integer maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    @JsonProperty("interest")
    public OfferInterest getInterest() {
        return interest;
    }

    @JsonProperty("interest")
    public void setInterest(OfferInterest interest) {
        this.interest = interest;
    }

    @JsonProperty("oneOffCharges")
    public List<OfferOneOffCharge> getOneOffCharges() {
        return oneOffCharges;
    }

    @JsonProperty("oneOffCharges")
    public void setOneOffCharges(List<OfferOneOffCharge> oneOffCharges) {
        this.oneOffCharges = oneOffCharges;
    }

    @JsonProperty("recurringCharges")
    public List<OfferRecurringCharge> getRecurringCharges() {
        return recurringCharges;
    }

    @JsonProperty("recurringCharges")
    public void setRecurringCharges(List<OfferRecurringCharge> recurringCharges) {
        this.recurringCharges = recurringCharges;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "chargeType",
        "chargeValue",
        "chargeVAT",
        "daysOffset"
})
 class OfferOneOffCharge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeType")
    private String chargeType;
    @JsonProperty("chargeValue")
    private Integer chargeValue;
    @JsonProperty("chargeVAT")
    private Float chargeVAT;
    @JsonProperty("daysOffset")
    private Integer daysOffset;

    @JsonProperty("chargeName")
    public String getChargeName() {
        return chargeName;
    }

    @JsonProperty("chargeName")
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @JsonProperty("chargeType")
    public String getChargeType() {
        return chargeType;
    }

    @JsonProperty("chargeType")
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @JsonProperty("chargeValue")
    public Integer getChargeValue() {
        return chargeValue;
    }

    @JsonProperty("chargeValue")
    public void setChargeValue(Integer chargeValue) {
        this.chargeValue = chargeValue;
    }

    @JsonProperty("chargeVAT")
    public Float getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(Float chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

    @JsonProperty("daysOffset")
    public Integer getDaysOffset() {
        return daysOffset;
    }

    @JsonProperty("daysOffset")
    public void setDaysOffset(Integer daysOffset) {
        this.daysOffset = daysOffset;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currencyCode",
        "numOutstandingLoans",
        "totalGross",
        "totalPrincipal",
        "totalSetupFees",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "totalPendingLoans",
        "totalPendingRecoveries"
})
 class OfferOutstandingstatus {

    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("numOutstandingLoans")
    private Integer numOutstandingLoans;
    @JsonProperty("totalGross")
    private Integer totalGross;
    @JsonProperty("totalPrincipal")
    private Integer totalPrincipal;
    @JsonProperty("totalSetupFees")
    private Integer totalSetupFees;
    @JsonProperty("totalInterest")
    private Integer totalInterest;
    @JsonProperty("totalInterestVAT")
    private Integer totalInterestVAT;
    @JsonProperty("totalCharges")
    private Integer totalCharges;
    @JsonProperty("totalChargesVAT")
    private Integer totalChargesVAT;
    @JsonProperty("totalPendingLoans")
    private Integer totalPendingLoans;
    @JsonProperty("totalPendingRecoveries")
    private Integer totalPendingRecoveries;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("numOutstandingLoans")
    public Integer getNumOutstandingLoans() {
        return numOutstandingLoans;
    }

    @JsonProperty("numOutstandingLoans")
    public void setNumOutstandingLoans(Integer numOutstandingLoans) {
        this.numOutstandingLoans = numOutstandingLoans;
    }

    @JsonProperty("totalGross")
    public Integer getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(Integer totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalPrincipal")
    public Integer getTotalPrincipal() {
        return totalPrincipal;
    }

    @JsonProperty("totalPrincipal")
    public void setTotalPrincipal(Integer totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    @JsonProperty("totalSetupFees")
    public Integer getTotalSetupFees() {
        return totalSetupFees;
    }

    @JsonProperty("totalSetupFees")
    public void setTotalSetupFees(Integer totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
    }

    @JsonProperty("totalInterest")
    public Integer getTotalInterest() {
        return totalInterest;
    }

    @JsonProperty("totalInterest")
    public void setTotalInterest(Integer totalInterest) {
        this.totalInterest = totalInterest;
    }

    @JsonProperty("totalInterestVAT")
    public Integer getTotalInterestVAT() {
        return totalInterestVAT;
    }

    @JsonProperty("totalInterestVAT")
    public void setTotalInterestVAT(Integer totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    @JsonProperty("totalCharges")
    public Integer getTotalCharges() {
        return totalCharges;
    }

    @JsonProperty("totalCharges")
    public void setTotalCharges(Integer totalCharges) {
        this.totalCharges = totalCharges;
    }

    @JsonProperty("totalChargesVAT")
    public Integer getTotalChargesVAT() {
        return totalChargesVAT;
    }

    @JsonProperty("totalChargesVAT")
    public void setTotalChargesVAT(Integer totalChargesVAT) {
        this.totalChargesVAT = totalChargesVAT;
    }

    @JsonProperty("totalPendingLoans")
    public Integer getTotalPendingLoans() {
        return totalPendingLoans;
    }

    @JsonProperty("totalPendingLoans")
    public void setTotalPendingLoans(Integer totalPendingLoans) {
        this.totalPendingLoans = totalPendingLoans;
    }

    @JsonProperty("totalPendingRecoveries")
    public Integer getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    @JsonProperty("totalPendingRecoveries")
    public void setTotalPendingRecoveries(Integer totalPendingRecoveries) {
        this.totalPendingRecoveries = totalPendingRecoveries;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "chargeType",
        "chargeValue",
        "chargeVAT",
        "daysOffset",
        "interval"
})
 class OfferRecurringCharge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeType")
    private String chargeType;
    @JsonProperty("chargeValue")
    private Integer chargeValue;
    @JsonProperty("chargeVAT")
    private Float chargeVAT;
    @JsonProperty("daysOffset")
    private Integer daysOffset;
    @JsonProperty("interval")
    private Integer interval;

    @JsonProperty("chargeName")
    public String getChargeName() {
        return chargeName;
    }

    @JsonProperty("chargeName")
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @JsonProperty("chargeType")
    public String getChargeType() {
        return chargeType;
    }

    @JsonProperty("chargeType")
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @JsonProperty("chargeValue")
    public Integer getChargeValue() {
        return chargeValue;
    }

    @JsonProperty("chargeValue")
    public void setChargeValue(Integer chargeValue) {
        this.chargeValue = chargeValue;
    }

    @JsonProperty("chargeVAT")
    public Float getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(Float chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

    @JsonProperty("daysOffset")
    public Integer getDaysOffset() {
        return daysOffset;
    }

    @JsonProperty("daysOffset")
    public void setDaysOffset(Integer daysOffset) {
        this.daysOffset = daysOffset;
    }

    @JsonProperty("interval")
    public Integer getInterval() {
        return interval;
    }

    @JsonProperty("interval")
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

}