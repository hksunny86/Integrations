package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.optasiaVO.MaturityDetails;
import com.inov8.integration.webservice.optasiaVO.OutstandingStatus;
import com.inov8.integration.webservice.optasiaVO.Plan;
import com.inov8.integration.webservice.optasiaVO.Repayment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identityValue",
        "identityType",
        "origSource",
        "receivedTimestamp",
        "loanInfo"
})
public class LoanStatusResponse extends Response implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("loanInfo")
    private LoanInfo loanInfo;

    private String responseCode;
    private String responseDescription;

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

    @JsonProperty("loanInfo")
    public LoanInfo getLoanInfo() {
        return loanInfo;
    }

    @JsonProperty("loanInfo")
    public void setLoanInfo(LoanInfo loanInfo) {
        this.loanInfo = loanInfo;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode().equals("200") || this.getResponseCode() != null) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
        }
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());
        if (this.getLoanInfo() != null) {

            com.inov8.integration.webservice.optasiaVO.LoanInfo loanInfo = new com.inov8.integration.webservice.optasiaVO.LoanInfo();
            com.inov8.integration.webservice.optasiaVO.LoanSummary loanSummary = new com.inov8.integration.webservice.optasiaVO.LoanSummary();
            com.inov8.integration.webservice.optasiaVO.LoanOffers loanOffers = new com.inov8.integration.webservice.optasiaVO.LoanOffers();
            com.inov8.integration.webservice.optasiaVO.MaturityDetails maturityDetails = new MaturityDetails();
            com.inov8.integration.webservice.optasiaVO.Reports reports = new com.inov8.integration.webservice.optasiaVO.Reports();
            com.inov8.integration.webservice.optasiaVO.Repayment repayment = new com.inov8.integration.webservice.optasiaVO.Repayment();
            com.inov8.integration.webservice.optasiaVO.OutstandingStatus outstandingStatus = new com.inov8.integration.webservice.optasiaVO.OutstandingStatus();
            com.inov8.integration.webservice.optasiaVO.Plan plan = new com.inov8.integration.webservice.optasiaVO.Plan();

            i8SBSwitchControllerResponseVO.setLoanState(this.getLoanInfo().getLoan().getLoanState());

            loanSummary.setInternalLoanId(this.getLoanInfo().getLoan().getInternalLoanId());
            loanSummary.setLoanState(this.getLoanInfo().getLoan().getLoanState());
            loanSummary.setLoanTimestamp(this.getLoanInfo().getLoan().getLoanTimestamp());
            loanSummary.setLoanReason(this.getLoanInfo().getLoan().getLoanReason());
            loanOffers.setOfferName(this.getLoanInfo().getLoan().getLoanOffer().getOfferName());
            loanOffers.setCommodityType(this.getLoanInfo().getLoan().getLoanOffer().getCommodityType());
            loanOffers.setCurrencyCode(this.getLoanInfo().getLoan().getLoanOffer().getCurrencyCode());
            loanOffers.setPrincipalAmount(this.getLoanInfo().getLoan().getLoanOffer().getPrincipalAmount());
            loanOffers.setSetupFees(this.getLoanInfo().getLoan().getLoanOffer().getSetupFees());
            loanOffers.setLoanPlanId(this.getLoanInfo().getLoan().getLoanOffer().getLoanPlanId());
            loanOffers.setLoanPlanName(this.getLoanInfo().getLoan().getLoanOffer().getLoanPlanName());
            loanOffers.setLoanProductGroup(this.getLoanInfo().getLoan().getLoanOffer().getLoanProductGroup());
            maturityDetails.setMaturityDuration(this.getLoanInfo().getLoan().getLoanOffer().getMaturityDetails().getMaturityDuration());
            loanOffers.setMaturityDetailsList(Collections.singletonList(maturityDetails));
            loanSummary.setLoanOffer(loanOffers);

            if (this.getLoanInfo().getReport() != null) {
                if (this.getLoanInfo().getReport().getRepayment() != null) {
                    repayment.setRepaymentsCount(this.getLoanInfo().getReport().getRepayment().getRepaymentsCount().toString());
                    repayment.setGross(this.getLoanInfo().getReport().getRepayment().getGross());
                    repayment.setPrincipal(this.getLoanInfo().getReport().getRepayment().getPrincipal());
                    repayment.setSetupFees(this.getLoanInfo().getReport().getRepayment().getSetupFees());
                    repayment.setInterest(this.getLoanInfo().getReport().getRepayment().getInterest());
                    repayment.setInterestVAT(this.getLoanInfo().getReport().getRepayment().getInterestVAT());
                    repayment.setCharges(this.getLoanInfo().getReport().getRepayment().getCharges());
                    repayment.setChargesVAT(this.getLoanInfo().getReport().getRepayment().getChargesVAT());
                }

                if (this.getLoanInfo().getReport().getOutstanding() != null) {
                    outstandingStatus.setCurrencyCode(this.getLoanInfo().getReport().getOutstanding().getCurrencyCode());
                    outstandingStatus.setTotalGross(this.getLoanInfo().getReport().getOutstanding().getTotalGross());
                    outstandingStatus.setTotalPrincipal(this.getLoanInfo().getReport().getOutstanding().getTotalPrincipal());
                    outstandingStatus.setTotalSetupFees(this.getLoanInfo().getReport().getOutstanding().getTotalSetupFees());
                    outstandingStatus.setTotalInterest(this.getLoanInfo().getReport().getOutstanding().getTotalInterest());
                    outstandingStatus.setTotalInterestVAT(this.getLoanInfo().getReport().getOutstanding().getTotalInterestVAT());
                    outstandingStatus.setTotalCharges(this.getLoanInfo().getReport().getOutstanding().getTotalCharges());
                    outstandingStatus.setTotalChargesVAT(this.getLoanInfo().getReport().getOutstanding().getTotalChargesVAT());
                    outstandingStatus.setTotalPendingRecoveries(this.getLoanInfo().getReport().getOutstanding().getTotalPendingRecoveries());
                }

                if (this.getLoanInfo().getReport().getPlan() != null) {
                    plan.setCurrentPeriod(this.getLoanInfo().getReport().getPlan().getCurrentPeriod());
                    plan.setDaysLeftInPeriod(this.getLoanInfo().getReport().getPlan().getDaysLeftInPeriod());
                    plan.setNextPeriod(this.getLoanInfo().getReport().getPlan().getNextPeriod());
                }
            }

            if (this.getLoanInfo().getEvents() != null) {

                List<LoanStatusEvent> loanStatusEvents = this.getLoanInfo().getEvents();
                List<com.inov8.integration.webservice.optasiaVO.Event> eventList = new ArrayList<>();
                List<com.inov8.integration.webservice.optasiaVO.EventReasonDetails> eventReasonDetailsList = new ArrayList<>();
                List<com.inov8.integration.webservice.optasiaVO.EventTypeDetails> eventTypeDetailsList = new ArrayList<>();
                List<com.inov8.integration.webservice.optasiaVO.LoanReasonDetails> loanReasonDetailsList = new ArrayList<>();
                List<com.inov8.integration.webservice.optasiaVO.MaturityDetails> maturityDetailsList = new ArrayList<>();

                com.inov8.integration.webservice.optasiaVO.Event event;
                com.inov8.integration.webservice.optasiaVO.EventReasonDetails eventReasonDetails;
                com.inov8.integration.webservice.optasiaVO.EventTypeDetails eventTypeDetails;
                com.inov8.integration.webservice.optasiaVO.LoanReasonDetails loanReasonDetails;
                com.inov8.integration.webservice.optasiaVO.MaturityDetails maturityDetails1;
                for (int i = 0; i < loanStatusEvents.size(); i++) {

                    event = new com.inov8.integration.webservice.optasiaVO.Event();
                    eventTypeDetails = new com.inov8.integration.webservice.optasiaVO.EventTypeDetails();
                    eventReasonDetails = new com.inov8.integration.webservice.optasiaVO.EventReasonDetails();
                    loanReasonDetails = new com.inov8.integration.webservice.optasiaVO.LoanReasonDetails();
                    maturityDetails1 = new com.inov8.integration.webservice.optasiaVO.MaturityDetails();

                    event.setEventType(loanStatusEvents.get(i).getEventType());
                    if (loanStatusEvents.get(i).getEventTypeDetails() != null) {
                        eventTypeDetails.setChargeCalculationName(loanStatusEvents.get(i).getEventTypeDetails().getChargeCalculationName());
                        eventTypeDetails.setIsPassiveRecovery(loanStatusEvents.get(i).getEventTypeDetails().getIsPassiveRecovery());
                        eventTypeDetails.setRecoveryPassiveAutoRecovered(loanStatusEvents.get(i).getEventTypeDetails().getRecoveryPassiveAutoRecovered());
                        eventTypeDetails.setIsPassiveAdvance(loanStatusEvents.get(i).getEventTypeDetails().getIsPassiveAdvance());
                        eventTypeDetails.setPendingOperationId(loanStatusEvents.get(i).getEventTypeDetails().getPendingOperationId());
//                        eventTypeDetails.setAdvanceFailedFailureType(loanStatusEvents.get(i).getEventTypeDetails().getAdvanceFailedFailureType());
                    }
                    event.setEventTypeStatus(loanStatusEvents.get(i).getEventTypeStatus());
                    event.setEventTransactionId(loanStatusEvents.get(i).getEventTransactionId());
                    event.setThirdPartyTransactionId(loanStatusEvents.get(i).getThirdPartyTransactionId());
                    event.setEventReason(loanStatusEvents.get(i).getEventReason());
//                    if (loanStatusEvents.get(i).getEventReasonDetails() != null) {
//                        eventReasonDetails.setShortCode(loanStatusEvents.get(i).getEventReasonDetails().get(i).getUserApp());
//                    }
                    event.setPeriod(loanStatusEvents.get(i).getPeriod());
                    event.setPeriodIndex(loanStatusEvents.get(i).getPeriodIndex());
                    event.setPeriodExpirationTimestamp(loanStatusEvents.get(i).getPeriodExpirationTimestamp());
                    event.setPrincipalAdjustment(loanStatusEvents.get(i).getPrincipalAdjustment());
                    event.setPrincipalBefore(loanStatusEvents.get(i).getPrincipalBefore());
                    event.setPrincipalAfter(loanStatusEvents.get(i).getPrincipalAfter());
                    event.setSetupFeesAdjustment(loanStatusEvents.get(i).getSetupFeesAdjustment());
                    event.setSetupFeesBefore(loanStatusEvents.get(i).getSetupFeesBefore());
                    event.setSetupFeesAfter(loanStatusEvents.get(i).getSetupFeesAfter());
                    event.setInterestAdjustment(loanStatusEvents.get(i).getInterestAdjustment());
                    event.setInterestAdjustmentVAT(loanStatusEvents.get(i).getInterestAdjustmentVAT());
                    event.setInterestBefore(loanStatusEvents.get(i).getInterestBefore());
                    event.setInterestAfter(loanStatusEvents.get(i).getInterestAfter());
                    event.setTotalChargesAfter(loanStatusEvents.get(i).getTotalChargesAfter());
                    event.setTotalChargesAdjustment(loanStatusEvents.get(i).getTotalChargesAdjustment());
                    event.setTotalChargesAdjustmentVAT(loanStatusEvents.get(i).getTotalChargesAdjustmentVAT());
                    event.setTotalChargesBefore(loanStatusEvents.get(i).getTotalChargesBefore());
                    event.setEventTimestamp(loanStatusEvents.get(i).getEventTimestamp());
                    event.setReceptionTimestamp(loanStatusEvents.get(i).getReceptionTimestamp());
                    event.setProcessingTimestamp(loanStatusEvents.get(i).getProcessingTimestamp());
                    event.setSourceRequestId(loanStatusEvents.get(i).getSourceRequestId());
                    event.setLoanReason(loanStatusEvents.get(i).getLoanReason());
//                    if (loanStatusEvents.get(i).getLoanReasonDetails() != null) {
//                        loanReasonDetails.setShortCode(loanStatusEvents.get(i).getLoanReasonDetails().get(i).getUserApp());
//                    }
                    event.setLoanTimestamp(loanStatusEvents.get(i).getLoanTimestamp());
                    event.setInternalLoanId(loanStatusEvents.get(i).getInternalLoanId());
                    event.setLoanState(loanStatusEvents.get(i).getLoanState());
                    event.setOfferName(loanStatusEvents.get(i).getOfferName());
                    event.setCommodityType(loanStatusEvents.get(i).getCommodityType());
                    event.setCurrencyCode(loanStatusEvents.get(i).getCurrencyCode());
                    event.setPrincipalAmount(loanStatusEvents.get(i).getPrincipalAmount());
                    event.setSetupFees(loanStatusEvents.get(i).getSetupFees());
                    event.setLoanPlanId(loanStatusEvents.get(i).getLoanPlanId());
                    event.setLoanPlanName(loanStatusEvents.get(i).getLoanPlanName());
                    event.setLoanProductGroup(loanStatusEvents.get(i).getLoanProductGroup());
                    if (loanStatusEvents.get(i).getMaturityDetails() != null) {
                        maturityDetails.setMaturityDuration(String.valueOf(loanStatusEvents.get(i).getMaturityDetails().getMaturityDuration()));
                    }
                    eventReasonDetailsList.add(eventReasonDetails);
                    loanReasonDetailsList.add(loanReasonDetails);
                    maturityDetailsList.add(maturityDetails);
                    eventList.add(event);


                }
                i8SBSwitchControllerResponseVO.setEventList(eventList);
            }

            reports.setRepayment(repayment);
            reports.setOutstanding(outstandingStatus);
            reports.setPlan(plan);

            loanInfo.setLoan(loanSummary);
            loanInfo.setReport(reports);
            i8SBSwitchControllerResponseVO.setLoanInfoSummary(loanInfo);
        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanId",
        "internalLoanId",
        "loanTimestamp",
        "loanState",
        "loanReason",
        "loanReasonDetails",
        "loanOffer"
})
class LoanSummary implements Serializable {

