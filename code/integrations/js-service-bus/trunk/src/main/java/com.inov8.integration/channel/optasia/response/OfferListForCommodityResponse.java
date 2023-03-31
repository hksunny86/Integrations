package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.optasia.service.OptasiaService;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.webservice.optasiaVO.*;
import com.inov8.integration.webservice.optasiaVO.Interest;
import com.inov8.integration.webservice.optasiaVO.LoanOffers;
import com.inov8.integration.webservice.optasiaVO.MaturityDetails;
import com.inov8.integration.webservice.optasiaVO.OneOffCharges;
import org.slf4j.LoggerFactory;

import java.awt.font.TextHitInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identityValue",
        "identityType",
        "origSource",
        "receivedTimestamp",
        "eligibilityStatus",
        "loanOffersByProductGroup",
        "outstandingStatus"
})
public class OfferListForCommodityResponse extends Response implements Serializable {


    private static org.slf4j.Logger logger = LoggerFactory.getLogger(OfferListForCommodityResponse.class.getSimpleName());

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
    private EligibilityStatus eligibilityStatus;
    @JsonProperty("loanOffersByProductGroup")
    private List<LoanOffersByProductGroup> loanOffersByProductGroup;
    @JsonProperty("outstandingStatus")
    private List<Outstandingstatus> outstandingStatus;
    private String responseCode;
    private Map<String, List<?>> collectionOfList = new HashMap();
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;


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
    public EligibilityStatus getEligibilityStatus() {
        return eligibilityStatus;
    }

