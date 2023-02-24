package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identityValue",
        "identityType",
        "origSource",
        "receivedTimestamp",
        "loanOffer",
        "periodsProjections"
})
public class InitiateLoanResponse extends Response {

    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("loanOffer")
    private ProjectionLoanOffer projectionLoanOffer;
    @JsonProperty("periodsProjections")
    private List<PeriodsProjection> periodsProjections;
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

    @JsonProperty("loanOffer")
    public ProjectionLoanOffer getLoanOffer() {
        return projectionLoanOffer;
    }

    @JsonProperty("loanOffer")
    public void setLoanOffer(ProjectionLoanOffer projectionLoanOffer) {
        this.projectionLoanOffer = projectionLoanOffer;
    }

    @JsonProperty("periodsProjections")
    public List<PeriodsProjection> getPeriodsProjections() {
        return periodsProjections;
    }

    @JsonProperty("periodsProjections")
    public void setPeriodsProjections(List<PeriodsProjection> periodsProjections) {
        this.periodsProjections = periodsProjections;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        i8SBSwitchControllerResponseVO.setResponseCode("00");
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());
        i8SBSwitchControllerResponseVO.setOfferClass(this.getLoanOffer().getOfferClass());
        i8SBSwitchControllerResponseVO.setOfferName(this.getLoanOffer().getOfferName());
        i8SBSwitchControllerResponseVO.setCurrencyCode(this.getLoanOffer().getCurrencyCode());
        i8SBSwitchControllerResponseVO.setPrincipalFrom(this.getLoanOffer().getPrincipalFrom());
        i8SBSwitchControllerResponseVO.setPrincipalTo(this.getLoanOffer().getPrincipalTo());
        i8SBSwitchControllerResponseVO.setSetUpFees(this.getLoanOffer().getSetupFees());
        i8SBSwitchControllerResponseVO.setCommodityType(this.getLoanOffer().getCommodityType());
        i8SBSwitchControllerResponseVO.setLoanPlanId(this.getLoanOffer().getLoanPlanId());
        i8SBSwitchControllerResponseVO.setLoanPlanName(this.getLoanOffer().getLoanPlanName());
        i8SBSwitchControllerResponseVO.setLoanProductGroup(this.getLoanOffer().getLoanProductGroup());
        i8SBSwitchControllerResponseVO.setMaturityDuration(this.getLoanOffer().getMaturityDetails().getMaturityDuration());
        i8SBSwitchControllerResponseVO.setInterest(this.getLoanOffer().getMaturityDetails().getInterest().getInterestName());
        i8SBSwitchControllerResponseVO.setInterestType(this.getLoanOffer().getMaturityDetails().getInterest().getInterestType());
        i8SBSwitchControllerResponseVO.setInterestValue(this.getLoanOffer().getMaturityDetails().getInterest().getInterestValue());
        i8SBSwitchControllerResponseVO.setInterestVAT(this.getLoanOffer().getMaturityDetails().getInterest().getInterestVAT());
        i8SBSwitchControllerResponseVO.setDaysOffset(this.getLoanOffer().getMaturityDetails().getInterest().getDaysOffset());
        i8SBSwitchControllerResponseVO.setInterval(this.getLoanOffer().getMaturityDetails().getInterest().getInterval());
        i8SBSwitchControllerResponseVO.setChargeName(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeName());
        i8SBSwitchControllerResponseVO.setChargeType(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeType());
        i8SBSwitchControllerResponseVO.setChargeValue(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeValue());
        i8SBSwitchControllerResponseVO.setChargeVAT(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getChargeVAT());
        i8SBSwitchControllerResponseVO.setDaysOffset(this.getLoanOffer().getMaturityDetails().getOneOffCharges().getDaysOffset());
        i8SBSwitchControllerResponseVO.setPeriodIndex(this.getPeriodsProjections().get(0).getPeriodIndex());
        i8SBSwitchControllerResponseVO.setPeriodType(this.getPeriodsProjections().get(0).getPeriodType());
        i8SBSwitchControllerResponseVO.setPeriodStartTimemp(this.getPeriodsProjections().get(0).getPeriodStartTimemp());
        i8SBSwitchControllerResponseVO.setPeriodEndTimestamp(this.getPeriodsProjections().get(0).getPeriodEndTimestamp());
        i8SBSwitchControllerResponseVO.setPeriodStartDayOfLoanIndex(this.getPeriodsProjections().get(0).getPeriodStartDayOfLoanIndex());
        i8SBSwitchControllerResponseVO.setPeriodEndDayOfLoanIndex(this.getPeriodsProjections().get(0).getPeriodEndDayOfLoanIndex());
        i8SBSwitchControllerResponseVO.setPrincipal(this.getPeriodsProjections().get(0).getPrincipal());
        i8SBSwitchControllerResponseVO.setTotalExpenses(this.getPeriodsProjections().get(0).getTotalExpenses());
        i8SBSwitchControllerResponseVO.setTotalGross(this.getPeriodsProjections().get(0).getTotalGross());
        i8SBSwitchControllerResponseVO.setTotalInterest(this.getPeriodsProjections().get(0).getTotalInterest());
        i8SBSwitchControllerResponseVO.setTotalInterestVAT(this.getPeriodsProjections().get(0).getTotalInterestVAT());
        i8SBSwitchControllerResponseVO.setTotalCharges(this.getPeriodsProjections().get(0).getTotalCharges());
        i8SBSwitchControllerResponseVO.setTotalChargesVAT(this.getPeriodsProjections().get(0).getTotalChargesVAT());
        i8SBSwitchControllerResponseVO.setChargeName(this.getPeriodsProjections().get(0).getTotalOneOffCharges().get(0).getChargeName());
        i8SBSwitchControllerResponseVO.setChargeAmount(this.getPeriodsProjections().get(0).getTotalOneOffCharges().get(0).getChargeAmount());
        i8SBSwitchControllerResponseVO.setChargesVAT(this.getPeriodsProjections().get(0).getTotalOneOffCharges().get(0).getChargeVAT());
        i8SBSwitchControllerResponseVO.setDayOfLoan(this.getPeriodsProjections().get(0).getMilestones().get(0).getDayOfLoan());
        i8SBSwitchControllerResponseVO.setDate(this.getPeriodsProjections().get(0).getMilestones().get(0).getDate());
        i8SBSwitchControllerResponseVO.setGross(this.getPeriodsProjections().get(0).getMilestones().get(0).getInterestAdjustment().getGross());
        i8SBSwitchControllerResponseVO.setNet(this.getPeriodsProjections().get(0).getMilestones().get(0).getInterestAdjustment().getNet());
        i8SBSwitchControllerResponseVO.setVat(this.getPeriodsProjections().get(0).getMilestones().get(0).getInterestAdjustment().getVat());
        i8SBSwitchControllerResponseVO.setName(this.getPeriodsProjections().get(0).getMilestones().get(0).getChargeAdjustments().get(0).getName());
        i8SBSwitchControllerResponseVO.setGross(this.getPeriodsProjections().get(0).getMilestones().get(0).getChargeAdjustments().get(0).getGross());
        i8SBSwitchControllerResponseVO.setNet(this.getPeriodsProjections().get(0).getMilestones().get(0).getChargeAdjustments().get(0).getNet());
        i8SBSwitchControllerResponseVO.setVat(this.getPeriodsProjections().get(0).getMilestones().get(0).getChargeAdjustments().get(0).getVat());


        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "gross",
        "net",
        "vat"
})
class ChargeAdjustment {

