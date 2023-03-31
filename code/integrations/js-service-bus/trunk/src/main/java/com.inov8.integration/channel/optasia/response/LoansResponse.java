
package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.optasiaVO.Loans;

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
        "loansPerState"
})

public class LoansResponse extends Response implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("identityValue")
    private String identityValue;
    @JsonProperty("identityType")
    private String identityType;
    @JsonProperty("origSource")
    private String origSource;
    @JsonProperty("receivedTimestamp")
    private String receivedTimestamp;
    @JsonProperty("loansPerState")
    private List<LoansPerState> loansPerState;
    private String responseCode;
    private String responseDescription;
    private Map<String, List<?>> collectionOfList = new HashMap();
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;

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

    @JsonProperty("loansPerState")
    public List<LoansPerState> getLoansPerState() {
        return loansPerState;
    }

    @JsonProperty("loansPerState")
    public void setLoansPerState(List<LoansPerState> loansPerState) {
        this.loansPerState = loansPerState;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
        }
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());

        if (loansPerState != null) {

            List<com.inov8.integration.webservice.optasiaVO.LoansPerState> loansPerStateList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Report> reportList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Loan> loanList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.LoanOffers> loanOffersList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Loans> loansList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Repayment> repaymentList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.OutstandingStatus> outstandingStatusList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Plan> planList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.LoanReasonDetail> loanReasonDetailList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.LoanReasonDetail__1> loanReasonDetail__1s = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.MaturityDetails> maturityDetailsList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.Events> eventsList = new ArrayList<>();
            List<com.inov8.integration.webservice.optasiaVO.EventReasonDetail> eventReasonDetailList = new ArrayList<>();

            com.inov8.integration.webservice.optasiaVO.LoansPerState loansPerState;

            com.inov8.integration.webservice.optasiaVO.Report report;

            com.inov8.integration.webservice.optasiaVO.Loan loan;

            com.inov8.integration.webservice.optasiaVO.LoanOffers loanOffers;

            com.inov8.integration.webservice.optasiaVO.Loans loans;

            com.inov8.integration.webservice.optasiaVO.Repayment repayment;

            com.inov8.integration.webservice.optasiaVO.OutstandingStatus outstandingStatus;

            com.inov8.integration.webservice.optasiaVO.Plan plan;

            com.inov8.integration.webservice.optasiaVO.LoanReasonDetail loanReasonDetail;

            com.inov8.integration.webservice.optasiaVO.MaturityDetails maturityDetails;

            com.inov8.integration.webservice.optasiaVO.Events events;

            com.inov8.integration.webservice.optasiaVO.EventTypeDetails eventTypeDetails;

            com.inov8.integration.webservice.optasiaVO.EventReasonDetail eventReasonDetail;

            com.inov8.integration.webservice.optasiaVO.MaturityDetails__1 maturityDetails__1;

            com.inov8.integration.webservice.optasiaVO.LoanReasonDetail__1 loanReasonDetail__1;


            for (int i = 0; i < this.getLoansPerState().size(); i++) {

                loansPerState = new com.inov8.integration.webservice.optasiaVO.LoansPerState();
                report = new com.inov8.integration.webservice.optasiaVO.Report();
                loan = new com.inov8.integration.webservice.optasiaVO.Loan();
                loanOffers = new com.inov8.integration.webservice.optasiaVO.LoanOffers();
                loans = new com.inov8.integration.webservice.optasiaVO.Loans();
                repayment = new com.inov8.integration.webservice.optasiaVO.Repayment();
                outstandingStatus = new com.inov8.integration.webservice.optasiaVO.OutstandingStatus();
                plan = new com.inov8.integration.webservice.optasiaVO.Plan();
                loanReasonDetail = new com.inov8.integration.webservice.optasiaVO.LoanReasonDetail();
                maturityDetails = new com.inov8.integration.webservice.optasiaVO.MaturityDetails();
                events = new com.inov8.integration.webservice.optasiaVO.Events();
                eventTypeDetails = new com.inov8.integration.webservice.optasiaVO.EventTypeDetails();
                eventReasonDetail = new com.inov8.integration.webservice.optasiaVO.EventReasonDetail();
                maturityDetails__1 = new com.inov8.integration.webservice.optasiaVO.MaturityDetails__1();
                loanReasonDetail__1 = new com.inov8.integration.webservice.optasiaVO.LoanReasonDetail__1();


                loansPerState.setLoanState(this.getLoansPerState().get(i).getLoanState());
                loan.setLoanId(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanId());
                loan.setInternalLoanId(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getInternalLoanId());
                loan.setLoanTimestamp(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanTimestamp());
                loan.setLoanState(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanState());
                loan.setLoanReason(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanReason());
                loanReasonDetail.setUserApp(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanReasonDetails().get(i).getUserApp());
                loanReasonDetailList.add(loanReasonDetail);
                loan.setLoanReasonDetails(loanReasonDetailList);


                loanOffers.setOfferName(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getOfferName());
                loanOffers.setAdvanceOfferId(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getAdvanceOfferId());
                loanOffers.setCommodityType(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getCommodityType());
                loanOffers.setCurrencyCode(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getCurrencyCode());
                loanOffers.setPrincipalAmount(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getPrincipalAmount()));
                loanOffers.setSetupFees(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getSetupFees()));
                loanOffers.setLoanPlanId(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanPlanId()));
                loanOffers.setLoanPlanName(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanPlanName());
                loanOffers.setLoanProductGroup(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanProductGroup());
                maturityDetails.setMaturityDuration(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getMaturityDetails()));
                maturityDetailsList.add(maturityDetails);
                loanOffers.setMaturityDetailsList(maturityDetailsList);

                repayment.setRepaymentsCount(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getRepaymentsCount()));
                repayment.setGross(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getGross()));
                repayment.setPrincipal(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getPrincipal()));
                repayment.setSetupFees(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getSetupFees()));
                repayment.setInterest(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getInterest()));
                repayment.setInterestVAT(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getInterestVAT()));
                repayment.setCharges(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getCharges()));
                repayment.setChargesVAT(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getChargesVAT()));


                outstandingStatus.setCurrencyCode(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getCurrencyCode());
                outstandingStatus.setTotalGross(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalGross()));
                outstandingStatus.setTotalPrincipal(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalPrincipal()));
                outstandingStatus.setTotalSetupFees(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalSetupFees()));
                outstandingStatus.setTotalInterest(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalInterest()));
                outstandingStatus.setTotalInterestVAT(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalInterestVAT()));
                outstandingStatus.setTotalCharges(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalCharges()));
                outstandingStatus.setTotalChargesVAT(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalChargesVAT()));
                outstandingStatus.setTotalPendingRecoveries(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalPendingRecoveries()));


                plan.setCurrentPeriod(this.getLoansPerState().get(i).getLoans().get(i).getReport().getPlan().getCurrentPeriod());
                plan.setDaysLeftInPeriod(String.valueOf(this.getLoansPerState().get(i).getLoans().get(i).getReport().getPlan().getDaysLeftInPeriod()));
                plan.setNextPeriod(this.getLoansPerState().get(i).getLoans().get(i).getReport().getPlan().getNextPeriod());

                events.setEventType(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventType());
                eventTypeDetails.setIsPassiveAdvance(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventTypeDetails().getIsPassiveAdvance());
                eventTypeDetails.setPendingOperationId(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventTypeDetails().getPendingOperationId());
                events.setEventTypeDetails(eventTypeDetails);
                events.setEventTypeStatus(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventTypeStatus());
                events.setEventTransactionId(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventTransactionId());
                events.setThirdPartyTransactionId(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getThirdPartyTransactionId());
                events.setEventReason(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventReason());
                eventReasonDetail.setUserApp(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventReasonDetails().get(i).getUserApp());
                eventReasonDetailList.add(eventReasonDetail);
                events.setEventReasonDetails(eventReasonDetailList);
                events.setPeriod(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getPeriod());
                events.setPeriodIndex(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getPeriodIndex());
                events.setPeriodExpirationTimestamp(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getPeriodExpirationTimestamp());
                events.setPrincipalAdjustment(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getPrincipalAdjustment());
                events.setPrincipalBefore(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getPrincipalBefore());
                events.setPrincipalAfter(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getPrincipalAfter());
                events.setSetupFeesAdjustment(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getSetupFeesAdjustment());
                events.setSetupFeesBefore(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getSetupFeesBefore());
                events.setSetupFeesAfter(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getSetupFeesAfter());
                events.setInterestAdjustment(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getInterestAdjustment());
                events.setInterestAdjustmentVAT(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getInterestAdjustmentVAT());
                events.setInterestBefore(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getInterestBefore());
                events.setInterestAfter(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getInterestAfter());
                events.setTotalChargesAfter(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getTotalChargesAfter());
                events.setTotalChargesAdjustment(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getTotalChargesAdjustment());
                events.setTotalChargesAdjustmentVAT(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getTotalChargesAdjustmentVAT());
                events.setTotalChargesBefore(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getTotalChargesBefore());
                events.setTotalChargesAfter(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getTotalChargesAfter());
                events.setEventTimestamp(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getEventTimestamp());
                events.setReceptionTimestamp(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getReceptionTimestamp());
                events.setProcessingTimestamp(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getProcessingTimestamp());
                events.setSourceRequestId(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getSourceRequestId());
                events.setOfferName(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getOfferName());
                events.setCommodityType(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getCommodityType());
                events.setCurrencyCode(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getCurrencyCode());
                events.setPrincipalAmount(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getPrincipalAmount());
                events.setSetupFees(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getSetupFees());
                events.setLoanProductGroup(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getLoanProductGroup());
                events.setLoanPlanId(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getLoanPlanId());
                events.setLoanPlanName(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getLoanPlanName());
                events.setLoanPlanName(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getLoanPlanName());
                maturityDetails__1.setMaturityDuration(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getMaturityDetails().getMaturityDuration());
                events.setMaturityDetails(maturityDetails__1);
                events.setProjectSpecific(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getProjectSpecific());
                events.setLoanReason(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getLoanReason());
                loanReasonDetail__1.setUserApp(this.getLoansPerState().get(i).getLoans().get(i).getEvents().get(i).getLoanReasonDetails().get(i).getUserApp());
                loanReasonDetail__1s.add(loanReasonDetail__1);
                events.setLoanReasonDetails(loanReasonDetail__1s);