    @JsonProperty("loanId")
    private String loanId;
    @JsonProperty("internalLoanId")
    private String internalLoanId;
    @JsonProperty("externalLoanId")
    private String externalLoanId;
    @JsonProperty("loanState")
    private String loanState;
    @JsonProperty("loanTimestamp")
    private String loanTimestamp;
    @JsonProperty("loanReason")
    private String loanReason;
    @JsonProperty("loanReasonDetails")
    private List<LoanReasonDetail> loanReasonDetails;
    @JsonProperty("loanOffer")
    private LoanOffers loanOffers;

    @JsonProperty("loanId")
    public String getLoanId() {
        return loanId;
    }

    @JsonProperty("loanId")
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    @JsonProperty("internalLoanId")
    public String getInternalLoanId() {
        return internalLoanId;
    }

    @JsonProperty("internalLoanId")
    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    @JsonProperty("externalLoanId")
    public String getExternalLoanId() {
        return externalLoanId;
    }

    @JsonProperty("externalLoanId")
    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
    }

    @JsonProperty("loanState")
    public String getLoanState() {
        return loanState;
    }

    @JsonProperty("loanState")
    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    @JsonProperty("loanTimestamp")
    public String getLoanTimestamp() {
        return loanTimestamp;
    }

    @JsonProperty("loanTimestamp")
    public void setLoanTimestamp(String loanTimestamp) {
        this.loanTimestamp = loanTimestamp;
    }

    @JsonProperty("loanReason")
    public String getLoanReason() {
        return loanReason;
    }

    @JsonProperty("loanReason")
    public void setLoanReason(String loanReason) {
        this.loanReason = loanReason;
    }

    @JsonProperty("loanReasonDetails")
    public List<LoanReasonDetail> getLoanReasonDetails() {
        return loanReasonDetails;
    }

    @JsonProperty("loanReasonDetails")
    public void setLoanReasonDetails(List<LoanReasonDetail> loanReasonDetails) {
        this.loanReasonDetails = loanReasonDetails;
    }

    @JsonProperty("loanOffer")
    public LoanOffers getLoanOffer() {
        return loanOffers;
    }

    @JsonProperty("loanOffer")
    public void setLoanOffer(LoanOffers loanOffers) {
        this.loanOffers = loanOffers;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loan",
        "report",
        "events"
})
class LoanInfo implements Serializable {