    @JsonProperty("name")
    private String name;
    @JsonProperty("gross")
    private String gross;
    @JsonProperty("net")
    private String net;
    @JsonProperty("vat")
    private String vat;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("gross")
    public String getGross() {
        return gross;
    }

    @JsonProperty("gross")
    public void setGross(String gross) {
        this.gross = gross;
    }

    @JsonProperty("net")
    public String getNet() {
        return net;
    }

    @JsonProperty("net")
    public void setNet(String net) {
        this.net = net;
    }

    @JsonProperty("vat")
    public String getVat() {
        return vat;
    }

    @JsonProperty("vat")
    public void setVat(String vat) {
        this.vat = vat;
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
class Interest {

    @JsonProperty("interestName")
    private String interestName;
    @JsonProperty("interestType")
    private String interestType;
    @JsonProperty("interestValue")
    private String interestValue;
    @JsonProperty("interestVAT")
    private String interestVAT;
    @JsonProperty("daysOffset")
    private String daysOffset;
    @JsonProperty("interval")
    private String interval;

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
    public String getInterestValue() {
        return interestValue;
    }

    @JsonProperty("interestValue")
    public void setInterestValue(String interestValue) {
        this.interestValue = interestValue;
    }

    @JsonProperty("interestVAT")
    public String getInterestVAT() {
        return interestVAT;
    }

    @JsonProperty("interestVAT")
    public void setInterestVAT(String interestVAT) {
        this.interestVAT = interestVAT;
    }

    @JsonProperty("daysOffset")
    public String getDaysOffset() {
        return daysOffset;
    }

    @JsonProperty("daysOffset")
    public void setDaysOffset(String daysOffset) {
        this.daysOffset = daysOffset;
    }

    @JsonProperty("interval")
    public String getInterval() {
        return interval;
    }

    @JsonProperty("interval")
    public void setInterval(String interval) {
        this.interval = interval;
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
    private String gross;
    @JsonProperty("net")
    private String net;
    @JsonProperty("vat")
    private String vat;

    @JsonProperty("gross")
    public String getGross() {
        return gross;
    }

    @JsonProperty("gross")
    public void setGross(String gross) {
        this.gross = gross;
    }

    @JsonProperty("net")
    public String getNet() {
        return net;
    }

    @JsonProperty("net")
    public void setNet(String net) {
        this.net = net;
    }

    @JsonProperty("vat")
    public String getVat() {
        return vat;
    }

    @JsonProperty("vat")
    public void setVat(String vat) {
        this.vat = vat;
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
class ProjectionLoanOffer {

    @JsonProperty("offerClass")
    private String offerClass;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("principalFrom")
    private String principalFrom;
    @JsonProperty("principalTo")
    private String principalTo;
    @JsonProperty("setupFees")
    private String setupFees;
    @JsonProperty("commodityType")
    private String commodityType;
    @JsonProperty("loanPlanId")
    private String loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
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

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("principalFrom")
    public String getPrincipalFrom() {
        return principalFrom;
    }

    @JsonProperty("principalFrom")
    public void setPrincipalFrom(String principalFrom) {
        this.principalFrom = principalFrom;
    }

    @JsonProperty("principalTo")
    public String getPrincipalTo() {
        return principalTo;
    }

    @JsonProperty("principalTo")
    public void setPrincipalTo(String principalTo) {
        this.principalTo = principalTo;
    }

    @JsonProperty("setupFees")
    public String getSetupFees() {
        return setupFees;
    }

    @JsonProperty("setupFees")
    public void setSetupFees(String setupFees) {
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
    public String getLoanPlanId() {
        return loanPlanId;
    }

    @JsonProperty("loanPlanId")
    public void setLoanPlanId(String loanPlanId) {
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
        "interest",
        "oneOffCharges"
})
class MaturityDetails {

    @JsonProperty("maturityDuration")
    private String maturityDuration;
    @JsonProperty("interest")
    private Interest interest;
    @JsonProperty("oneOffCharges")
    private OneOffCharges oneOffCharges;

    @JsonProperty("maturityDuration")
    public String getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(String maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    @JsonProperty("interest")
    public Interest getInterest() {
        return interest;
    }

    @JsonProperty("interest")
    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    @JsonProperty("oneOffCharges")
    public OneOffCharges getOneOffCharges() {
        return oneOffCharges;
    }

    @JsonProperty("oneOffCharges")
    public void setOneOffCharges(OneOffCharges oneOffCharges) {
        this.oneOffCharges = oneOffCharges;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dayOfLoan",
        "date",
        "interestAdjustment",
        "chargeAdjustments",
        "principal",
        "totalExpenses",
        "totalGross",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT"
})
class Milestone {

    @JsonProperty("dayOfLoan")
    private String dayOfLoan;
    @JsonProperty("date")
    private String date;
    @JsonProperty("interestAdjustment")
    private InterestAdjustment interestAdjustment;
    @JsonProperty("chargeAdjustments")
    private List<ChargeAdjustment> chargeAdjustments;
    @JsonProperty("principal")
    private String principal;
    @JsonProperty("totalExpenses")
    private String totalExpenses;
    @JsonProperty("totalGross")
    private String totalGross;
    @JsonProperty("totalInterest")
    private String totalInterest;
    @JsonProperty("totalInterestVAT")
    private String totalInterestVAT;
    @JsonProperty("totalCharges")
    private String totalCharges;
    @JsonProperty("totalChargesVAT")
    private String totalChargesVAT;

    @JsonProperty("dayOfLoan")
    public String getDayOfLoan() {
        return dayOfLoan;
    }

    @JsonProperty("dayOfLoan")
    public void setDayOfLoan(String dayOfLoan) {
        this.dayOfLoan = dayOfLoan;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("interestAdjustment")
    public InterestAdjustment getInterestAdjustment() {
        return interestAdjustment;
    }

    @JsonProperty("interestAdjustment")
    public void setInterestAdjustment(InterestAdjustment interestAdjustment) {
        this.interestAdjustment = interestAdjustment;
    }

    @JsonProperty("chargeAdjustments")
    public List<ChargeAdjustment> getChargeAdjustments() {
        return chargeAdjustments;
    }

    @JsonProperty("chargeAdjustments")
    public void setChargeAdjustments(List<ChargeAdjustment> chargeAdjustments) {
        this.chargeAdjustments = chargeAdjustments;
    }

    @JsonProperty("principal")
    public String getPrincipal() {
        return principal;
    }

    @JsonProperty("principal")
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @JsonProperty("totalExpenses")
    public String getTotalExpenses() {
        return totalExpenses;
    }

    @JsonProperty("totalExpenses")
    public void setTotalExpenses(String totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    @JsonProperty("totalGross")
    public String getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalInterest")
    public String getTotalInterest() {
        return totalInterest;
    }

    @JsonProperty("totalInterest")
    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    @JsonProperty("totalInterestVAT")
    public String getTotalInterestVAT() {
        return totalInterestVAT;
    }

    @JsonProperty("totalInterestVAT")
    public void setTotalInterestVAT(String totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    @JsonProperty("totalCharges")
    public String getTotalCharges() {
        return totalCharges;
    }

    @JsonProperty("totalCharges")
    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    @JsonProperty("totalChargesVAT")
    public String getTotalChargesVAT() {
        return totalChargesVAT;
    }

    @JsonProperty("totalChargesVAT")
    public void setTotalChargesVAT(String totalChargesVAT) {
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
class OneOffCharges {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeType")
    private String chargeType;
    @JsonProperty("chargeValue")
    private String chargeValue;
    @JsonProperty("chargeVAT")
    private String chargeVAT;
    @JsonProperty("daysOffset")
    private String daysOffset;

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
    public String getChargeValue() {
        return chargeValue;
    }

    @JsonProperty("chargeValue")
    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    @JsonProperty("chargeVAT")
    public String getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(String chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

    @JsonProperty("daysOffset")
    public String getDaysOffset() {
        return daysOffset;
    }

    @JsonProperty("daysOffset")
    public void setDaysOffset(String daysOffset) {
        this.daysOffset = daysOffset;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "periodIndex",
        "periodType",
        "periodStartTimemp",
        "periodEndTimestamp",
        "periodStartDayOfLoanIndex",
        "periodEndDayOfLoanIndex",
        "principal",
        "totalExpenses",
        "totalGross",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "totalOneOffCharges",
        "milestones"
})
class PeriodsProjection {

    @JsonProperty("periodIndex")
    private String periodIndex;
    @JsonProperty("periodType")
    private String periodType;
    @JsonProperty("periodStartTimemp")
    private String periodStartTimemp;
    @JsonProperty("periodEndTimestamp")
    private String periodEndTimestamp;
    @JsonProperty("periodStartDayOfLoanIndex")
    private String periodStartDayOfLoanIndex;
    @JsonProperty("periodEndDayOfLoanIndex")
    private String periodEndDayOfLoanIndex;
    @JsonProperty("principal")
    private String principal;
    @JsonProperty("totalExpenses")
    private String totalExpenses;
    @JsonProperty("totalGross")
    private String totalGross;
    @JsonProperty("totalInterest")
    private String totalInterest;
    @JsonProperty("totalInterestVAT")
    private String totalInterestVAT;
    @JsonProperty("totalCharges")
    private String totalCharges;
    @JsonProperty("totalChargesVAT")
    private String totalChargesVAT;
    @JsonProperty("totalOneOffCharges")
    private List<TotalOneOffCharge> totalOneOffCharges;
    @JsonProperty("milestones")
    private List<Milestone> milestones;

    @JsonProperty("periodIndex")
    public String getPeriodIndex() {
        return periodIndex;
    }

    @JsonProperty("periodIndex")
    public void setPeriodIndex(String periodIndex) {
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

    @JsonProperty("periodStartTimemp")
    public String getPeriodStartTimemp() {
        return periodStartTimemp;
    }

    @JsonProperty("periodStartTimemp")
    public void setPeriodStartTimemp(String periodStartTimemp) {
        this.periodStartTimemp = periodStartTimemp;
    }

    @JsonProperty("periodEndTimestamp")
    public String getPeriodEndTimestamp() {
        return periodEndTimestamp;
    }

    @JsonProperty("periodEndTimestamp")
    public void setPeriodEndTimestamp(String periodEndTimestamp) {
        this.periodEndTimestamp = periodEndTimestamp;
    }

    @JsonProperty("periodStartDayOfLoanIndex")
    public String getPeriodStartDayOfLoanIndex() {
        return periodStartDayOfLoanIndex;
    }

    @JsonProperty("periodStartDayOfLoanIndex")
    public void setPeriodStartDayOfLoanIndex(String periodStartDayOfLoanIndex) {
        this.periodStartDayOfLoanIndex = periodStartDayOfLoanIndex;
    }

    @JsonProperty("periodEndDayOfLoanIndex")
    public String getPeriodEndDayOfLoanIndex() {
        return periodEndDayOfLoanIndex;
    }

    @JsonProperty("periodEndDayOfLoanIndex")
    public void setPeriodEndDayOfLoanIndex(String periodEndDayOfLoanIndex) {
        this.periodEndDayOfLoanIndex = periodEndDayOfLoanIndex;
    }

    @JsonProperty("principal")
    public String getPrincipal() {
        return principal;
    }

    @JsonProperty("principal")
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @JsonProperty("totalExpenses")
    public String getTotalExpenses() {
        return totalExpenses;
    }

    @JsonProperty("totalExpenses")
    public void setTotalExpenses(String totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    @JsonProperty("totalGross")
    public String getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalInterest")
    public String getTotalInterest() {
        return totalInterest;
    }

    @JsonProperty("totalInterest")
    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    @JsonProperty("totalInterestVAT")
    public String getTotalInterestVAT() {
        return totalInterestVAT;
    }

    @JsonProperty("totalInterestVAT")
    public void setTotalInterestVAT(String totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    @JsonProperty("totalCharges")
    public String getTotalCharges() {
        return totalCharges;
    }

    @JsonProperty("totalCharges")
    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    @JsonProperty("totalChargesVAT")
    public String getTotalChargesVAT() {
        return totalChargesVAT;
    }

    @JsonProperty("totalChargesVAT")
    public void setTotalChargesVAT(String totalChargesVAT) {
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
        "chargeAmount",
        "chargeVAT"
})
class TotalOneOffCharge {

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeAmount")
    private String chargeAmount;
    @JsonProperty("chargeVAT")
    private String chargeVAT;

    @JsonProperty("chargeName")
    public String getChargeName() {
        return chargeName;
    }

    @JsonProperty("chargeName")
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @JsonProperty("chargeAmount")
    public String getChargeAmount() {
        return chargeAmount;
    }

    @JsonProperty("chargeAmount")
    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    @JsonProperty("chargeVAT")
    public String getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(String chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

}