    @JsonProperty("eligibilityStatus")
    public void setEligibilityStatus(EligibilityStatus eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    @JsonProperty("loanOffersByProductGroup")
    public List<LoanOffersByProductGroup> getLoanOffersByProductGroup() {
        return loanOffersByProductGroup;
    }

    @JsonProperty("loanOffersByProductGroup")
    public void setLoanOffersByProductGroup(List<LoanOffersByProductGroup> loanOffersByProductGroup) {
        this.loanOffersByProductGroup = loanOffersByProductGroup;
    }

    @JsonProperty("outstandingStatus")
    public List<Outstandingstatus> getOutstandingStatus() {
        return outstandingStatus;
    }

    @JsonProperty("outstandingStatus")
    public void setOutstandingStatus(List<Outstandingstatus> outstandingStatus) {
        this.outstandingStatus = outstandingStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();


        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
            i8SBSwitchControllerResponseVO.setCode(this.getCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
        }
        i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
        i8SBSwitchControllerResponseVO.setCode(this.getCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceptionTimestamp(this.getReceivedTimestamp());


        List<com.inov8.integration.webservice.optasiaVO.EligibilityStatus> eligibilityStatusList = new ArrayList<>();
        if (this.getEligibilityStatus() != null) {
            com.inov8.integration.webservice.optasiaVO.EligibilityStatus eligibilityStatus = new com.inov8.integration.webservice.optasiaVO.EligibilityStatus();
            eligibilityStatus.setEligibilityStatus(this.getEligibilityStatus().getEligibilityStatus());
            eligibilityStatus.setEligible(this.getEligibilityStatus().getIsEligible());
            eligibilityStatusList.add(eligibilityStatus);
        }
        collectionOfList.put("EligibilityStatus", eligibilityStatusList);
        i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);


//        if (eligibilityStatus != null) {
//            com.inov8.integration.webservice.optasiaVO.EligibilityStatus eligibilityStatus;
//            List<com.inov8.integration.webservice.optasiaVO.EligibilityStatus> eligibilityStatusList = new ArrayList<>();
//            for (int i = 0; i < this.getEligibilityStatus().size(); i++) {
//                eligibilityStatus = new com.inov8.integration.webservice.optasiaVO.EligibilityStatus();
//                eligibilityStatus.setEligible(this.getEligibilityStatus().get(i).getIsEligible());
//                eligibilityStatus.setEligibilityStatus(this.getEligibilityStatus().get(i).getEligibilityStatus());
//
//                eligibilityStatusList.add(eligibilityStatus);
//
//                collectionOfList.put("EligibilityStatus", eligibilityStatusList);
//                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
//            }
//        }


        if (loanOffersByProductGroup != null) {
            List<com.inov8.integration.webservice.optasiaVO.LoanOffersByLoanProductGroup> loanOffersByLoanProductGroupList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.MaturityDetails> maturityDetailsList = new ArrayList<>();
//            List<com.inov8.integration.webservice.optasiaVO.Interest> interestList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.OneOffCharges> oneOffChargesList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.RecurringCharges> recurringChargesList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.LoanOffers> loanOffersList = new ArrayList<>();

            com.inov8.integration.webservice.optasiaVO.LoanOffersByLoanProductGroup loanOffersByLoanProductGroup;
            com.inov8.integration.webservice.optasiaVO.MaturityDetails maturityDetails;
//            com.inov8.integration.webservice.optasiaVO.Interest interest;
            com.inov8.integration.webservice.optasiaVO.OneOffCharges oneOffCharges;
            com.inov8.integration.webservice.optasiaVO.RecurringCharges recurringCharges;
            com.inov8.integration.webservice.optasiaVO.LoanOffers loanOffers;

            for (int i = 0; i < this.getLoanOffersByProductGroup().size(); i++) {

                loanOffersByLoanProductGroup = new com.inov8.integration.webservice.optasiaVO.LoanOffersByLoanProductGroup();
                maturityDetails = new com.inov8.integration.webservice.optasiaVO.MaturityDetails();
//                interest = new com.inov8.integration.webservice.optasiaVO.Interest();
                oneOffCharges = new com.inov8.integration.webservice.optasiaVO.OneOffCharges();
                recurringCharges = new com.inov8.integration.webservice.optasiaVO.RecurringCharges();
                loanOffers = new com.inov8.integration.webservice.optasiaVO.LoanOffers();


                loanOffersByLoanProductGroup.setLoanProductGroup(this.getLoanOffersByProductGroup().get(i).getLoanProductGroup());
                loanOffers.setOfferClass(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getOfferClass());
                loanOffers.setOfferName(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getOfferName());
                loanOffers.setCommodityType(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getCommodityType());
                loanOffers.setCurrencyCode(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getCurrencyCode());
                loanOffers.setSetupFees(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getSetupFees()));
                loanOffers.setLoanProductGroup(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getLoanProductGroup());
                loanOffers.setLoanPlanId(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getLoanPlanId()));
                loanOffers.setLoanPlanName(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getLoanPlanName());
                maturityDetails.setMaturityDuration(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getMaturityDuration()));

//                interest.setInterestName(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestName());
//                interest.setInterestType(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestType());
//                interest.setInterestValue(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestValue()));
//                interest.setInterestVAT(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterestVAT()));
//                interest.setDaysOffset(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getDaysOffset()));
//                interest.setInterval(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getInterest().getInterval()));

                oneOffCharges.setChargeName(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeName());
                oneOffCharges.setChargeType(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeType());
                oneOffCharges.setChargeValue(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeValue()));
                oneOffCharges.setChargeVAT(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getChargeVAT()));
                oneOffCharges.setDaysOffset(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getOneOffCharges().get(i).getDaysOffset()));
                recurringCharges.setChargeName(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeName());
                recurringCharges.setChargeType(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeType());
                recurringCharges.setChargeValue(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeValue()));
                recurringCharges.setChargeVAT(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getChargeVAT()));
                recurringCharges.setInterval(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getMaturityDetails().getRecurringCharges().get(i).getInterval()));
                loanOffers.setPrincipalFrom(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getPrincipalFrom()));
                loanOffers.setPrincipalTo(String.valueOf(this.getLoanOffersByProductGroup().get(i).getLoanOffers().get(i).getPrincipalTo()));

//                interestList.add(interest);
                loanOffersList.add(loanOffers);
                maturityDetailsList.add(maturityDetails);
                oneOffChargesList.add(oneOffCharges);
                recurringChargesList.add(recurringCharges);
                loanOffersByLoanProductGroupList.add(loanOffersByLoanProductGroup);