    @JsonProperty("loan")
    private LoanSummary loanSummary;
    @JsonProperty("report")
    private Reports reports;
    @JsonProperty("events")
    private List<LoanStatusEvent> events;

    @JsonProperty("loan")
    public LoanSummary getLoan() {
        return loanSummary;
    }

    @JsonProperty("loan")
    public void setLoan(LoanSummary loanSummary) {
        this.loanSummary = loanSummary;
    }

    @JsonProperty("report")
    public Reports getReport() {
        return reports;
    }

    @JsonProperty("report")
    public void setReport(Reports reports) {
        this.reports = reports;
    }

    @JsonProperty("events")
    public List<LoanStatusEvent> getEvents() {
        return events;
    }

    @JsonProperty("events")
    public void setEvents(List<LoanStatusEvent> events) {
        this.events = events;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "offerName",
        "advanceOfferId,",
        "commodityType",
        "currencyCode",
        "principalAmount",
        "setupFees",
        "loanPlanId",
        "loanPlanName",
        "loanProductGroup",
        "maturityDetails"
})
class LoanOffers implements Serializable {

    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("advanceOfferId")
    private String advanceOfferId;
    @JsonProperty("commodityType")
    private String commodityType;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("principalAmount")
    private String principalAmount;
    @JsonProperty("setupFees")
    private String setupFees;
    @JsonProperty("loanPlanId")
    private String loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("maturityDetails")
    private MaturityDetail maturityDetail;

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
    public String getPrincipalAmount() {
        return principalAmount;
    }

