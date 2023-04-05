package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.optasiaVO.ChargeAdjustments;
import com.inov8.integration.webservice.optasiaVO.Milestones;
import com.inov8.integration.webservice.optasiaVO.OneOffCharges;
import com.inov8.integration.webservice.optasiaVO.TotalOneOffCharges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

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
        "loanOffer",
        "periodsProjections"
})
public class InitiateLoanResponse extends Response implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(InitiateLoanResponse.class.getSimpleName());


    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("loanOffer")
    private ProjectionLoanOffer loanOffer;
    @JsonProperty("periodsProjections")
    private List<PeriodsProjection> periodsProjections;
    private String responseCode;
    private String responseDescription;
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

    @JsonProperty("loanOffer")
    public ProjectionLoanOffer getLoanOffer() {
        return loanOffer;
    }

    @JsonProperty("loanOffer")
    public void setLoanOffer(ProjectionLoanOffer loanOffer) {
        this.loanOffer = loanOffer;
    }

    @JsonProperty("periodsProjections")
    public List<PeriodsProjection> getPeriodsProjections() {
        return periodsProjections;
    }

    @JsonProperty("periodsProjections")
    public void setPeriodsProjections(List<PeriodsProjection> periodsProjections) {
        this.periodsProjections = periodsProjections;
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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        List<com.inov8.integration.webservice.optasiaVO.OneOffCharges> oneOffChargesList = new ArrayList<>();
        List<com.inov8.integration.webservice.optasiaVO.RecurringCharges> recurringChargesList = new ArrayList<>();

        com.inov8.integration.webservice.optasiaVO.OneOffCharges oneOffCharges;
        com.inov8.integration.webservice.optasiaVO.RecurringCharges recurringCharges;

        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        } else {
            i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
            i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
        }
        i8SBSwitchControllerResponseVO.setCode(this.getCode());
        i8SBSwitchControllerResponseVO.setMessage(this.getMessage());
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());

        if (loanOffer != null) {

            i8SBSwitchControllerResponseVO.setOfferClass(this.getLoanOffer().getOfferClass());
            i8SBSwitchControllerResponseVO.setOfferName(this.getLoanOffer().getOfferName());
            i8SBSwitchControllerResponseVO.setAdvanceOfferId(this.getLoanOffer().getAdvanceOfferId());
            i8SBSwitchControllerResponseVO.setCommodityType(this.getLoanOffer().getCommodityType());
            i8SBSwitchControllerResponseVO.setCurrencyCode(this.getLoanOffer().getCurrencyCode());
            i8SBSwitchControllerResponseVO.setPrincipalAmount(String.valueOf(this.getLoanOffer().getPrincipalAmount()));
            i8SBSwitchControllerResponseVO.setSetUpFees(String.valueOf(this.getLoanOffer().getSetupFees()));
            i8SBSwitchControllerResponseVO.setLoanProductGroup(this.getLoanOffer().getLoanProductGroup());
            i8SBSwitchControllerResponseVO.setLoanPlanId(String.valueOf(this.getLoanOffer().getLoanPlanId()));
            i8SBSwitchControllerResponseVO.setLoanPlanName(this.getLoanOffer().getLoanPlanName());
            i8SBSwitchControllerResponseVO.setMaturityDuration(String.valueOf(this.getLoanOffer().getMaturityDetails().getMaturityDuration()));

            List<ProjectionOneOffCharge> projectionOneOffChargeList = this.getLoanOffer().getMaturityDetails().getOneOffCharges();
            if (projectionOneOffChargeList != null) {
                oneOffChargesList = new ArrayList<>();
                for (int i = 0; i < projectionOneOffChargeList.size(); i++) {
                    oneOffCharges = new com.inov8.integration.webservice.optasiaVO.OneOffCharges();
                    oneOffCharges.setChargeName(projectionOneOffChargeList.get(i).getChargeName());
                    oneOffCharges.setChargeType(projectionOneOffChargeList.get(i).getChargeType());
                    oneOffCharges.setChargeValue(String.valueOf(projectionOneOffChargeList.get(i).getChargeValue()));
                    oneOffCharges.setChargeVAT(String.valueOf(projectionOneOffChargeList.get(i).getChargeVAT()));
                    oneOffCharges.setDaysOffset(String.valueOf(projectionOneOffChargeList.get(i).getDaysOffset()));
                    oneOffChargesList.add(oneOffCharges);
                    collectionOfList.put("OneOffCharges", oneOffChargesList);
                    i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
                }
            }

            List<ProjectionRecurringCharge> projectionRecurringChargeList = this.getLoanOffer().getMaturityDetails().getRecurringCharges();
            if (projectionRecurringChargeList != null) {
                recurringChargesList = new ArrayList<>();
                for (int j = 0; j < projectionRecurringChargeList.size(); j++) {
                    recurringCharges = new com.inov8.integration.webservice.optasiaVO.RecurringCharges();

                    recurringCharges.setChargeName(projectionRecurringChargeList.get(j).getChargeName());
                    recurringCharges.setChargeType(projectionRecurringChargeList.get(j).getChargeType());
                    recurringCharges.setChargeValue(String.valueOf(projectionRecurringChargeList.get(j).getChargeValue()));
                    recurringCharges.setChargeVAT(String.valueOf(projectionRecurringChargeList.get(j).getChargeVAT()));
                    recurringCharges.setInterval(String.valueOf(projectionRecurringChargeList.get(j).getInterval()));
                    recurringChargesList.add(recurringCharges);

                    collectionOfList.put("RecurringCharges", recurringChargesList);
                    i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
                }

            }

        }