//                collectionOfList.put("Interest", interestList);
//                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

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
                outstandingStatus.setAvailableCreditLimit(String.valueOf(this.getOutstandingStatus().get(i).getAvailableCreditLimit()));
                outstandingStatus.setDynamicCreditLimit(String.valueOf(this.getOutstandingStatus().get(i).getDynamicCreditLimit()));
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
        "eligibilityStatus",
        "isEligible"
})
class EligibilityStatus {

    @JsonProperty("eligibilityStatus")
    private String eligibilityStatus;
    @JsonProperty("isEligible")
    private Boolean isEligible;

    @JsonProperty("eligibilityStatus")
    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    @JsonProperty("eligibilityStatus")
    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    @JsonProperty("isEligible")
    public Boolean getIsEligible() {
        return isEligible;
    }

    @JsonProperty("isEligible")
    public void setIsEligible(Boolean isEligible) {
        this.isEligible = isEligible;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "offerClass",
        "offerName",
        "commodityType",
        "currencyCode",
        "setupFees",
        "loanProductGroup",
        "loanPlanId",
        "loanPlanName",
        "maturityDetails",
        "principalFrom",
        "principalTo"
})
class OfferLoanOffer {

    @JsonProperty("offerClass")
    private String offerClass;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("commodityType")
    private String commodityType;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("setupFees")
    private Float setupFees;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("loanPlanId")
    private Integer loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("maturityDetails")
    private OfferMaturityDetails maturityDetails;
    @JsonProperty("principalFrom")
    private Float principalFrom;
    @JsonProperty("principalTo")
    private Float principalTo;

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

    @JsonProperty("commodityType")
    public String getCommodityType() {
        return commodityType;
    }

    @JsonProperty("commodityType")
    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("setupFees")
    public Float getSetupFees() {
        return setupFees;
    }

    @JsonProperty("setupFees")
    public void setSetupFees(Float setupFees) {
        this.setupFees = setupFees;
    }

    @JsonProperty("loanProductGroup")
    public String getLoanProductGroup() {
        return loanProductGroup;
    }

    @JsonProperty("loanProductGroup")
    public void setLoanProductGroup(String loanProductGroup) {
        this.loanProductGroup = loanProductGroup;
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

    @JsonProperty("maturityDetails")
    public OfferMaturityDetails getMaturityDetails() {
        return maturityDetails;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(OfferMaturityDetails maturityDetails) {
        this.maturityDetails = maturityDetails;
    }

    @JsonProperty("principalFrom")
    public Float getPrincipalFrom() {
        return principalFrom;
    }

    @JsonProperty("principalFrom")
    public void setPrincipalFrom(Float principalFrom) {
        this.principalFrom = principalFrom;
    }

    @JsonProperty("principalTo")
    public Float getPrincipalTo() {
        return principalTo;
    }

    @JsonProperty("principalTo")
    public void setPrincipalTo(Float principalTo) {
        this.principalTo = principalTo;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanProductGroup",
        "loanOffers"
})
class LoanOffersByProductGroup {

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
        "oneOffCharges",
        "recurringCharges"
})
class OfferMaturityDetails {

    @JsonProperty("maturityDuration")
    private Integer maturityDuration;
    @JsonProperty("oneOffCharges")
    private List<OneOffCharge> oneOffCharges;
    @JsonProperty("recurringCharges")
    private List<RecurringCharge> recurringCharges;