//            loans.setPlanList(planList);
//            loans.setOutstandingStatusList(outstandingStatusList);
//            report.setRepaymentList(repaymentList);
//            loans.setReportList(reportList);
//            loan.setLoanOffersList(loanOffersList);
//            loans.setLoanList(loanList);
//            loansPerState.setLoansList(loansList);

                eventsList.add(events);
                planList.add(plan);
                outstandingStatusList.add(outstandingStatus);
                repaymentList.add(repayment);
                reportList.add(report);
                loanOffersList.add(loanOffers);
                loanList.add(loan);
                loansList.add(loans);
                loansPerStateList.add(loansPerState);

                collectionOfList.put("LoansPerState", loansPerStateList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Loans", loansList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Loan", loanList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("LoanOffers", loanOffersList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Report", reportList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Repayment", repaymentList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Outstanding", outstandingStatusList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Plan", planList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

                collectionOfList.put("Events", eventsList);
                i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

            }
        }


        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "eventType",
        "eventTypeDetails",
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
        "totalChargesAdjustment",
        "totalChargesAdjustmentVAT",
        "totalChargesBefore",
        "totalChargesAfter",
        "eventTimestamp",
        "receptionTimestamp",
        "processingTimestamp",
        "remoteRequestId",
        "sourceRequestId",
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
        "loanReason",
        "loanReasonDetails"
})
class Events implements Serializable {