//        i8SBSwitchControllerResponseVO.setInterest(this.getLoanOffer().getMaturityDetails().getInterest().getInterestName());
//        i8SBSwitchControllerResponseVO.setInterestType(this.getLoanOffer().getMaturityDetails().getInterest().getInterestType());
//        i8SBSwitchControllerResponseVO.setInterestValue(this.getLoanOffer().getMaturityDetails().getInterest().getInterestValue());
//        i8SBSwitchControllerResponseVO.setInterestVAT(this.getLoanOffer().getMaturityDetails().getInterest().getInterestVAT());
//        i8SBSwitchControllerResponseVO.setDaysOffset(this.getLoanOffer().getMaturityDetails().getInterest().getDaysOffset());
//        i8SBSwitchControllerResponseVO.setInterval(this.getLoanOffer().getMaturityDetails().getInterest().getInterval());


//        i8SBSwitchControllerResponseVO.setChargeName(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeName());
//        i8SBSwitchControllerResponseVO.setChargeType(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeType());
//        i8SBSwitchControllerResponseVO.setChargeValue(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeValue());
//        i8SBSwitchControllerResponseVO.setChargeVAT(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeVAT());
//        i8SBSwitchControllerResponseVO.setDaysOffset(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getDaysOffset());

        if (periodsProjections != null) {
            List<com.inov8.integration.webservice.optasiaVO.PeriodsProjection> periodsProjectionList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.TotalOneOffCharges> totalOneOffChargesList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.TotalRecurringCharge> totalRecurringChargeList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Milestones> milestonesList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.InterestAdjustment> interestAdjustmentList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.ChargeAdjustments> chargeAdjustmentList = new ArrayList<>();

            int i, j, k, l;
            for (i = 0; i < periodsProjections.size(); i++) {

                com.inov8.integration.webservice.optasiaVO.PeriodsProjection periodsProjection = new com.inov8.integration.webservice.optasiaVO.PeriodsProjection();
                com.inov8.integration.webservice.optasiaVO.TotalOneOffCharges totalOneOffCharges;
                com.inov8.integration.webservice.optasiaVO.TotalRecurringCharge totalRecurringCharge;
                com.inov8.integration.webservice.optasiaVO.Milestones milestones;
                com.inov8.integration.webservice.optasiaVO.InterestAdjustment interestAdjustment;
                com.inov8.integration.webservice.optasiaVO.ChargeAdjustments chargeAdjustment;

                periodsProjection.setPeriodIndex(String.valueOf(periodsProjections.get(i).getPeriodIndex()));
                periodsProjection.setPeriodType(periodsProjections.get(i).getPeriodType());
                periodsProjection.setPeriodStartTimemp(periodsProjections.get(i).getPeriodStartTimestamp());
                periodsProjection.setPeriodEndTimestamp(periodsProjections.get(i).getPeriodExpirationTimestamp());
                periodsProjection.setPeriodStartDayOfLoanIndex(String.valueOf(periodsProjections.get(i).getPeriodStartDayOfLoanIndex()));
                periodsProjection.setPeriodEndDayOfLoanIndex(String.valueOf(periodsProjections.get(i).getPeriodEndDayOfLoanIndex()));
                periodsProjection.setPrincipal(String.valueOf(periodsProjections.get(i).getPrincipal()));
                periodsProjection.setTotalExpenses(String.valueOf(periodsProjections.get(i).getTotalExpenses()));
                periodsProjection.setTotalGross(String.valueOf(periodsProjections.get(i).getTotalGross()));
                periodsProjection.setTotalInterest(String.valueOf(periodsProjections.get(i).getTotalInterest()));
                periodsProjection.setTotalInterestVAT(String.valueOf(periodsProjections.get(i).getTotalInterestVAT()));
                periodsProjection.setTotalCharges(String.valueOf(periodsProjections.get(i).getTotalCharges()));
                periodsProjection.setTotalChargesVAT(String.valueOf(periodsProjections.get(i).getTotalChargesVAT()));

                List<TotalOneOffCharge> totalOneOffChargesList1 = periodsProjections.get(i).getTotalOneOffCharges();
                if (totalOneOffChargesList1 != null) {
                    totalOneOffChargesList = new ArrayList<>();
                    for (j = 0; j < totalOneOffChargesList1.size(); j++) {
                        totalOneOffCharges = new com.inov8.integration.webservice.optasiaVO.TotalOneOffCharges();
                        totalOneOffCharges.setChargeName(totalOneOffChargesList1.get(j).getChargeName());
                        totalOneOffCharges.setCharge(String.valueOf(totalOneOffChargesList1.get(j).getCharge()));
                        totalOneOffCharges.setChargeVAT(String.valueOf(totalOneOffChargesList1.get(j).getChargeVAT()));
                        totalOneOffChargesList.add(totalOneOffCharges);
                        periodsProjection.setTotalOneOffChargesList(totalOneOffChargesList);

                    }
//                    periodsProjection.setTotalOneOffChargesList(totalOneOffChargesList);
                }

                List<TotalRecurringCharge> totalRecurringChargesList1 = periodsProjections.get(i).getTotalRecurringCharges();
                if (totalOneOffChargesList1 != null) {
                    totalRecurringChargeList = new ArrayList<>();
                    for (j = 0; j < totalRecurringChargesList1.size(); j++) {
                        totalRecurringCharge = new com.inov8.integration.webservice.optasiaVO.TotalRecurringCharge();
                        totalRecurringCharge.setChargeName(totalRecurringChargesList1.get(j).getChargeName());
                        totalRecurringCharge.setCharge(totalRecurringChargesList1.get(j).getCharge());
                        totalRecurringCharge.setChargeVAT(totalRecurringChargesList1.get(j).getChargeVAT());
                        totalRecurringChargeList.add(totalRecurringCharge);
                        periodsProjection.setTotalRecurringCharges(totalRecurringChargeList);

                    }
//                    periodsProjection.setTotalRecurringCharges(totalRecurringChargeList);
                }

                List<Milestone> milestonesList1 = periodsProjections.get(i).getMilestones();
                if (milestonesList1 != null) {
                    milestonesList = new ArrayList<>();
                    for (l = 0; l < milestonesList1.size(); l++) {
                        milestones = new com.inov8.integration.webservice.optasiaVO.Milestones();
                        milestones.setDate(milestonesList1.get(l).getDate());
                        milestones.setDayOfLoan(String.valueOf(milestonesList1.get(l).getDayOfLoanIndex()));

                        List<ChargeAdjustment> chargeAdjustmentList1 = milestonesList1.get(l).getChargeAdjustments();
                        if (chargeAdjustmentList1 != null) {
                            chargeAdjustmentList = new ArrayList<>();
                            for (k = 0; k < chargeAdjustmentList1.size(); k++) {
                                chargeAdjustment = new com.inov8.integration.webservice.optasiaVO.ChargeAdjustments();
//                                List<?> milestonesList2 = periodsProjections.get(k).getMilestones();
//                                if (!milestonesList2.isEmpty()) {
//                                List<?> chargeAdjustmentsList1 = periodsProjections.get(k).getMilestones().get(k).getChargeAdjustments();
                                if (!(chargeAdjustmentList1.isEmpty())) {
                                    chargeAdjustment.setName(chargeAdjustmentList1.get(k).getName());
                                    chargeAdjustment.setGross(String.valueOf(chargeAdjustmentList1.get(k).getGross()));
                                    chargeAdjustment.setNet(String.valueOf(chargeAdjustmentList1.get(k).getNet()));
                                    chargeAdjustment.setVat(String.valueOf(chargeAdjustmentList1.get(k).getVat()));
                                    chargeAdjustmentList.add(chargeAdjustment);
                                    milestones.setChargeAdjustmentsList(chargeAdjustmentList);

                                }
                            }
//                            milestones.setChargeAdjustmentsList(chargeAdjustmentList);
                        }


                        interestAdjustmentList = new ArrayList<>();
                        if (milestonesList1.get(l).getInterestAdjustment() != null) {
                            interestAdjustment = new com.inov8.integration.webservice.optasiaVO.InterestAdjustment();
                            interestAdjustment.setGross(String.valueOf(milestonesList1.get(l).getInterestAdjustment().getGross()));
                            interestAdjustment.setNet(String.valueOf(milestonesList1.get(l).getInterestAdjustment().getNet()));
                            interestAdjustment.setVat(String.valueOf(milestonesList1.get(l).getInterestAdjustment().getVat()));
                            interestAdjustmentList.add(interestAdjustment);
                            milestones.setInterestAdjustmentList(interestAdjustmentList);
                        }
                        milestones.setPrincipal(String.valueOf(milestonesList1.get(l).getPrincipal()));
                        milestones.setTotalExpenses(String.valueOf(milestonesList1.get(l).getTotalExpenses()));
                        milestones.setTotalGross(String.valueOf(milestonesList1.get(l).getTotalGross()));
                        milestones.setTotalInterest(String.valueOf(milestonesList1.get(l).getTotalInterest()));
                        milestones.setTotalInterestVAT(String.valueOf(milestonesList1.get(l).getTotalInterestVAT()));
                        milestones.setTotalCharges(String.valueOf(milestonesList1.get(l).getTotalCharges()));
                        milestones.setTotalChargesVAT(String.valueOf(milestonesList1.get(l).getTotalChargesVAT()));
//                        milestones.setInterestAdjustmentList(interestAdjustmentList);
//                        milestones.setChargeAdjustmentsList(chargeAdjustmentList);
                        milestonesList.add(milestones);

                        periodsProjection.setMilestonesList(milestonesList);

                        collectionOfList.put("ChargeAdjustments", chargeAdjustmentList);
                        i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                        collectionOfList.put("InterestAdjustment", interestAdjustmentList);
                        i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

//                        collectionOfList.put("Milestones", milestonesList);
//                        i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                    }
                    collectionOfList.put("Milestones", milestonesList);
                    i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
//                    periodsProjection.setMilestonesList(milestonesList);
                }
//                }


//                milestones.setInterestAdjustmentList(interestAdjustmentList);
//                milestones.setChargeAdjustmentsList(chargeAdjustmentList);

                periodsProjectionList.add(periodsProjection);


                collectionOfList.put("TotalOneOffCharges", totalOneOffChargesList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("TotalRecurringCharges", totalRecurringChargeList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Projections", periodsProjectionList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);
            }

        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "gross",
        "net",
        "vat",
        "name"
})
class ChargeAdjustment {

    @JsonProperty("gross")
    private Float gross;
    @JsonProperty("net")
    private Float net;
    @JsonProperty("vat")
    private Float vat;
    @JsonProperty("name")
    private String name;

    @JsonProperty("gross")
    public Float getGross() {
        return gross;
    }

    @JsonProperty("gross")
    public void setGross(Float gross) {
        this.gross = gross;
    }

    @JsonProperty("net")
    public Float getNet() {
        return net;
    }

    @JsonProperty("net")
    public void setNet(Float net) {
        this.net = net;
    }

    @JsonProperty("vat")
    public Float getVat() {
        return vat;
    }

    @JsonProperty("vat")
    public void setVat(Float vat) {
        this.vat = vat;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "gross",
        "net",
        "vat"
})
class InterestAdjustment {

    @JsonProperty("gross")
    private Float gross;
    @JsonProperty("net")
    private Float net;
    @JsonProperty("vat")
    private Float vat;

    @JsonProperty("gross")
    public Float getGross() {
        return gross;
    }

    @JsonProperty("gross")
    public void setGross(Float gross) {
        this.gross = gross;
    }

    @JsonProperty("net")
    public Float getNet() {
        return net;
    }

    @JsonProperty("net")
    public void setNet(Float net) {
        this.net = net;
    }

    @JsonProperty("vat")
    public Float getVat() {
        return vat;
    }

    @JsonProperty("vat")
    public void setVat(Float vat) {
        this.vat = vat;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "offerClass",
        "offerName",
        "advanceOfferId",
        "commodityType",
        "currencyCode",
        "principalAmount",
        "setupFees",
        "loanProductGroup",
        "loanPlanId",
        "loanPlanName",
        "maturityDetails"
})
class ProjectionLoanOffer {

    @JsonProperty("offerClass")
    private String offerClass;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("advanceOfferId")
    private String advanceOfferId;
    @JsonProperty("commodityType")
    private String commodityType;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("principalAmount")
    private Float principalAmount;
    @JsonProperty("setupFees")
    private Float setupFees;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("loanPlanId")
    private Integer loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("maturityDetails")
    private MaturityDetails maturityDetails;

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

    @JsonProperty("advanceOfferId")
    public String getAdvanceOfferId() {
        return advanceOfferId;
    }

    @JsonProperty("advanceOfferId")
    public void setAdvanceOfferId(String advanceOfferId) {
        this.advanceOfferId = advanceOfferId;
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

    @JsonProperty("principalAmount")
    public Float getPrincipalAmount() {
        return principalAmount;
    }

    @JsonProperty("principalAmount")
    public void setPrincipalAmount(Float principalAmount) {
        this.principalAmount = principalAmount;
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
    public MaturityDetails getMaturityDetails() {
        return maturityDetails;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(MaturityDetails maturityDetails) {
        this.maturityDetails = maturityDetails;
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration",
        "oneOffCharges",
        "recurringCharges"
})
class MaturityDetails {

    @JsonProperty("maturityDuration")
    private Integer maturityDuration;
    @JsonProperty("oneOffCharges")
    private List<ProjectionOneOffCharge> oneOffCharges;
    @JsonProperty("recurringCharges")
    private List<ProjectionRecurringCharge> recurringCharges;

    @JsonProperty("maturityDuration")
    public Integer getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(Integer maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    @JsonProperty("oneOffCharges")
    public List<ProjectionOneOffCharge> getOneOffCharges() {
        return oneOffCharges;
    }

    @JsonProperty("oneOffCharges")
    public void setOneOffCharges(List<ProjectionOneOffCharge> oneOffCharges) {
        this.oneOffCharges = oneOffCharges;
    }

    @JsonProperty("recurringCharges")
    public List<ProjectionRecurringCharge> getRecurringCharges() {
        return recurringCharges;
    }

    @JsonProperty("recurringCharges")
    public void setRecurringCharges(List<ProjectionRecurringCharge> recurringCharges) {
        this.recurringCharges = recurringCharges;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "dayOfLoanIndex",
        "chargeAdjustments",
        "interestAdjustment",
        "principal",
        "totalGross",
        "totalExpenses",
        "totalSetupFees",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT"
})
class Milestone {

    @JsonProperty("date")
    private String date;
    @JsonProperty("dayOfLoanIndex")
    private Integer dayOfLoanIndex;
    @JsonProperty("chargeAdjustments")
    private List<ChargeAdjustment> chargeAdjustments;
    @JsonProperty("interestAdjustment")
    private InterestAdjustment interestAdjustment;
    @JsonProperty("principal")
    private Float principal;
    @JsonProperty("totalGross")
    private Float totalGross;
    @JsonProperty("totalExpenses")
    private Float totalExpenses;
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

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("dayOfLoanIndex")
    public Integer getDayOfLoanIndex() {
        return dayOfLoanIndex;
    }

    @JsonProperty("dayOfLoanIndex")
    public void setDayOfLoanIndex(Integer dayOfLoanIndex) {
        this.dayOfLoanIndex = dayOfLoanIndex;
    }

    @JsonProperty("chargeAdjustments")
    public List<ChargeAdjustment> getChargeAdjustments() {
        return chargeAdjustments;
    }

    @JsonProperty("chargeAdjustments")
    public void setChargeAdjustments(List<ChargeAdjustment> chargeAdjustments) {
        this.chargeAdjustments = chargeAdjustments;
    }

    @JsonProperty("interestAdjustment")
    public InterestAdjustment getInterestAdjustment() {
        return interestAdjustment;
    }

    @JsonProperty("interestAdjustment")
    public void setInterestAdjustment(InterestAdjustment interestAdjustment) {
        this.interestAdjustment = interestAdjustment;
    }

    @JsonProperty("principal")
    public Float getPrincipal() {
        return principal;
    }

    @JsonProperty("principal")
    public void setPrincipal(Float principal) {
        this.principal = principal;
    }

    @JsonProperty("totalGross")
    public Float getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(Float totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalExpenses")
    public Float getTotalExpenses() {
        return totalExpenses;
    }

    @JsonProperty("totalExpenses")
    public void setTotalExpenses(Float totalExpenses) {
        this.totalExpenses = totalExpenses;
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

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "chargeType",
        "chargeValue",
        "chargeVAT",
        "daysOffset"
})
class ProjectionOneOffCharge {

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
        "periodIndex",
        "periodType",
        "periodStartTimestamp",
        "periodExpirationTimestamp",
        "periodStartDayOfLoanIndex",
        "periodEndDayOfLoanIndex",
        "currencyCode",
        "totalGross",
        "principal",
        "totalExpenses",
        "totalSetupFees",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "totalOneOffCharges",
        "totalRecurringCharges",
        "milestones"
})
class PeriodsProjection {

    @JsonProperty("periodIndex")
    private Integer periodIndex;
    @JsonProperty("periodType")
    private String periodType;
    @JsonProperty("periodStartTimestamp")
    private String periodStartTimestamp;
    @JsonProperty("periodExpirationTimestamp")
    private String periodExpirationTimestamp;
    @JsonProperty("periodStartDayOfLoanIndex")
    private Integer periodStartDayOfLoanIndex;
    @JsonProperty("periodEndDayOfLoanIndex")
    private Integer periodEndDayOfLoanIndex;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("totalGross")
    private Float totalGross;
    @JsonProperty("principal")
    private Float principal;
    @JsonProperty("totalExpenses")
    private Float totalExpenses;
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
    @JsonProperty("totalOneOffCharges")
    private List<TotalOneOffCharge> totalOneOffCharges;
    @JsonProperty("totalRecurringCharges")
    private List<TotalRecurringCharge> totalRecurringCharges;
    @JsonProperty("milestones")
    private List<Milestone> milestones;

    @JsonProperty("periodIndex")
    public Integer getPeriodIndex() {
        return periodIndex;
    }

    @JsonProperty("periodIndex")
    public void setPeriodIndex(Integer periodIndex) {
        this.periodIndex = periodIndex;
    }

    @JsonProperty("periodType")
    public String getPeriodType() {
        return periodType;
    }

    @JsonProperty("periodType")
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    @JsonProperty("periodStartTimestamp")
    public String getPeriodStartTimestamp() {
        return periodStartTimestamp;
    }

    @JsonProperty("periodStartTimestamp")
    public void setPeriodStartTimestamp(String periodStartTimestamp) {
        this.periodStartTimestamp = periodStartTimestamp;
    }

    @JsonProperty("periodExpirationTimestamp")
    public String getPeriodExpirationTimestamp() {
        return periodExpirationTimestamp;
    }

    @JsonProperty("periodExpirationTimestamp")
    public void setPeriodExpirationTimestamp(String periodExpirationTimestamp) {
        this.periodExpirationTimestamp = periodExpirationTimestamp;
    }

    @JsonProperty("periodStartDayOfLoanIndex")
    public Integer getPeriodStartDayOfLoanIndex() {
        return periodStartDayOfLoanIndex;
    }

    @JsonProperty("periodStartDayOfLoanIndex")
    public void setPeriodStartDayOfLoanIndex(Integer periodStartDayOfLoanIndex) {
        this.periodStartDayOfLoanIndex = periodStartDayOfLoanIndex;
    }

    @JsonProperty("periodEndDayOfLoanIndex")
    public Integer getPeriodEndDayOfLoanIndex() {
        return periodEndDayOfLoanIndex;
    }

    @JsonProperty("periodEndDayOfLoanIndex")
    public void setPeriodEndDayOfLoanIndex(Integer periodEndDayOfLoanIndex) {
        this.periodEndDayOfLoanIndex = periodEndDayOfLoanIndex;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("totalGross")
    public Float getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(Float totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("principal")
    public Float getPrincipal() {
        return principal;
    }

    @JsonProperty("principal")
    public void setPrincipal(Float principal) {
        this.principal = principal;
    }

    @JsonProperty("totalExpenses")
    public Float getTotalExpenses() {
        return totalExpenses;
    }

    @JsonProperty("totalExpenses")
    public void setTotalExpenses(Float totalExpenses) {
        this.totalExpenses = totalExpenses;
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

    @JsonProperty("totalOneOffCharges")
    public List<TotalOneOffCharge> getTotalOneOffCharges() {
        return totalOneOffCharges;
    }

    @JsonProperty("totalOneOffCharges")
    public void setTotalOneOffCharges(List<TotalOneOffCharge> totalOneOffCharges) {
        this.totalOneOffCharges = totalOneOffCharges;
    }

    @JsonProperty("totalRecurringCharges")
    public List<TotalRecurringCharge> getTotalRecurringCharges() {
        return totalRecurringCharges;
    }

    @JsonProperty("totalRecurringCharges")
    public void setTotalRecurringCharges(List<TotalRecurringCharge> totalRecurringCharges) {
        this.totalRecurringCharges = totalRecurringCharges;
    }

    @JsonProperty("milestones")
    public List<Milestone> getMilestones() {
        return milestones;
    }

    @JsonProperty("milestones")
    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
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
class ProjectionRecurringCharge {

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "charge",
        "chargeVAT"
})
class TotalOneOffCharge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("charge")
    private Float charge;
    @JsonProperty("chargeVAT")
    private Float chargeVAT;

    @JsonProperty("chargeName")
    public String getChargeName() {
        return chargeName;
    }

    @JsonProperty("chargeName")
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @JsonProperty("charge")
    public Float getCharge() {
        return charge;
    }

    @JsonProperty("charge")
    public void setCharge(Float charge) {
        this.charge = charge;
    }

    @JsonProperty("chargeVAT")
    public Float getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(Float chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "charge",
        "chargeVAT"
})
class TotalRecurringCharge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("charge")
    private Float charge;
    @JsonProperty("chargeVAT")
    private Float chargeVAT;

    @JsonProperty("chargeName")
    public String getChargeName() {
        return chargeName;
    }

    @JsonProperty("chargeName")
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @JsonProperty("charge")
    public Float getCharge() {
        return charge;
    }

    @JsonProperty("charge")
    public void setCharge(Float charge) {
        this.charge = charge;
    }

    @JsonProperty("chargeVAT")
    public Float getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(Float chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

}