    @JsonProperty("principalAmount")
    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    @JsonProperty("setupFees")
    public String getSetupFees() {
        return setupFees;
    }

    @JsonProperty("setupFees")
    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
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
    public MaturityDetail getMaturityDetails() {
        return maturityDetail;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(MaturityDetail maturityDetail) {
        this.maturityDetail = maturityDetail;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration"
})
class MaturityDetail implements Serializable {

    @JsonProperty("maturityDuration")
    private String maturityDuration;

    @JsonProperty("maturityDuration")
    public String getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(String maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currencyCode",
        "totalGross",
        "totalPrincipal",
        "totalSetupFees",
        "totalInterest",
        "totalInterestVAT",
        "totalCharges",
        "totalChargesVAT",
        "totalPendingRecoveries"
})
class Outstandings implements Serializable {

    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("totalGross")
    private String totalGross;
    @JsonProperty("totalPrincipal")
    private String totalPrincipal;
    @JsonProperty("totalSetupFees")
    private String totalSetupFees;
    @JsonProperty("totalInterest")
    private String totalInterest;
    @JsonProperty("totalInterestVAT")
    private String totalInterestVAT;
    @JsonProperty("totalCharges")
    private String totalCharges;
    @JsonProperty("totalChargesVAT")
    private String totalChargesVAT;
    @JsonProperty("totalPendingRecoveries")
    private String totalPendingRecoveries;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("totalGross")
    public String getTotalGross() {
        return totalGross;
    }

    @JsonProperty("totalGross")
    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    @JsonProperty("totalPrincipal")
    public String getTotalPrincipal() {
        return totalPrincipal;
    }