    @JsonProperty("eventType")
    private String eventType;
    @JsonProperty("eventTypeDetails")
    private EventTypeDetails eventTypeDetails;
    @JsonProperty("eventTypeStatus")
    private String eventTypeStatus;
    @JsonProperty("eventTransactionId")
    private String eventTransactionId;
    @JsonProperty("thirdPartyTransactionId")
    private String thirdPartyTransactionId;
    @JsonProperty("eventReason")
    private String eventReason;
    @JsonProperty("eventReasonDetails")
    private List<EventReasonDetail> eventReasonDetails;
    @JsonProperty("period")
    private String period;
    @JsonProperty("periodIndex")
    private Integer periodIndex;
    @JsonProperty("periodExpirationTimestamp")
    private String periodExpirationTimestamp;
    @JsonProperty("principalAdjustment")
    private Float principalAdjustment;
    @JsonProperty("principalBefore")
    private Float principalBefore;
    @JsonProperty("principalAfter")
    private Float principalAfter;
    @JsonProperty("setupFeesAdjustment")
    private Float setupFeesAdjustment;
    @JsonProperty("setupFeesBefore")
    private Float setupFeesBefore;
    @JsonProperty("setupFeesAfter")
    private Float setupFeesAfter;
    @JsonProperty("interestAdjustment")
    private Float interestAdjustment;
    @JsonProperty("interestAdjustmentVAT")
    private Float interestAdjustmentVAT;
    @JsonProperty("interestBefore")
    private Float interestBefore;
    @JsonProperty("interestAfter")
    private Float interestAfter;
    @JsonProperty("totalChargesAdjustment")
    private Float totalChargesAdjustment;
    @JsonProperty("totalChargesAdjustmentVAT")
    private Float totalChargesAdjustmentVAT;
    @JsonProperty("totalChargesBefore")
    private Float totalChargesBefore;
    @JsonProperty("totalChargesAfter")
    private Float totalChargesAfter;
    @JsonProperty("eventTimestamp")
    private String eventTimestamp;
    @JsonProperty("receptionTimestamp")
    private String receptionTimestamp;
    @JsonProperty("processingTimestamp")
    private String processingTimestamp;
    @JsonProperty("remoteRequestId")
    private String remoteRequestId;
    @JsonProperty("sourceRequestId")
    private String sourceRequestId;
    @JsonProperty("offerName")
    private String offerName;
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
    private MaturityDetails__1 maturityDetails;
    @JsonProperty("projectSpecific")
    private ProjectSpecific projectSpecific;
    @JsonProperty("loanReason")
    private String loanReason;
    @JsonProperty("loanReasonDetails")
    private List<LoanReasonDetail__1> loanReasonDetails;

    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @JsonProperty("eventTypeDetails")
    public EventTypeDetails getEventTypeDetails() {
        return eventTypeDetails;
    }

