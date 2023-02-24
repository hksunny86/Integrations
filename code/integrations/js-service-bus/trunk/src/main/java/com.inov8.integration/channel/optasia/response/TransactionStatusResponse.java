
package com.inov8.integration.channel.optasia.response;

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
        "events"
})
public class TransactionStatusResponse extends Response{

    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("events")
    private List<Event> events;

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

    @JsonProperty("events")
    public List<Event> getEvents() {
        return events;
    }

    @JsonProperty("events")
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        i8SBSwitchControllerResponseVO.setResponseCode("00");
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());
        i8SBSwitchControllerResponseVO.setEventType(this.getEvents().get(0).getEventType());
        i8SBSwitchControllerResponseVO.setEventTypeStatus(this.getEvents().get(0).getEventTypeStatus());
        i8SBSwitchControllerResponseVO.setEventTransactionId(this.getEvents().get(0).getEventTransactionId());
        i8SBSwitchControllerResponseVO.setThirdPartyTransactionId(this.getEvents().get(0).getThirdPartyTransactionId());
        i8SBSwitchControllerResponseVO.setEventReason(this.getEvents().get(0).getEventReason());
        i8SBSwitchControllerResponseVO.setShortCode(this.getEvents().get(0).getEventReasonDetails().getShortCode());
        i8SBSwitchControllerResponseVO.setPeriod(this.getEvents().get(0).getPeriod());
        i8SBSwitchControllerResponseVO.setPeriodIndex(this.getEvents().get(0).getPeriodIndex());
        i8SBSwitchControllerResponseVO.setPeriodExpirationTimestamp(this.getEvents().get(0).getPeriodExpirationTimestamp());
        i8SBSwitchControllerResponseVO.setPrincipalAdjustment(this.getEvents().get(0).getPrincipalAdjustment());
        i8SBSwitchControllerResponseVO.setPrincipalBefore(this.getEvents().get(0).getPrincipalBefore());
        i8SBSwitchControllerResponseVO.setPrincipalAfter(this.getEvents().get(0).getPrincipalAfter());
        i8SBSwitchControllerResponseVO.setSetupFeesAdjustment(this.getEvents().get(0).getSetupFeesAdjustment());
        i8SBSwitchControllerResponseVO.setSetupFeesBefore(this.getEvents().get(0).getSetupFeesBefore());
        i8SBSwitchControllerResponseVO.setSetupFeesAfter(this.getEvents().get(0).getSetupFeesAfter());
        i8SBSwitchControllerResponseVO.setInterestAdjustment(this.getEvents().get(0).getInterestAdjustment());
        i8SBSwitchControllerResponseVO.setInterestAdjustmentVAT(this.getEvents().get(0).getInterestAdjustmentVAT());
        i8SBSwitchControllerResponseVO.setInterestBefore(this.getEvents().get(0).getInterestBefore());
        i8SBSwitchControllerResponseVO.setInterestAfter(this.getEvents().get(0).getInterestAfter());
        i8SBSwitchControllerResponseVO.setTotalChargesAfter(this.getEvents().get(0).getTotalChargesAfter());
        i8SBSwitchControllerResponseVO.setTotalChargesAdjustment(this.getEvents().get(0).getTotalChargesAdjustment());
        i8SBSwitchControllerResponseVO.setTotalChargesAdjustmentVAT(this.getEvents().get(0).getTotalChargesAdjustmentVAT());
        i8SBSwitchControllerResponseVO.setTotalChargesBefore(this.getEvents().get(0).getTotalChargesBefore());
        i8SBSwitchControllerResponseVO.setEventTimestamp(this.getEvents().get(0).getEventTimestamp());
        i8SBSwitchControllerResponseVO.setReceptionTimestamp(this.getEvents().get(0).getReceptionTimestamp());
        i8SBSwitchControllerResponseVO.setProcessingTimestamp(this.getEvents().get(0).getProcessingTimestamp());
        i8SBSwitchControllerResponseVO.setSourceRequestId(this.getEvents().get(0).getSourceRequestId());
        i8SBSwitchControllerResponseVO.setLoanReason(this.getEvents().get(0).getLoanReason());
        i8SBSwitchControllerResponseVO.setShortCode(this.getEvents().get(0).getLoanReasonDetails().getShortCode());
        i8SBSwitchControllerResponseVO.setLoanTimeStamp(this.getEvents().get(0).getLoanTimestamp());
        i8SBSwitchControllerResponseVO.setInternalLoanId(this.getEvents().get(0).getInternalLoanId());
        i8SBSwitchControllerResponseVO.setLoanState(this.getEvents().get(0).getLoanState());
        i8SBSwitchControllerResponseVO.setExternalLoanId(this.getEvents().get(0).getExternalLoanId());
        i8SBSwitchControllerResponseVO.setOfferName(this.getEvents().get(0).getOfferName());
        i8SBSwitchControllerResponseVO.setCommodityType(this.getEvents().get(0).getCommodityType());
        i8SBSwitchControllerResponseVO.setCurrencyCode(this.getEvents().get(0).getCurrencyCode());
        i8SBSwitchControllerResponseVO.setPrincipalAmount(this.getEvents().get(0).getPrincipalAmount());
        i8SBSwitchControllerResponseVO.setSetupFees(this.getEvents().get(0).getSetupFees());
        i8SBSwitchControllerResponseVO.setLoanPlanId(this.getEvents().get(0).getLoanPlanId());
        i8SBSwitchControllerResponseVO.setLoanPlanName(this.getEvents().get(0).getLoanPlanName());
        i8SBSwitchControllerResponseVO.setLoanProductGroup(this.getEvents().get(0).getLoanProductGroup());
        i8SBSwitchControllerResponseVO.setMaturityDuration(this.getEvents().get(0).getMaturityDetails().getMaturityDuration());

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "eventType",
        "eventTypeStatus",
        "eventTransactionId",
        "thirdPartyTransactionId",
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
        "totalChargesAfter",
        "totalChargesAdjustment",
        "totalChargesAdjustmentVAT",
        "totalChargesBefore",
        "eventTimestamp",
        "receptionTimestamp",
        "processingTimestamp",
        "sourceRequestId",
        "loanReason",
        "loanReasonDetails",
        "loanTimestamp",
        "internalLoanId",
        "loanState",
        "externalLoanId",
        "offerName",
        "commodityType",
        "currencyCode",
        "principalAmount",
        "setupFees",
        "loanPlanId",
        "loanPlanName",
        "loanProductGroup",
        "maturityDetails"
})
 class Event {

    @JsonProperty("eventType")
    private String eventType;
    @JsonProperty("eventTypeStatus")
    private String eventTypeStatus;
    @JsonProperty("eventTransactionId")
    private String eventTransactionId;
    @JsonProperty("thirdPartyTransactionId")
    private String thirdPartyTransactionId;
    @JsonProperty("eventReason")
    private String eventReason;
    @JsonProperty("eventReasonDetails")
    private EventReasonDetails eventReasonDetails;
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
    @JsonProperty("totalChargesAfter")
    private String totalChargesAfter;
    @JsonProperty("totalChargesAdjustment")
    private String totalChargesAdjustment;
    @JsonProperty("totalChargesAdjustmentVAT")
    private String totalChargesAdjustmentVAT;
    @JsonProperty("totalChargesBefore")
    private String totalChargesBefore;
    @JsonProperty("eventTimestamp")
    private String eventTimestamp;
    @JsonProperty("receptionTimestamp")
    private String receptionTimestamp;
    @JsonProperty("processingTimestamp")
    private String processingTimestamp;
    @JsonProperty("sourceRequestId")
    private String sourceRequestId;
    @JsonProperty("loanReason")
    private String loanReason;
    @JsonProperty("loanReasonDetails")
    private LoanReasonDetails loanReasonDetails;
    @JsonProperty("loanTimestamp")
    private String loanTimestamp;
    @JsonProperty("internalLoanId")
    private String internalLoanId;
    @JsonProperty("loanState")
    private String loanState;
    @JsonProperty("externalLoanId")
    private String externalLoanId;
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
    @JsonProperty("loanPlanId")
    private String loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("maturityDetails")
    private TransactionsMaturityDetails transactionsMaturityDetails;

    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @JsonProperty("eventTypeStatus")
    public String getEventTypeStatus() {
        return eventTypeStatus;
    }

    @JsonProperty("eventTypeStatus")
    public void setEventTypeStatus(String eventTypeStatus) {
        this.eventTypeStatus = eventTypeStatus;
    }

    @JsonProperty("eventTransactionId")
    public String getEventTransactionId() {
        return eventTransactionId;
    }

    @JsonProperty("eventTransactionId")
    public void setEventTransactionId(String eventTransactionId) {
        this.eventTransactionId = eventTransactionId;
    }

    @JsonProperty("thirdPartyTransactionId")
    public String getThirdPartyTransactionId() {
        return thirdPartyTransactionId;
    }

    @JsonProperty("thirdPartyTransactionId")
    public void setThirdPartyTransactionId(String thirdPartyTransactionId) {
        this.thirdPartyTransactionId = thirdPartyTransactionId;
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
    public EventReasonDetails getEventReasonDetails() {
        return eventReasonDetails;
    }

    @JsonProperty("eventReasonDetails")
    public void setEventReasonDetails(EventReasonDetails eventReasonDetails) {
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

    @JsonProperty("totalChargesAfter")
    public String getTotalChargesAfter() {
        return totalChargesAfter;
    }

    @JsonProperty("totalChargesAfter")
    public void setTotalChargesAfter(String totalChargesAfter) {
        this.totalChargesAfter = totalChargesAfter;
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

    @JsonProperty("sourceRequestId")
    public String getSourceRequestId() {
        return sourceRequestId;
    }

    @JsonProperty("sourceRequestId")
    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
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
    public LoanReasonDetails getLoanReasonDetails() {
        return loanReasonDetails;
    }

    @JsonProperty("loanReasonDetails")
    public void setLoanReasonDetails(LoanReasonDetails loanReasonDetails) {
        this.loanReasonDetails = loanReasonDetails;
    }

    @JsonProperty("loanTimestamp")
    public String getLoanTimestamp() {
        return loanTimestamp;
    }

    @JsonProperty("loanTimestamp")
    public void setLoanTimestamp(String loanTimestamp) {
        this.loanTimestamp = loanTimestamp;
    }

    @JsonProperty("internalLoanId")
    public String getInternalLoanId() {
        return internalLoanId;
    }

    @JsonProperty("internalLoanId")
    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    @JsonProperty("loanState")
    public String getLoanState() {
        return loanState;
    }

    @JsonProperty("loanState")
    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    @JsonProperty("externalLoanId")
    public String getExternalLoanId() {
        return externalLoanId;
    }

    @JsonProperty("externalLoanId")
    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
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
    public TransactionsMaturityDetails getMaturityDetails() {
        return transactionsMaturityDetails;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(TransactionsMaturityDetails transactionsMaturityDetails) {
        this.transactionsMaturityDetails = transactionsMaturityDetails;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "shortCode",
        "type"
})
 class EventReasonDetails {

    @JsonProperty("shortCode")
    private String shortCode;
    @JsonProperty("type")
    private String type;

    @JsonProperty("shortCode")
    public String getShortCode() {
        return shortCode;
    }

    @JsonProperty("shortCode")
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "shortCode"
})
 class LoanReasonDetails {

    @JsonProperty("shortCode")
    private String shortCode;

    @JsonProperty("shortCode")
    public String getShortCode() {
        return shortCode;
    }

    @JsonProperty("shortCode")
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration"
})
 class TransactionsMaturityDetails {

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