    @JsonProperty("maturityDuration")
    public Integer getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(Integer maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    @JsonProperty("oneOffCharges")
    public List<OneOffCharge> getOneOffCharges() {
        return oneOffCharges;
    }

    @JsonProperty("oneOffCharges")
    public void setOneOffCharges(List<OneOffCharge> oneOffCharges) {
        this.oneOffCharges = oneOffCharges;
    }

    @JsonProperty("recurringCharges")
    public List<RecurringCharge> getRecurringCharges() {
        return recurringCharges;
    }

    @JsonProperty("recurringCharges")
    public void setRecurringCharges(List<RecurringCharge> recurringCharges) {
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
class OneOffCharge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeType")
    private String chargeType;
    @JsonProperty("chargeValue")
    private Object chargeValue;
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
    public Object getChargeValue() {
        return chargeValue;
    }

    @JsonProperty("chargeValue")
    public void setChargeValue(Object chargeValue) {
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
        "availableCreditLimit",
        "dynamicCreditLimit",
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
@Generated("jsonschema2pojo")
class Outstandingstatus {

    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("availableCreditLimit")
    private Float availableCreditLimit;
    @JsonProperty("dynamicCreditLimit")
    private Float dynamicCreditLimit;
    @JsonProperty("numOutstandingLoans")
    private Integer numOutstandingLoans;
    @JsonProperty("totalGross")
    private Float totalGross;
    @JsonProperty("totalPrincipal")
    private Float totalPrincipal;
    @JsonProperty("totalSetupFees")
    private Float totalSetupFees;
    @JsonProperty("totalInterest")
    private Float totalInterest;
    @JsonProperty("totalInterestVAT")
    private Float totalInterestVAT;
    @JsonProperty("totalCharges")
    private Float totalCharges;
    @JsonProperty("totalChargesVAT")
    private Float totalChargesVAT;
    @JsonProperty("totalPendingLoans")
    private Float totalPendingLoans;
    @JsonProperty("totalPendingRecoveries")
    private Float totalPendingRecoveries;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("availableCreditLimit")
    public Float getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    @JsonProperty("availableCreditLimit")
    public void setAvailableCreditLimit(Float availableCreditLimit) {
        this.availableCreditLimit = availableCreditLimit;
    }

    @JsonProperty("dynamicCreditLimit")
    public Float getDynamicCreditLimit() {
        return dynamicCreditLimit;
    }

    @JsonProperty("dynamicCreditLimit")
    public void setDynamicCreditLimit(Float dynamicCreditLimit) {
        this.dynamicCreditLimit = dynamicCreditLimit;
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
    public Float getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(Float totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalPrincipal")
    public Float getTotalPrincipal() {
        return totalPrincipal;
    }

    @JsonProperty("totalPrincipal")
    public void setTotalPrincipal(Float totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    @JsonProperty("totalSetupFees")
    public Float getTotalSetupFees() {
        return totalSetupFees;
    }

    @JsonProperty("totalSetupFees")
    public void setTotalSetupFees(Float totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
    }

    @JsonProperty("totalInterest")
    public Float getTotalInterest() {
        return totalInterest;
    }

    @JsonProperty("totalInterest")
    public void setTotalInterest(Float totalInterest) {
        this.totalInterest = totalInterest;
    }

    @JsonProperty("totalInterestVAT")
    public Float getTotalInterestVAT() {
        return totalInterestVAT;
    }

    @JsonProperty("totalInterestVAT")
    public void setTotalInterestVAT(Float totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    @JsonProperty("totalCharges")
    public Float getTotalCharges() {
        return totalCharges;
    }

    @JsonProperty("totalCharges")
    public void setTotalCharges(Float totalCharges) {
        this.totalCharges = totalCharges;
    }

    @JsonProperty("totalChargesVAT")
    public Float getTotalChargesVAT() {
        return totalChargesVAT;
    }

    @JsonProperty("totalChargesVAT")
    public void setTotalChargesVAT(Float totalChargesVAT) {
        this.totalChargesVAT = totalChargesVAT;
    }

    @JsonProperty("totalPendingLoans")
    public Float getTotalPendingLoans() {
        return totalPendingLoans;
    }

    @JsonProperty("totalPendingLoans")
    public void setTotalPendingLoans(Float totalPendingLoans) {
        this.totalPendingLoans = totalPendingLoans;
    }

    @JsonProperty("totalPendingRecoveries")
    public Float getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    @JsonProperty("totalPendingRecoveries")
    public void setTotalPendingRecoveries(Float totalPendingRecoveries) {
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
class RecurringCharge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeType")
    private String chargeType;
    @JsonProperty("chargeValue")
    private Float chargeValue;
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
    public Float getChargeValue() {
        return chargeValue;
    }

    @JsonProperty("chargeValue")
    public void setChargeValue(Float chargeValue) {
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