    @JsonProperty("totalPrincipal")
    public void setTotalPrincipal(String totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    @JsonProperty("totalSetupFees")
    public String getTotalSetupFees() {
        return totalSetupFees;
    }

    @JsonProperty("totalSetupFees")
    public void setTotalSetupFees(String totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
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

    @JsonProperty("totalPendingRecoveries")
    public String getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    @JsonProperty("totalPendingRecoveries")
    public void setTotalPendingRecoveries(String totalPendingRecoveries) {
        this.totalPendingRecoveries = totalPendingRecoveries;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currentPeriod",
        "daysLeftInPeriod",
        "nextPeriod"
})
class Plans implements Serializable {

    @JsonProperty("currentPeriod")
    private String currentPeriod;
    @JsonProperty("daysLeftInPeriod")
    private String daysLeftInPeriod;
    @JsonProperty("nextPeriod")
    private String nextPeriod;

    @JsonProperty("currentPeriod")
    public String getCurrentPeriod() {
        return currentPeriod;
    }

    @JsonProperty("currentPeriod")
    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    @JsonProperty("daysLeftInPeriod")
    public String getDaysLeftInPeriod() {
        return daysLeftInPeriod;
    }

    @JsonProperty("daysLeftInPeriod")
    public void setDaysLeftInPeriod(String daysLeftInPeriod) {
        this.daysLeftInPeriod = daysLeftInPeriod;
    }

    @JsonProperty("nextPeriod")
    public String getNextPeriod() {
        return nextPeriod;
    }

    @JsonProperty("nextPeriod")
    public void setNextPeriod(String nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
class LoanStatusProjectSpecific {

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lastRepaymentDate,",
        "repaymentsCount",
        "gross",
        "principal",
        "setupFees",
        "interest",
        "interestVAT",
        "charges",
        "chargesVAT"
})
class Repayments implements Serializable {

    @JsonProperty("lastRepaymentDate")
    private String lastRepaymentDate;
    @JsonProperty("repaymentsCount")
    private String repaymentsCount;
    @JsonProperty("gross")
    private String gross;
    @JsonProperty("principal")
    private String principal;
    @JsonProperty("setupFees")
    private String setupFees;
    @JsonProperty("interest")
    private String interest;
    @JsonProperty("interestVAT")
    private String interestVAT;
    @JsonProperty("charges")
    private String charges;
    @JsonProperty("chargesVAT")
    private String chargesVAT;

    @JsonProperty("lastRepaymentDate")
    public String getLastRepaymentDate() {
        return lastRepaymentDate;
    }

    @JsonProperty("lastRepaymentDate")
    public void setLastRepaymentDate(String lastRepaymentDate) {
        this.lastRepaymentDate = lastRepaymentDate;
    }

    @JsonProperty("repaymentsCount")
    public String getRepaymentsCount() {
        return repaymentsCount;
    }

    @JsonProperty("repaymentsCount")
    public void setRepaymentsCount(String repaymentsCount) {
        this.repaymentsCount = repaymentsCount;
    }

    @JsonProperty("gross")
    public String getGross() {
        return gross;
    }

    @JsonProperty("gross")
    public void setGross(String gross) {
        this.gross = gross;
    }

    @JsonProperty("principal")
    public String getPrincipal() {
        return principal;
    }

    @JsonProperty("principal")
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @JsonProperty("setupFees")
    public String getSetupFees() {
        return setupFees;
    }

    @JsonProperty("setupFees")
    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
    }

    @JsonProperty("interest")
    public String getInterest() {
        return interest;
    }

    @JsonProperty("interest")
    public void setInterest(String interest) {
        this.interest = interest;
    }

    @JsonProperty("interestVAT")
    public String getInterestVAT() {
        return interestVAT;
    }

    @JsonProperty("interestVAT")
    public void setInterestVAT(String interestVAT) {
        this.interestVAT = interestVAT;
    }

    @JsonProperty("charges")
    public String getCharges() {
        return charges;
    }

    @JsonProperty("charges")
    public void setCharges(String charges) {
        this.charges = charges;
    }

    @JsonProperty("chargesVAT")
    public String getChargesVAT() {
        return chargesVAT;
    }

    @JsonProperty("chargesVAT")
    public void setChargesVAT(String chargesVAT) {
        this.chargesVAT = chargesVAT;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "repayment",
        "outstanding",
        "plan"
})
class Reports implements Serializable {

    @JsonProperty("repayment")
    private Repayments repayments;
    @JsonProperty("outstanding")
    private Outstandings outstandings;
    @JsonProperty("plan")
    private Plans plans;

    @JsonProperty("repayment")
    public Repayments getRepayment() {
        return repayments;
    }

    @JsonProperty("repayment")
    public void setRepayment(Repayments repayments) {
        this.repayments = repayments;
    }

    @JsonProperty("outstanding")
    public Outstandings getOutstanding() {
        return outstandings;
    }

    @JsonProperty("outstanding")
    public void setOutstanding(Outstandings outstandings) {
        this.outstandings = outstandings;
    }

    @JsonProperty("plan")
    public Plans getPlan() {
        return plans;
    }

    @JsonProperty("plan")
    public void setPlan(Plans plans) {
        this.plans = plans;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userApp"
})
class LoanStatusLoanReasonDetail__1 implements Serializable {

    @JsonProperty("userApp")
    private String userApp;

    @JsonProperty("userApp")
    public String getUserApp() {
        return userApp;
    }

    @JsonProperty("userApp")
    public void setUserApp(String userApp) {
        this.userApp = userApp;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "charge.calculation.name",
        "is.passive.recovery",
        "recovery.passive.auto.recovered",
        "is.passive.advance",
        "pending.operation.id"
})
class LoanStatusEventTypeDetails implements Serializable {

    @JsonProperty("charge.calculation.name")
    private String chargeCalculationName;
    @JsonProperty("is.passive.recovery")
    private Boolean isPassiveRecovery;
    @JsonProperty("recovery.passive.auto.recovered")
    private Boolean recoveryPassiveAutoRecovered;
    @JsonProperty("is.passive.advance")
    private Boolean isPassiveAdvance;
    @JsonProperty("pending.operation.id")
    private String pendingOperationId;

    @JsonProperty("charge.calculation.name")
    public String getChargeCalculationName() {
        return chargeCalculationName;
    }

    @JsonProperty("charge.calculation.name")
    public void setChargeCalculationName(String chargeCalculationName) {
        this.chargeCalculationName = chargeCalculationName;
    }

    @JsonProperty("is.passive.recovery")
    public Boolean getIsPassiveRecovery() {
        return isPassiveRecovery;
    }

    @JsonProperty("is.passive.recovery")
    public void setIsPassiveRecovery(Boolean isPassiveRecovery) {
        this.isPassiveRecovery = isPassiveRecovery;
    }

    @JsonProperty("recovery.passive.auto.recovered")
    public Boolean getRecoveryPassiveAutoRecovered() {
        return recoveryPassiveAutoRecovered;
    }

    @JsonProperty("recovery.passive.auto.recovered")
    public void setRecoveryPassiveAutoRecovered(Boolean recoveryPassiveAutoRecovered) {
        this.recoveryPassiveAutoRecovered = recoveryPassiveAutoRecovered;
    }

    @JsonProperty("is.passive.advance")
    public Boolean getIsPassiveAdvance() {
        return isPassiveAdvance;
    }

    @JsonProperty("is.passive.advance")
    public void setIsPassiveAdvance(Boolean isPassiveAdvance) {
        this.isPassiveAdvance = isPassiveAdvance;
    }

    @JsonProperty("pending.operation.id")
    public String getPendingOperationId() {
        return pendingOperationId;
    }

    @JsonProperty("pending.operation.id")
    public void setPendingOperationId(String pendingOperationId) {
        this.pendingOperationId = pendingOperationId;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userApp"
})
class LoanStatusEventReasonDetail implements Serializable {

    @JsonProperty("userApp")
    private String userApp;

    @JsonProperty("userApp")
    public String getUserApp() {
        return userApp;
    }

    @JsonProperty("userApp")
    public void setUserApp(String userApp) {
        this.userApp = userApp;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "eventType",
        "eventTypeDetails",
        "eventTypeStatus",
        "eventReason",
        "eventReasonDetails",
        "period",
        "periodIndex",
        "periodExpirationTimestamp",
        "principalAdjustment",
        "principalBefore",
        "principalAfter",
        "setupFeesAdjustment",
        "setupFeesBefore",
        "setupFeesAfter",
        "interestAdjustment",
        "interestAdjustmentVAT",
        "interestBefore",
        "interestAfter",
        "totalChargesAdjustment",
        "totalChargesAdjustmentVAT",
        "totalChargesBefore",
        "totalChargesAfter",
        "eventTimestamp",
        "receptionTimestamp",
        "processingTimestamp",
        "offerName",
        "commodityType",
        "currencyCode",
        "principalAmount",
        "setupFees",
        "loanProductGroup",
        "loanPlanId",
        "loanPlanName",
        "maturityDetails",
        "projectSpecific",
        "loanId",
        "internalLoanId",
        "loanTimestamp",
        "loanState",
        "loanReason",
        "loanReasonDetails",
        "thirdPartyTransactionId",
        "sourceRequestId",
        "eventTransactionId",
        "remoteRequestId"
})
class LoanStatusEvent implements Serializable {

    @JsonProperty("eventType")
    private String eventType;
    @JsonProperty("eventTypeDetails")
    private LoanStatusEventTypeDetails eventTypeDetails;
    @JsonProperty("eventTypeStatus")
    private String eventTypeStatus;
    @JsonProperty("eventReason")
    private String eventReason;
    @JsonProperty("eventReasonDetails")
    private List<LoanStatusEventReasonDetail> eventReasonDetails;
    @JsonProperty("period")
    private String period;
    @JsonProperty("periodIndex")
    private String periodIndex;
    @JsonProperty("periodExpirationTimestamp")
    private String periodExpirationTimestamp;
    @JsonProperty("principalAdjustment")
    private String principalAdjustment;
    @JsonProperty("principalBefore")
    private String principalBefore;
    @JsonProperty("principalAfter")
    private String principalAfter;
    @JsonProperty("setupFeesAdjustment")
    private String setupFeesAdjustment;
    @JsonProperty("setupFeesBefore")
    private String setupFeesBefore;
    @JsonProperty("setupFeesAfter")
    private String setupFeesAfter;
    @JsonProperty("interestAdjustment")
    private String interestAdjustment;
    @JsonProperty("interestAdjustmentVAT")
    private String interestAdjustmentVAT;
    @JsonProperty("interestBefore")
    private String interestBefore;
    @JsonProperty("interestAfter")
    private String interestAfter;
    @JsonProperty("totalChargesAdjustment")
    private String totalChargesAdjustment;
    @JsonProperty("totalChargesAdjustmentVAT")
    private String totalChargesAdjustmentVAT;
    @JsonProperty("totalChargesBefore")
    private String totalChargesBefore;
    @JsonProperty("totalChargesAfter")
    private String totalChargesAfter;
    @JsonProperty("eventTimestamp")
    private String eventTimestamp;
    @JsonProperty("receptionTimestamp")
    private String receptionTimestamp;
    @JsonProperty("processingTimestamp")
    private String processingTimestamp;
    @JsonProperty("offerName")
    private String offerName;
    @JsonProperty("commodityType")
    private String commodityType;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("principalAmount")
    private String principalAmount;
    @JsonProperty("setupFees")
    private String setupFees;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("loanPlanId")
    private String loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("maturityDetails")
    private LoanStatusMaturityDetails__1 maturityDetails;
    @JsonProperty("projectSpecific")
    private LoanStatusProjectSpecific projectSpecific;
    @JsonProperty("loanId")
    private String loanId;
    @JsonProperty("internalLoanId")
    private String internalLoanId;
    @JsonProperty("loanTimestamp")
    private String loanTimestamp;
    @JsonProperty("loanState")
    private String loanState;
    @JsonProperty("loanReason")
    private String loanReason;
    @JsonProperty("loanReasonDetails")
    private List<LoanStatusLoanReasonDetail__1> loanReasonDetails;
    @JsonProperty("thirdPartyTransactionId")
    private String thirdPartyTransactionId;
    @JsonProperty("sourceRequestId")
    private String sourceRequestId;
    @JsonProperty("eventTransactionId")
    private String eventTransactionId;
    @JsonProperty("remoteRequestId")
    private String remoteRequestId;

    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @JsonProperty("eventTypeDetails")
    public LoanStatusEventTypeDetails getEventTypeDetails() {
        return eventTypeDetails;
    }

    @JsonProperty("eventTypeDetails")
    public void setEventTypeDetails(LoanStatusEventTypeDetails eventTypeDetails) {
        this.eventTypeDetails = eventTypeDetails;
    }

    @JsonProperty("eventTypeStatus")
    public String getEventTypeStatus() {
        return eventTypeStatus;
    }

    @JsonProperty("eventTypeStatus")
    public void setEventTypeStatus(String eventTypeStatus) {
        this.eventTypeStatus = eventTypeStatus;
    }

    @JsonProperty("eventReason")
    public String getEventReason() {
        return eventReason;
    }

    @JsonProperty("eventReason")
    public void setEventReason(String eventReason) {
        this.eventReason = eventReason;
    }

    @JsonProperty("eventReasonDetails")
    public List<LoanStatusEventReasonDetail> getEventReasonDetails() {
        return eventReasonDetails;
    }

    @JsonProperty("eventReasonDetails")
    public void setEventReasonDetails(List<LoanStatusEventReasonDetail> eventReasonDetails) {
        this.eventReasonDetails = eventReasonDetails;
    }

    @JsonProperty("period")
    public String getPeriod() {
        return period;
    }

    @JsonProperty("period")
    public void setPeriod(String period) {
        this.period = period;
    }

    @JsonProperty("periodIndex")
    public String getPeriodIndex() {
        return periodIndex;
    }

    @JsonProperty("periodIndex")
    public void setPeriodIndex(String periodIndex) {
        this.periodIndex = periodIndex;
    }

    @JsonProperty("periodExpirationTimestamp")
    public String getPeriodExpirationTimestamp() {
        return periodExpirationTimestamp;
    }

    @JsonProperty("periodExpirationTimestamp")
    public void setPeriodExpirationTimestamp(String periodExpirationTimestamp) {
        this.periodExpirationTimestamp = periodExpirationTimestamp;
    }

    @JsonProperty("principalAdjustment")
    public String getPrincipalAdjustment() {
        return principalAdjustment;
    }

    @JsonProperty("principalAdjustment")
    public void setPrincipalAdjustment(String principalAdjustment) {
        this.principalAdjustment = principalAdjustment;
    }

    @JsonProperty("principalBefore")
    public String getPrincipalBefore() {
        return principalBefore;
    }

    @JsonProperty("principalBefore")
    public void setPrincipalBefore(String principalBefore) {
        this.principalBefore = principalBefore;
    }

    @JsonProperty("principalAfter")
    public String getPrincipalAfter() {
        return principalAfter;
    }

    @JsonProperty("principalAfter")
    public void setPrincipalAfter(String principalAfter) {
        this.principalAfter = principalAfter;
    }

    @JsonProperty("setupFeesAdjustment")
    public String getSetupFeesAdjustment() {
        return setupFeesAdjustment;
    }

    @JsonProperty("setupFeesAdjustment")
    public void setSetupFeesAdjustment(String setupFeesAdjustment) {
        this.setupFeesAdjustment = setupFeesAdjustment;
    }

    @JsonProperty("setupFeesBefore")
    public String getSetupFeesBefore() {
        return setupFeesBefore;
    }

    @JsonProperty("setupFeesBefore")
    public void setSetupFeesBefore(String setupFeesBefore) {
        this.setupFeesBefore = setupFeesBefore;
    }

    @JsonProperty("setupFeesAfter")
    public String getSetupFeesAfter() {
        return setupFeesAfter;
    }

    @JsonProperty("setupFeesAfter")
    public void setSetupFeesAfter(String setupFeesAfter) {
        this.setupFeesAfter = setupFeesAfter;
    }

    @JsonProperty("interestAdjustment")
    public String getInterestAdjustment() {
        return interestAdjustment;
    }

    @JsonProperty("interestAdjustment")
    public void setInterestAdjustment(String interestAdjustment) {
        this.interestAdjustment = interestAdjustment;
    }

    @JsonProperty("interestAdjustmentVAT")
    public String getInterestAdjustmentVAT() {
        return interestAdjustmentVAT;
    }

    @JsonProperty("interestAdjustmentVAT")
    public void setInterestAdjustmentVAT(String interestAdjustmentVAT) {
        this.interestAdjustmentVAT = interestAdjustmentVAT;
    }

    @JsonProperty("interestBefore")
    public String getInterestBefore() {
        return interestBefore;
    }

    @JsonProperty("interestBefore")
    public void setInterestBefore(String interestBefore) {
        this.interestBefore = interestBefore;
    }

    @JsonProperty("interestAfter")
    public String getInterestAfter() {
        return interestAfter;
    }

    @JsonProperty("interestAfter")
    public void setInterestAfter(String interestAfter) {
        this.interestAfter = interestAfter;
    }

    @JsonProperty("totalChargesAdjustment")
    public String getTotalChargesAdjustment() {
        return totalChargesAdjustment;
    }

    @JsonProperty("totalChargesAdjustment")
    public void setTotalChargesAdjustment(String totalChargesAdjustment) {
        this.totalChargesAdjustment = totalChargesAdjustment;
    }

    @JsonProperty("totalChargesAdjustmentVAT")
    public String getTotalChargesAdjustmentVAT() {
        return totalChargesAdjustmentVAT;
    }

    @JsonProperty("totalChargesAdjustmentVAT")
    public void setTotalChargesAdjustmentVAT(String totalChargesAdjustmentVAT) {
        this.totalChargesAdjustmentVAT = totalChargesAdjustmentVAT;
    }

    @JsonProperty("totalChargesBefore")
    public String getTotalChargesBefore() {
        return totalChargesBefore;
    }

    @JsonProperty("totalChargesBefore")
    public void setTotalChargesBefore(String totalChargesBefore) {
        this.totalChargesBefore = totalChargesBefore;
    }

    @JsonProperty("totalChargesAfter")
    public String getTotalChargesAfter() {
        return totalChargesAfter;
    }

    @JsonProperty("totalChargesAfter")
    public void setTotalChargesAfter(String totalChargesAfter) {
        this.totalChargesAfter = totalChargesAfter;
    }

    @JsonProperty("eventTimestamp")
    public String getEventTimestamp() {
        return eventTimestamp;
    }

    @JsonProperty("eventTimestamp")
    public void setEventTimestamp(String eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    @JsonProperty("receptionTimestamp")
    public String getReceptionTimestamp() {
        return receptionTimestamp;
    }

    @JsonProperty("receptionTimestamp")
    public void setReceptionTimestamp(String receptionTimestamp) {
        this.receptionTimestamp = receptionTimestamp;
    }

    @JsonProperty("processingTimestamp")
    public String getProcessingTimestamp() {
        return processingTimestamp;
    }

    @JsonProperty("processingTimestamp")
    public void setProcessingTimestamp(String processingTimestamp) {
        this.processingTimestamp = processingTimestamp;
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

    @JsonProperty("principalAmount")
    public String getPrincipalAmount() {
        return principalAmount;
    }

    @JsonProperty("principalAmount")
    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    @JsonProperty("setupFees")
    public String getSetupFees() {
        return setupFees;
    }

    @JsonProperty("setupFees")
    public void setSetupFees(String setupFees) {
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

    @JsonProperty("maturityDetails")
    public LoanStatusMaturityDetails__1 getMaturityDetails() {
        return maturityDetails;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(LoanStatusMaturityDetails__1 maturityDetails) {
        this.maturityDetails = maturityDetails;
    }

    @JsonProperty("projectSpecific")
    public LoanStatusProjectSpecific getProjectSpecific() {
        return projectSpecific;
    }

    @JsonProperty("projectSpecific")
    public void setProjectSpecific(LoanStatusProjectSpecific projectSpecific) {
        this.projectSpecific = projectSpecific;
    }

    @JsonProperty("loanId")
    public String getLoanId() {
        return loanId;
    }

    @JsonProperty("loanId")
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    @JsonProperty("internalLoanId")
    public String getInternalLoanId() {
        return internalLoanId;
    }

    @JsonProperty("internalLoanId")
    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    @JsonProperty("loanTimestamp")
    public String getLoanTimestamp() {
        return loanTimestamp;
    }

    @JsonProperty("loanTimestamp")
    public void setLoanTimestamp(String loanTimestamp) {
        this.loanTimestamp = loanTimestamp;
    }

    @JsonProperty("loanState")
    public String getLoanState() {
        return loanState;
    }

    @JsonProperty("loanState")
    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    @JsonProperty("loanReason")
    public String getLoanReason() {
        return loanReason;
    }

    @JsonProperty("loanReason")
    public void setLoanReason(String loanReason) {
        this.loanReason = loanReason;
    }

    @JsonProperty("loanReasonDetails")
    public List<LoanStatusLoanReasonDetail__1> getLoanReasonDetails() {
        return loanReasonDetails;
    }

    @JsonProperty("loanReasonDetails")
    public void setLoanReasonDetails(List<LoanStatusLoanReasonDetail__1> loanReasonDetails) {
        this.loanReasonDetails = loanReasonDetails;
    }

    @JsonProperty("thirdPartyTransactionId")
    public String getThirdPartyTransactionId() {
        return thirdPartyTransactionId;
    }

    @JsonProperty("thirdPartyTransactionId")
    public void setThirdPartyTransactionId(String thirdPartyTransactionId) {
        this.thirdPartyTransactionId = thirdPartyTransactionId;
    }

    @JsonProperty("sourceRequestId")
    public String getSourceRequestId() {
        return sourceRequestId;
    }

    @JsonProperty("sourceRequestId")
    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    @JsonProperty("eventTransactionId")
    public String getEventTransactionId() {
        return eventTransactionId;
    }

    @JsonProperty("eventTransactionId")
    public void setEventTransactionId(String eventTransactionId) {
        this.eventTransactionId = eventTransactionId;
    }

    @JsonProperty("remoteRequestId")
    public String getRemoteRequestId() {
        return remoteRequestId;
    }

    @JsonProperty("remoteRequestId")
    public void setRemoteRequestId(String remoteRequestId) {
        this.remoteRequestId = remoteRequestId;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration"
})
class LoanStatusMaturityDetails__1 implements Serializable {

    @JsonProperty("maturityDuration")
    private String maturityDuration;

    @JsonProperty("maturityDuration")
    public String getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(String maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

}
