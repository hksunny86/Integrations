package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.optasiaVO.*;
import com.inov8.integration.webservice.optasiaVO.Interest;
import com.inov8.integration.webservice.optasiaVO.LoanOffers;
import com.inov8.integration.webservice.optasiaVO.MaturityDetails;
import com.inov8.integration.webservice.optasiaVO.OneOffCharges;

import java.awt.font.TextHitInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
public class OfferListForCommodityResponse extends Response implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

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
    private Map<String, List<?>> collectionOfList = new HashMap();


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

        if (eligibilityStatus != null) {
            com.inov8.integration.webservice.optasiaVO.EligibilityStatus eligibilityStatus;
            List<com.inov8.integration.webservice.optasiaVO.EligibilityStatus> eligibilityStatusList = new ArrayList<>();
            for (int i = 0; i < this.getEligibilityStatus().size(); i++) {
                eligibilityStatus = new com.inov8.integration.webservice.optasiaVO.EligibilityStatus();
                eligibilityStatus.setEligible(this.getEligibilityStatus().get(i).getIsEligible());
                eligibilityStatus.setEligibilityStatus(this.getEligibilityStatus().get(i).getEligibilityStatus());

                eligibilityStatusList.add(eligibilityStatus);

                collectionOfList.put("EligibilityStatus", eligibilityStatusList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
            }
        }


        if (loanOffersByLoanProductGroup != null) {
            List<com.inov8.integration.webservice.optasiaVO.LoanOffersByLoanProductGroup> loanOffersByLoanProductGroupList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.MaturityDetails> maturityDetailsList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Interest> interestList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.OneOffCharges> oneOffChargesList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.RecurringCharges> recurringChargesList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.LoanOffers> loanOffersList = new ArrayList<>();

            com.inov8.integration.webservice.optasiaVO.LoanOffersByLoanProductGroup loanOffersByLoanProductGroup;
            com.inov8.integration.webservice.optasiaVO.MaturityDetails maturityDetails;
            com.inov8.integration.webservice.optasiaVO.Interest interest;
            com.inov8.integration.webservice.optasiaVO.OneOffCharges oneOffCharges;
            com.inov8.integration.webservice.optasiaVO.RecurringCharges recurringCharges;
            com.inov8.integration.webservice.optasiaVO.LoanOffers loanOffers;

            for (int i = 0; i < this.getLoanOffersByLoanProductGroup().size(); i++) {

                loanOffersByLoanProductGroup = new com.inov8.integration.webservice.optasiaVO.LoanOffersByLoanProductGroup();
                maturityDetails = new com.inov8.integration.webservice.optasiaVO.MaturityDetails();
                interest = new com.inov8.integration.webservice.optasiaVO.Interest();
                oneOffCharges = new com.inov8.integration.webservice.optasiaVO.OneOffCharges();
                recurringCharges = new com.inov8.integration.webservice.optasiaVO.RecurringCharges();
                loanOffers = new com.inov8.integration.webservice.optasiaVO.LoanOffers();


                loanOffersByLoanProductGroup.setLoanProductGroup(this.getLoanOffersByLoanProductGroup().get(i).getLoanProductGroup());
                loanOffers.setOfferClass(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getOfferClass());
                loanOffers.setOfferName(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getOfferName());
                loanOffers.setCurrencyCode(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getCurrencyCode());
                loanOffers.setPrincipalFrom(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getPrincipalFrom()));
                loanOffers.setPrincipalTo(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getPrincipalTo()));
                loanOffers.setSetupFees(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getSetupFees()));
                loanOffers.setCommodityType(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getCommodityType());
                loanOffers.setLoanPlanId(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getLoanPlanId()));
                loanOffers.setLoanPlanName(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getLoanPlanName());
                loanOffersByLoanProductGroup.setLoanProductGroup(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getLoanProductGroup());
                maturityDetails.setMaturityDuration(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getMaturityDuration()));
                interest.setInterestName(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestName());
                interest.setInterestType(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestType());
                interest.setInterestValue(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestValue()));
                interest.setInterestVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestVAT()));
                interest.setDaysOffset(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getDaysOffset()));
                interest.setInterval(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterval()));
                oneOffCharges.setChargeName(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeName());
                oneOffCharges.setChargeType(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeType());
                oneOffCharges.setChargeValue(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeValue()));
                oneOffCharges.setChargeVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeVAT()));
                oneOffCharges.setDaysOffset(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getDaysOffset()));
                recurringCharges.setChargeName(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeName());
                recurringCharges.setChargeType(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeType());
                recurringCharges.setChargeValue(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeValue()));
                recurringCharges.setChargeVAT(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeVAT()));
                recurringCharges.setInterval(String.valueOf(this.getLoanOffersByLoanProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getInterval()));

                interestList.add(interest);
                loanOffersList.add(loanOffers);
                maturityDetailsList.add(maturityDetails);
                oneOffChargesList.add(oneOffCharges);
                recurringChargesList.add(recurringCharges);
                loanOffersByLoanProductGroupList.add(loanOffersByLoanProductGroup);

                collectionOfList.put("Interest", interestList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("LoanOffers", loanOffersList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("MaturityDetails", maturityDetailsList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("OneOffCharges", oneOffChargesList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("RecurringCharges", recurringChargesList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("LoanOffersByLoanProductGroup", loanOffersByLoanProductGroupList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

            }
        }

        if (outstandingStatus != null) {

            List<com.inov8.integration.webservice.optasiaVO.OutstandingStatus> outstandingStatusList = new ArrayList<>();
            com.inov8.integration.webservice.optasiaVO.OutstandingStatus outstandingStatus;

            for (int i = 0; i < this.getOutstandingStatus().size(); i++) {
                outstandingStatus = new com.inov8.integration.webservice.optasiaVO.OutstandingStatus();
                outstandingStatus.setCurrencyCode(this.getOutstandingStatus().get(i).getCurrencyCode());
                outstandingStatus.setNumOutstandingLoans(String.valueOf(this.getOutstandingStatus().get(i).getNumOutstandingLoans()));
                outstandingStatus.setTotalGross(String.valueOf(this.getOutstandingStatus().get(i).getTotalGross()));
                outstandingStatus.setTotalPrincipal(String.valueOf(this.getOutstandingStatus().get(i).getTotalPrincipal()));
                outstandingStatus.setTotalSetupFees(String.valueOf(this.getOutstandingStatus().get(i).getTotalSetupFees()));
                outstandingStatus.setTotalInterest(String.valueOf(this.getOutstandingStatus().get(i).getTotalInterest()));
                outstandingStatus.setTotalInterestVAT(String.valueOf(this.getOutstandingStatus().get(i).getTotalInterestVAT()));
                outstandingStatus.setTotalCharges(String.valueOf(this.getOutstandingStatus().get(i).getTotalCharges()));
                outstandingStatus.setTotalChargesVAT(String.valueOf(this.getOutstandingStatus().get(i).getTotalChargesVAT()));
                outstandingStatus.setTotalPendingLoans(String.valueOf(this.getOutstandingStatus().get(i).getTotalPendingLoans()));
                outstandingStatus.setTotalPendingRecoveries(String.valueOf(this.getOutstandingStatus().get(i).getTotalPendingRecoveries()));

                outstandingStatusList.add(outstandingStatus);
                collectionOfList.put("OutstandingStatus", outstandingStatusList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
            }
        }


        return i8SBSwitchControllerResponseVO;
    }
}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isEligible",
        "eligibilityStatus"
})
@Generated("jsonschema2pojo")
class OfferEligibilitystatus implements Serializable {

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
class OfferInterest implements Serializable {

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
class OfferLoanOffer implements Serializable {

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
class LoanOffersByLoanProductGroup implements Serializable {

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
class OfferMaturityDetails implements Serializable {

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
class OfferOneOffCharge implements Serializable {

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
class OfferOutstandingstatus implements Serializable {

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
class OfferRecurringCharge implements Serializable {

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