    @JsonProperty("eventTypeDetails")
    public void setEventTypeDetails(EventTypeDetails eventTypeDetails) {
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
    public List<EventReasonDetail> getEventReasonDetails() {
        return eventReasonDetails;
    }

    @JsonProperty("eventReasonDetails")
    public void setEventReasonDetails(List<EventReasonDetail> eventReasonDetails) {
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
    public Integer getPeriodIndex() {
        return periodIndex;
    }

    @JsonProperty("periodIndex")
    public void setPeriodIndex(Integer periodIndex) {
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
    public Float getPrincipalAdjustment() {
        return principalAdjustment;
    }

    @JsonProperty("principalAdjustment")
    public void setPrincipalAdjustment(Float principalAdjustment) {
        this.principalAdjustment = principalAdjustment;
    }

    @JsonProperty("principalBefore")
    public Float getPrincipalBefore() {
        return principalBefore;
    }

    @JsonProperty("principalBefore")
    public void setPrincipalBefore(Float principalBefore) {
        this.principalBefore = principalBefore;
    }

    @JsonProperty("principalAfter")
    public Float getPrincipalAfter() {
        return principalAfter;
    }

    @JsonProperty("principalAfter")
    public void setPrincipalAfter(Float principalAfter) {
        this.principalAfter = principalAfter;
    }

    @JsonProperty("setupFeesAdjustment")
    public Float getSetupFeesAdjustment() {
        return setupFeesAdjustment;
    }

    @JsonProperty("setupFeesAdjustment")
    public void setSetupFeesAdjustment(Float setupFeesAdjustment) {
        this.setupFeesAdjustment = setupFeesAdjustment;
    }

    @JsonProperty("setupFeesBefore")
    public Float getSetupFeesBefore() {
        return setupFeesBefore;
    }

    @JsonProperty("setupFeesBefore")
    public void setSetupFeesBefore(Float setupFeesBefore) {
        this.setupFeesBefore = setupFeesBefore;
    }

    @JsonProperty("setupFeesAfter")
    public Float getSetupFeesAfter() {
        return setupFeesAfter;
    }

    @JsonProperty("setupFeesAfter")
    public void setSetupFeesAfter(Float setupFeesAfter) {
        this.setupFeesAfter = setupFeesAfter;
    }

    @JsonProperty("interestAdjustment")
    public Float getInterestAdjustment() {
        return interestAdjustment;
    }

    @JsonProperty("interestAdjustment")
    public void setInterestAdjustment(Float interestAdjustment) {
        this.interestAdjustment = interestAdjustment;
    }

    @JsonProperty("interestAdjustmentVAT")
    public Float getInterestAdjustmentVAT() {
        return interestAdjustmentVAT;
    }

    @JsonProperty("interestAdjustmentVAT")
    public void setInterestAdjustmentVAT(Float interestAdjustmentVAT) {
        this.interestAdjustmentVAT = interestAdjustmentVAT;
    }

    @JsonProperty("interestBefore")
    public Float getInterestBefore() {
        return interestBefore;
    }

    @JsonProperty("interestBefore")
    public void setInterestBefore(Float interestBefore) {
        this.interestBefore = interestBefore;
    }

    @JsonProperty("interestAfter")
    public Float getInterestAfter() {
        return interestAfter;
    }

    @JsonProperty("interestAfter")
    public void setInterestAfter(Float interestAfter) {
        this.interestAfter = interestAfter;
    }

    @JsonProperty("totalChargesAdjustment")
    public Float getTotalChargesAdjustment() {
        return totalChargesAdjustment;
    }

    @JsonProperty("totalChargesAdjustment")
    public void setTotalChargesAdjustment(Float totalChargesAdjustment) {
        this.totalChargesAdjustment = totalChargesAdjustment;
    }

    @JsonProperty("totalChargesAdjustmentVAT")
    public Float getTotalChargesAdjustmentVAT() {
        return totalChargesAdjustmentVAT;
    }

    @JsonProperty("totalChargesAdjustmentVAT")
    public void setTotalChargesAdjustmentVAT(Float totalChargesAdjustmentVAT) {
        this.totalChargesAdjustmentVAT = totalChargesAdjustmentVAT;
    }

    @JsonProperty("totalChargesBefore")
    public Float getTotalChargesBefore() {
        return totalChargesBefore;
    }

    @JsonProperty("totalChargesBefore")
    public void setTotalChargesBefore(Float totalChargesBefore) {
        this.totalChargesBefore = totalChargesBefore;
    }

    @JsonProperty("totalChargesAfter")
    public Float getTotalChargesAfter() {
        return totalChargesAfter;
    }

    @JsonProperty("totalChargesAfter")
    public void setTotalChargesAfter(Float totalChargesAfter) {
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

    @JsonProperty("remoteRequestId")
    public String getRemoteRequestId() {
        return remoteRequestId;
    }

    @JsonProperty("remoteRequestId")
    public void setRemoteRequestId(String remoteRequestId) {
        this.remoteRequestId = remoteRequestId;
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
    public MaturityDetails__1 getMaturityDetails() {
        return maturityDetails;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(MaturityDetails__1 maturityDetails) {
        this.maturityDetails = maturityDetails;
    }

    @JsonProperty("projectSpecific")
    public ProjectSpecific getProjectSpecific() {
        return projectSpecific;
    }

    @JsonProperty("projectSpecific")
    public void setProjectSpecific(ProjectSpecific projectSpecific) {
        this.projectSpecific = projectSpecific;
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
    public List<LoanReasonDetail__1> getLoanReasonDetails() {
        return loanReasonDetails;
    }

    @JsonProperty("loanReasonDetails")
    public void setLoanReasonDetails(List<LoanReasonDetail__1> loanReasonDetails) {
        this.loanReasonDetails = loanReasonDetails;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userApp"
})
class EventReasonDetail implements Serializable {

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
        "is.passive.advance",
        "pending.operation.id",
        "advance.failed.failure.type"
})
class EventTypeDetails implements Serializable {

    @JsonProperty("is.passive.advance")
    private Boolean isPassiveAdvance;
    @JsonProperty("pending.operation.id")
    private Integer pendingOperationId;
    @JsonProperty("advance.failed.failure.type")
    private String advanceFailedFailureType;

    @JsonProperty("is.passive.advance")
    public Boolean getIsPassiveAdvance() {
        return isPassiveAdvance;
    }

    @JsonProperty("is.passive.advance")
    public void setIsPassiveAdvance(Boolean isPassiveAdvance) {
        this.isPassiveAdvance = isPassiveAdvance;
    }

    @JsonProperty("pending.operation.id")
    public Integer getPendingOperationId() {
        return pendingOperationId;
    }

    @JsonProperty("pending.operation.id")
    public void setPendingOperationId(Integer pendingOperationId) {
        this.pendingOperationId = pendingOperationId;
    }

    @JsonProperty("advance.failed.failure.type")
    public String getAdvanceFailedFailureType() {
        return advanceFailedFailureType;
    }

    @JsonProperty("advance.failed.failure.type")
    public void setAdvanceFailedFailureType(String advanceFailedFailureType) {
        this.advanceFailedFailureType = advanceFailedFailureType;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loan",
        "report",
        "events"
})
class Loan implements Serializable {

    @JsonProperty("loan")
    private Loan__1 loan;
    @JsonProperty("report")
    private Report report;
    @JsonProperty("events")
    private List<Events> events;

    @JsonProperty("loan")
    public Loan__1 getLoan() {
        return loan;
    }

    @JsonProperty("loan")
    public void setLoan(Loan__1 loan) {
        this.loan = loan;
    }

    @JsonProperty("report")
    public Report getReport() {
        return report;
    }

    @JsonProperty("report")
    public void setReport(Report report) {
        this.report = report;
    }

    @JsonProperty("events")
    public List<Events> getEvents() {
        return events;
    }

    @JsonProperty("events")
    public void setEvents(List<Events> events) {
        this.events = events;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "offerName",
        "advanceOfferId",
        "commodityType",
        "currencyCode",
        "principalAmount",
        "setupFees",
        "loanPlanId",
        "loanPlanName",
        "loanProductGroup",
        "maturityDetails"
})
class LoanOffer implements Serializable {

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
    @JsonProperty("loanPlanId")
    private Integer loanPlanId;
    @JsonProperty("loanPlanName")
    private String loanPlanName;
    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("maturityDetails")
    private MaturityDetailsLoans maturityDetails;

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
    public MaturityDetailsLoans getMaturityDetails() {
        return maturityDetails;
    }

    @JsonProperty("maturityDetails")
    public void setMaturityDetails(MaturityDetailsLoans maturityDetails) {
        this.maturityDetails = maturityDetails;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userApp"
})
class LoanReasonDetail implements Serializable {

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
        "userApp"
})
class LoanReasonDetail__1 implements Serializable {

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
        "loanId",
        "internalLoanId",
        "loanTimestamp",
        "loanState",
        "loanReason",
        "loanReasonDetails",
        "loanOffer"
})
class Loan__1 implements Serializable {

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
    private List<LoanReasonDetail> loanReasonDetails;
    @JsonProperty("loanOffer")
    private LoanOffer loanOffer;

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
    public List<LoanReasonDetail> getLoanReasonDetails() {
        return loanReasonDetails;
    }

    @JsonProperty("loanReasonDetails")
    public void setLoanReasonDetails(List<LoanReasonDetail> loanReasonDetails) {
        this.loanReasonDetails = loanReasonDetails;
    }

    @JsonProperty("loanOffer")
    public LoanOffer getLoanOffer() {
        return loanOffer;
    }

    @JsonProperty("loanOffer")
    public void setLoanOffer(LoanOffer loanOffer) {
        this.loanOffer = loanOffer;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanState",
        "loans"
})
class LoansPerState implements Serializable {

    @JsonProperty("loanState")
    private String loanState;
    @JsonProperty("loans")
    private List<Loan> loans;

    @JsonProperty("loanState")
    public String getLoanState() {
        return loanState;
    }

    @JsonProperty("loanState")
    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    @JsonProperty("loans")
    public List<Loan> getLoans() {
        return loans;
    }

    @JsonProperty("loans")
    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration"
})
class MaturityDetailsLoans implements Serializable {

    @JsonProperty("maturityDuration")
    private Integer maturityDuration;

    @JsonProperty("maturityDuration")
    public Integer getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(Integer maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration"
})
class MaturityDetails__1 implements Serializable {

    @JsonProperty("maturityDuration")
    private Integer maturityDuration;

    @JsonProperty("maturityDuration")
    public Integer getMaturityDuration() {
        return maturityDuration;
    }

    @JsonProperty("maturityDuration")
    public void setMaturityDuration(Integer maturityDuration) {
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
class Outstanding implements Serializable {

    @JsonProperty("currencyCode")
    private String currencyCode;
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
        "currentPeriod",
        "daysLeftInPeriod",
        "nextPeriod"
})
class Plan implements Serializable {

    @JsonProperty("currentPeriod")
    private String currentPeriod;
    @JsonProperty("daysLeftInPeriod")
    private Integer daysLeftInPeriod;
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
    public Integer getDaysLeftInPeriod() {
        return daysLeftInPeriod;
    }

    @JsonProperty("daysLeftInPeriod")
    public void setDaysLeftInPeriod(Integer daysLeftInPeriod) {
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
class ProjectSpecific extends com.inov8.integration.webservice.optasiaVO.ProjectSpecific {
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "repaymentsCount",
        "gross",
        "principal",
        "setupFees",
        "interest",
        "interestVAT",
        "charges",
        "chargesVAT"
})
class Repayment implements Serializable {

    @JsonProperty("repaymentsCount")
    private Integer repaymentsCount;
    @JsonProperty("gross")
    private Float gross;
    @JsonProperty("principal")
    private Float principal;
    @JsonProperty("setupFees")
    private Float setupFees;
    @JsonProperty("interest")
    private Float interest;
    @JsonProperty("interestVAT")
    private Float interestVAT;
    @JsonProperty("charges")
    private Float charges;
    @JsonProperty("chargesVAT")
    private Float chargesVAT;

    @JsonProperty("repaymentsCount")
    public Integer getRepaymentsCount() {
        return repaymentsCount;
    }

    @JsonProperty("repaymentsCount")
    public void setRepaymentsCount(Integer repaymentsCount) {
        this.repaymentsCount = repaymentsCount;
    }

    @JsonProperty("gross")
    public Float getGross() {
        return gross;
    }

    @JsonProperty("gross")
    public void setGross(Float gross) {
        this.gross = gross;
    }

    @JsonProperty("principal")
    public Float getPrincipal() {
        return principal;
    }

    @JsonProperty("principal")
    public void setPrincipal(Float principal) {
        this.principal = principal;
    }

    @JsonProperty("setupFees")
    public Float getSetupFees() {
        return setupFees;
    }

    @JsonProperty("setupFees")
    public void setSetupFees(Float setupFees) {
        this.setupFees = setupFees;
    }

    @JsonProperty("interest")
    public Float getInterest() {
        return interest;
    }

    @JsonProperty("interest")
    public void setInterest(Float interest) {
        this.interest = interest;
    }

    @JsonProperty("interestVAT")
    public Float getInterestVAT() {
        return interestVAT;
    }

    @JsonProperty("interestVAT")
    public void setInterestVAT(Float interestVAT) {
        this.interestVAT = interestVAT;
    }

    @JsonProperty("charges")
    public Float getCharges() {
        return charges;
    }

    @JsonProperty("charges")
    public void setCharges(Float charges) {
        this.charges = charges;
    }

    @JsonProperty("chargesVAT")
    public Float getChargesVAT() {
        return chargesVAT;
    }

    @JsonProperty("chargesVAT")
    public void setChargesVAT(Float chargesVAT) {
        this.chargesVAT = chargesVAT;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "repayment",
        "outstanding",
        "plan"
})
class Report implements Serializable {

    @JsonProperty("repayment")
    private Repayment repayment;
    @JsonProperty("outstanding")
    private Outstanding outstanding;
    @JsonProperty("plan")
    private Plan plan;

    @JsonProperty("repayment")
    public Repayment getRepayment() {
        return repayment;
    }

    @JsonProperty("repayment")
    public void setRepayment(Repayment repayment) {
        this.repayment = repayment;
    }

    @JsonProperty("outstanding")
    public Outstanding getOutstanding() {
        return outstanding;
    }

    @JsonProperty("outstanding")
    public void setOutstanding(Outstanding outstanding) {
        this.outstanding = outstanding;
    }

    @JsonProperty("plan")
    public Plan getPlan() {
        return plan;
    }

    @JsonProperty("plan")
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

}