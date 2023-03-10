package com.inov8.integration.middleware.mock;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.webservice.optasiaVO.EligibilityStatus;
import com.inov8.integration.webservice.optasiaVO.LoanOffersByLoanProductGroup;
import com.inov8.integration.webservice.optasiaVO.*;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

public class OptasiaMock {


    public OfferListForCommodityResponse offerListForCommodity() {

        OfferListForCommodityResponse webServiceVO = new OfferListForCommodityResponse();

        webServiceVO.setResponseCode("00");
        webServiceVO.setIdentityValue("16505130514");
        webServiceVO.setIdentityType("identityType");
        webServiceVO.setOrigSource("mobileApp");
        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
        EligibilityStatus eligibilityStatus = new EligibilityStatus();
        eligibilityStatus.setEligible(true);
        eligibilityStatus.setEligibilityStatus("ELIGIBLE");

        List<EligibilityStatus> eligibilityStatusList = new ArrayList<>();
        eligibilityStatusList.add(eligibilityStatus);

        webServiceVO.setEligibilityStatusList(eligibilityStatusList);

        LoanOffersByLoanProductGroup loanOffersByLoanProductGroup = new LoanOffersByLoanProductGroup();

        loanOffersByLoanProductGroup.setLoanProductGroup("consumer_group");


        List<LoanOffers> loanOffersList = new ArrayList<>();
        LoanOffers loanOffers = new LoanOffers();
        loanOffers.setOfferName("CASH15");
        loanOffers.setOfferClass("RANGE");
        loanOffers.setOfferName("CASHCON10000");
        loanOffers.setCurrencyCode("USD");
        loanOffers.setPrincipalFrom("50");
        loanOffers.setPrincipalTo("1000");
        loanOffers.setSetupFees("0");
        loanOffers.setCommodityType("CASH");
        loanOffers.setLoanPlanId("1");
        loanOffers.setLoanPlanName("Weekly Plan");
        loanOffersList.add(loanOffers);
//        webServiceVO.setLoanOffersList(loanOffersList);

        MaturityDetails maturityDetails = new MaturityDetails();

        maturityDetails.setMaturityDuration("7");

        List<MaturityDetails> maturityDetailsList = new ArrayList<>();

        maturityDetailsList.add(maturityDetails);

        loanOffers.setMaturityDetailsList(maturityDetailsList);

        List<Interest> interestList = new ArrayList<>();
        Interest interest = new Interest();
        interest.setInterestName("interest");
        interest.setInterestType("PERCENTAGE_OF_PRINCIPAL");
        interest.setInterestValue("0.15");
        interest.setInterestVAT("0");
        interest.setDaysOffset("0");
        interest.setInterval("1");

        interestList.add(interest);
        maturityDetails.setInterestList(interestList);
//        webServiceVO.setInterestList(interestList);

        List<OneOffCharges> oneOffChargesList = new ArrayList<>();
        OneOffCharges oneOffCharges = new OneOffCharges();
        oneOffCharges.setChargeName("initiation fee");
        oneOffCharges.setChargeType("FIXED_AMOUNT");
        oneOffCharges.setChargeValue("5");
        oneOffCharges.setChargeVAT("0.05");
        oneOffCharges.setDaysOffset("0");
        oneOffChargesList.add(oneOffCharges);
        maturityDetails.setOneOffChargesList(oneOffChargesList);
//        webServiceVO.setOneOffChargesList(oneOffChargesList);

        List<RecurringCharges> recurringChargesList = new ArrayList<>();
        RecurringCharges recurringCharges = new RecurringCharges();
        recurringCharges.setChargeName("service fee");
        recurringCharges.setChargeType("FIXED_AMOUNT");
        recurringCharges.setChargeValue("1");
        recurringCharges.setChargeVAT("0.05");
        recurringCharges.setDaysOffset("0");
        recurringCharges.setInterval("1");
        recurringChargesList.add(recurringCharges);
        maturityDetails.setRecurringChargesList(recurringChargesList);
//        webServiceVO.setRecurringChargesList(recurringChargesList);

        List<OutstandingStatus> outstandingStatusList = new ArrayList<>();
        OutstandingStatus outstandingStatus = new OutstandingStatus();
        outstandingStatus.setCurrencyCode("USD");
        outstandingStatus.setNumOutstandingLoans("0");
        outstandingStatus.setTotalGross("0");
        outstandingStatus.setTotalSetupFees("0");
        outstandingStatus.setTotalInterest("0");
        outstandingStatus.setTotalInterestVAT("0");
        outstandingStatus.setTotalCharges("0");
        outstandingStatus.setTotalChargesVAT("0");
        outstandingStatus.setTotalPendingLoans("0");
        outstandingStatus.setTotalPendingRecoveries("0");
        outstandingStatusList.add(outstandingStatus);

        webServiceVO.setOutstandingStatusList(outstandingStatusList);

        loanOffersByLoanProductGroup.setLoanOffersList(loanOffersList);
        List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroupList = new ArrayList<>();
        loanOffersByLoanProductGroupList.add(loanOffersByLoanProductGroup);
        webServiceVO.setLoanOffersByLoanProductGroupList(loanOffersByLoanProductGroupList);
        return webServiceVO;
    }

    public LoanOfferResponse loanOffer() {

        LoanOfferResponse webServiceVO = new LoanOfferResponse();


        webServiceVO.setResponseCode("00");
        webServiceVO.setCode("0");
        webServiceVO.setMessage("ACK");
        webServiceVO.setIdentityValue("16505130514");
        webServiceVO.setIdentityType("identityType");
        webServiceVO.setOrigSource("mobileApp");
        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
        webServiceVO.setSourceRequestId("EXT123");
        webServiceVO.setExternalLoanId("REF123939");
        webServiceVO.setInternalLoanId("ACS2021042634324123");
        webServiceVO.setAdvanceOfferId("REF123");
        webServiceVO.setOfferName("CASH15");

        return webServiceVO;
    }

    public LoanCallBackResponse callBack() {

        LoanCallBackResponse webServiceVO = new LoanCallBackResponse();

        webServiceVO.setResponseCode("00");
        webServiceVO.setResponseDescription("Success");

        return webServiceVO;
    }

    public LoansResponse loans() {
        LoansResponse webServiceVO = new LoansResponse();

        webServiceVO.setResponseCode("00");
        webServiceVO.setIdentityValue("16505130514");
        webServiceVO.setIdentityType("customeridentity");
        webServiceVO.setOrigSource("mobileApp");
        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
        LoansPerState loansPerState = new LoansPerState();
        loansPerState.setLoanState("OPEN");


        Loan loan = new Loan();
        loan.setInternalLoanId("123444");
//        loan.setExternalLoanId("REF23232323");
        loan.setLoanState("OPEN");
        loan.setLoanTimestamp("2021-07-20T10:10:10.348+03:00");
        loan.setLoanReason("USSD_INBOUND");

        Loans loans = new Loans();
        List<Loan> loanList = new ArrayList<>();
        loanList.add(loan);

        loans.setLoanList(loanList);


        LoanOffers loanOffers = new LoanOffers();

        loanOffers.setAdvanceOfferId("101");
        loanOffers.setOfferName("CASH15");
        loanOffers.setCommodityType("CASH");
        loanOffers.setCurrencyCode("USD");
        loanOffers.setPrincipalAmount("15");
        loanOffers.setSetupFees("5");
        loanOffers.setLoanPlanId("1");
        loanOffers.setLoanPlanName("Standard plan");
        loanOffers.setLoanProductGroup("Standard group");

        List<LoanOffers> loanOffersList = new ArrayList<>();
        loanOffersList.add(loanOffers);

        loan.setLoanOffersList(loanOffersList);

        Repayment repayment = new Repayment();
        repayment.setRepaymentsCount("0");
        repayment.setGross("0");
        repayment.setPrincipal("0");
        repayment.setSetupFees("0");
        repayment.setInterest("0");
        repayment.setInterestVAT("0");
        repayment.setCharges("0");
        repayment.setChargesVAT("0");

        Report report = new Report();
        List<Repayment> repaymentList = new ArrayList<>();
        repaymentList.add(repayment);

        report.setRepaymentList(repaymentList);

        List<Report> reportList = new ArrayList<>();
        reportList.add(report);

        loans.setReportList(reportList);


        OutstandingStatus outstandingStatus = new OutstandingStatus();
        outstandingStatus.setCurrencyCode("USD");
        outstandingStatus.setTotalGross("16");
        outstandingStatus.setTotalPrincipal("15");
        outstandingStatus.setTotalSetupFees("0");
        outstandingStatus.setTotalInterest("0");
        outstandingStatus.setTotalInterestVAT("0");
        outstandingStatus.setTotalCharges("0");
        outstandingStatus.setTotalChargesVAT("0");
        outstandingStatus.setTotalPendingRecoveries("0");

        List<OutstandingStatus> outstandingStatusList = new ArrayList<>();
        outstandingStatusList.add(outstandingStatus);
        loans.setOutstandingStatusList(outstandingStatusList);

        Plan plan = new Plan();
        plan.setCurrentPeriod("MATURITY");
        plan.setDaysLeftInPeriod("360");
        plan.setNextPeriod("DEFAULT");

        List<Plan> planList = new ArrayList<>();
        planList.add(plan);
        loans.setPlanList(planList);

        List<Loans> loansList = new ArrayList<>();
        loansList.add(loans);

        loansPerState.setLoansList(loansList);
        List<LoansPerState> loansPerStateList = new ArrayList<>();

        loansPerStateList.add(loansPerState);

        webServiceVO.setLoansPerStateList(loansPerStateList);

        return webServiceVO;
    }

    public ProjectionResponse projection() {

        ProjectionResponse webServiceVO = new ProjectionResponse();

        webServiceVO.setResponseCode("00");
        webServiceVO.setIdentityValue("16505130514");
        webServiceVO.setIdentityType("customeridentity");
        webServiceVO.setOrigSource("mobileApp");
        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
        List<LoanOffers> loanOffersList = new ArrayList<>();
        LoanOffers loanOffers = new LoanOffers();
        loanOffers.setOfferName("CASH15");
        loanOffers.setOfferClass("RANGE");
        loanOffers.setOfferName("CASHCON10000");
        loanOffers.setCurrencyCode("USD");
        loanOffers.setPrincipalFrom("50");
        loanOffers.setPrincipalTo("1000");
        loanOffers.setSetupFees("0");
        loanOffers.setCommodityType("CASH");
        loanOffers.setLoanPlanId("1");
        loanOffers.setLoanPlanName("Weekly Plan");

        MaturityDetails maturityDetails = new MaturityDetails();

        maturityDetails.setMaturityDuration("7");

        List<MaturityDetails> maturityDetailsList = new ArrayList<>();
        loanOffers.setMaturityDetailsList(maturityDetailsList);

        maturityDetailsList.add(maturityDetails);

        loanOffersList.add(loanOffers);
        webServiceVO.setLoanOffersList(loanOffersList);

        List<Interest> interestList = new ArrayList<>();
        Interest interest = new Interest();
        interest.setInterestName("interest");
        interest.setInterestType("PERCENTAGE_OF_PRINCIPAL");
        interest.setInterestValue("0.15");
        interest.setInterestVAT("0");
        interest.setDaysOffset("0");
        interest.setInterval("1");

        interestList.add(interest);


        interestList.add(interest);
        maturityDetails.setInterestList(interestList);

        List<OneOffCharges> oneOffChargesList = new ArrayList<>();
        OneOffCharges oneOffCharges = new OneOffCharges();
        oneOffCharges.setChargeName("initiation fee");
        oneOffCharges.setChargeType("FIXED_AMOUNT");
        oneOffCharges.setChargeValue("5");
        oneOffCharges.setChargeVAT("0.05");
        oneOffCharges.setDaysOffset("0");
        oneOffChargesList.add(oneOffCharges);

        List<PeriodsProjection> periodsProjectionList = new ArrayList<>();
        PeriodsProjection periodsProjection = new PeriodsProjection();
        periodsProjection.setPeriodIndex("1");
        periodsProjection.setPeriodType("MATURITY");
        periodsProjection.setPeriodStartTimemp("2021-07-01T13:10:38.738+03:00");
        periodsProjection.setPeriodEndTimestamp("2021-07-01T13:10:38.738+03:00");
        periodsProjection.setPeriodStartDayOfLoanIndex("1");
        periodsProjection.setPeriodEndDayOfLoanIndex("28");
        periodsProjection.setPrincipal("10000");
        periodsProjection.setTotalExpenses("2740.25");
        periodsProjection.setTotalGross("12740.25");
        periodsProjection.setTotalInterest("1400");
        periodsProjection.setTotalInterestVAT("182");
        periodsProjection.setTotalCharges("1025");
        periodsProjection.setTotalChargesVAT("133.25");
        periodsProjectionList.add(periodsProjection);

        webServiceVO.setPeriodsProjectionList(periodsProjectionList);

        TotalOneOffCharges totalOneOffCharges = new TotalOneOffCharges();

        totalOneOffCharges.setChargeName("initiation_fee");
        totalOneOffCharges.setChargeAmount("1025");
        totalOneOffCharges.setChargeVAT("133.25");
        List<TotalOneOffCharges> totalOneOffChargesList = new ArrayList<>();
        totalOneOffChargesList.add(totalOneOffCharges);
        periodsProjection.setTotalOneOffChargesList(totalOneOffChargesList);

        Milestones milestones = new Milestones();

        milestones.setDayOfLoan("1");
        milestones.setDate("2021-07-01T13:10:38.738+03:00");
        List<Milestones> milestonesList = new ArrayList<>();
        milestonesList.add(milestones);
        periodsProjection.setMilestonesList(milestonesList);

        InterestAdjustment interestAdjustment = new InterestAdjustment();

        interestAdjustment.setGross("395.5");
        interestAdjustment.setNet("350");
        interestAdjustment.setVat("45.5");
        List<InterestAdjustment> interestAdjustmentList = new ArrayList<>();
        interestAdjustmentList.add(interestAdjustment);
        milestones.setInterestAdjustmentList(interestAdjustmentList);

        ChargeAdjustments chargeAdjustments = new ChargeAdjustments();

        chargeAdjustments.setName("initiation_fee");
        chargeAdjustments.setGross("1158.25");
        chargeAdjustments.setNet("1025");
        chargeAdjustments.setVat("133.25");
        List<ChargeAdjustments> chargeAdjustmentsList = new ArrayList<>();
        chargeAdjustmentsList.add(chargeAdjustments);
        milestones.setChargeAdjustmentsList(chargeAdjustmentsList);

        milestones.setPrincipal("10000");
        milestones.setTotalExpenses("1553.5");
        milestones.setTotalGross("11553.75");
        milestones.setTotalInterest("350");
        milestones.setTotalInterestVAT("45.5");
        milestones.setTotalCharges("1025");
        milestones.setTotalChargesVAT("133.25");

        return webServiceVO;
    }

    public OutstandingResponse outstanding() {
        OutstandingResponse webServiceVO = new OutstandingResponse();

        webServiceVO.setResponseCode("00");
        webServiceVO.setIdentityValue("16505130514");
        webServiceVO.setIdentityType("customeridentity");
        webServiceVO.setOrigSource("mobileApp");
        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
        webServiceVO.setCurrencyCode("USD");
        webServiceVO.setNumOutstandingLoans("1");
        webServiceVO.setTotalGross("12");
        webServiceVO.setTotalPrincipal("2");
        webServiceVO.setTotalSetupFees("0");
        webServiceVO.setTotalInterest("0");
        webServiceVO.setTotalInterestVAT("0");
        webServiceVO.setTotalCharges("0");
        webServiceVO.setTotalChargesVAT("0");
        webServiceVO.setTotalPendingLoans("0");
        webServiceVO.setTotalPendingRecoveries("0");

        return webServiceVO;


    }

//    public WebServiceVO transaction() {
//        WebServiceVO webServiceVO = new WebServiceVO();
//
//        webServiceVO.setResponseCode("00");
//        webServiceVO.setIdentityValue("16505130514");
//        webServiceVO.setIdentityType("customeridentity");
//        webServiceVO.setOrigSource("mobileApp");
//        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
//        webServiceVO.setEventType("ADVANCE");
//        webServiceVO.setEventTypeStatus("COMPLETED");
//        webServiceVO.setEventTransactionId("ABC23434");
//        webServiceVO.setThirdPartyTransactionId("REF123123");
//        webServiceVO.setEventReason("USSD_INBOUND");
//        webServiceVO.setShortCode("123");
//        webServiceVO.setPeriod("MATURITY");
//        webServiceVO.setPeriodIndex("1");
//        webServiceVO.setPeriodExpirationTimestamp("2021-07-28T00:01:00.000+03:00");
//        webServiceVO.setPrincipalAdjustment("15");
//        webServiceVO.setPrincipalBefore("0");
//        webServiceVO.setPrincipalAfter("15");
//        webServiceVO.setSetupFeesAdjustment("5");
//        webServiceVO.setSetupFeesBefore("0");
//        webServiceVO.setSetupFeesAfter("5");
//        webServiceVO.setInterestAdjustment("0");
//        webServiceVO.setInterestAdjustmentVAT("0");
//        webServiceVO.setInterestBefore("0");
//        webServiceVO.setInterestAfter("0");
//        webServiceVO.setTotalChargesAfter("0");
//        webServiceVO.setTotalChargesAdjustment("0");
//        webServiceVO.setTotalChargesAdjustmentVAT("0");
//        webServiceVO.setTotalChargesBefore("0");
//        webServiceVO.setEventTimestamp("2021-07-20T10:10:10.348+03:00");
//        webServiceVO.setReceptionTimestamp("2021-07-20T10:10:10.348+03:00");
//        webServiceVO.setProcessingTimestamp("2021-07-20T10:10:10.348+03:00");
//        webServiceVO.setSourceRequestId("EXT123");
//        webServiceVO.setLoanReason("USSD_INBOUND");
//        webServiceVO.setLoanTimeStamp("2021-07-20T10:10:10.348+03:00");
//        webServiceVO.setInternalLoanId("123444");
//        webServiceVO.setLoanState("OPEN");
//        webServiceVO.setExternalLoanId("REF23232323");
//        webServiceVO.setOfferName("CASH15");
//        webServiceVO.setCommodityType("CASH");
//        webServiceVO.setCurrencyCode("USD");
//        webServiceVO.setPrincipalAmount("15");
//        webServiceVO.setSetUpFees("5");
//        webServiceVO.setLoanPlanId("1");
//        webServiceVO.setLoanPlanName("Standard plan");
//        webServiceVO.setLoanProductGroup("Standard group");
//        webServiceVO.setMaturityDuration("360");
//
//        return webServiceVO;
//    }

    public LoanStatusResponse status() {
        LoanStatusResponse webServiceVO = new LoanStatusResponse();

        webServiceVO.setResponseCode("00");
        webServiceVO.setIdentityValue("16505130514");
        webServiceVO.setIdentityType("customeridentity");
        webServiceVO.setOrigSource("mobileApp");
        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
        webServiceVO.setLoanState("OPEN");
        webServiceVO.setInternalLoanId("123444");
        webServiceVO.setExternalLoanId("REF23232323");
        webServiceVO.setLoanState("OPEN");
        webServiceVO.setLoanTimestamp("2021-07-20T10:10:10.348+03:00");
        webServiceVO.setLoanReason("USSD_INBOUND");
        webServiceVO.setOfferName("CASH15");
        webServiceVO.setCommodityType("CASH");
        webServiceVO.setCurrencyCode("USD");
        webServiceVO.setPrincipalAmount("15");
        webServiceVO.setSetupFees("5");
        webServiceVO.setLoanPlanId("1");
        webServiceVO.setLoanPlanName("Standard plan");
        webServiceVO.setLoanProductGroup("Standard group");
        webServiceVO.setMaturityDuration("360");
        webServiceVO.setRepaymentsCount("1");
        webServiceVO.setGross("10");
        webServiceVO.setPrincipal("5");
        webServiceVO.setSetupFees("0");
        webServiceVO.setInterest("0");
        webServiceVO.setInterestVAT("0");
        webServiceVO.setCharges("0");
        webServiceVO.setChargesVAT("0");
        webServiceVO.setCurrencyCode("USD");
        webServiceVO.setTotalGross("16");
        webServiceVO.setTotalPrincipal("15");
        webServiceVO.setTotalSetupFees("0");
        webServiceVO.setTotalInterest("0");
        webServiceVO.setTotalInterestVAT("0");
        webServiceVO.setTotalCharges("0");
        webServiceVO.setTotalChargesVAT("0");
        webServiceVO.setTotalPendingRecoveries("0");
        webServiceVO.setCurrentPeriod("MATURITY");
        webServiceVO.setDaysLeftInPeriod("360");
        webServiceVO.setNextPeriod("DEFAULT");

        return webServiceVO;

    }

    public LoanPaymentResponse payment() {

        LoanPaymentResponse webServiceVO = new LoanPaymentResponse();

        webServiceVO.setRrn("");
        webServiceVO.setResponseCode("00");
        webServiceVO.setCode("0");
        webServiceVO.setMessage("ACK");
        webServiceVO.setIdentityValue("16505130514");
        webServiceVO.setIdentityType("customerIdentity");
        webServiceVO.setOrigSource("mobileApp");
        webServiceVO.setReceivedTimestamp("2021-07-20T13:10:38.738+03:00");
        webServiceVO.setSourceRequestId("EXT123");
        webServiceVO.setTransactionId("54879653212");
        webServiceVO.setAmount("5000");
        webServiceVO.setProcessingFee("500");
        webServiceVO.setTotalAmount("5500");

        return webServiceVO;
    }

    public CustomerAnalyticsResponse analyticsResponse() {

        CustomerAnalyticsResponse webServiceVO = new CustomerAnalyticsResponse();

        webServiceVO.setRrn("");
        webServiceVO.setResponseCode("00");
        webServiceVO.setResponseDescription("Success");
        webServiceVO.setResponseDateTime("");
        webServiceVO.setStatusCode("111");
        webServiceVO.setMessageCode("00922001");
        webServiceVO.setMessage("Report generated successfully");
        webServiceVO.setReportDate("13 Aug 2020");
        webServiceVO.setReportTime("15:26:38");
        webServiceVO.setName("");
        webServiceVO.setCnic("xxxxx-xxxxxxx-x");
        webServiceVO.setCity("");
        webServiceVO.setNoOfActiveAccounts("");
        webServiceVO.setTotalOutstandingBalance("");
        webServiceVO.setDob("");
        webServiceVO.setPlus3024m("");
        webServiceVO.setPlus6024m("");
        webServiceVO.setPlus9024m("");
        webServiceVO.setPlus12024m("");
        webServiceVO.setPlus15024m("");
        webServiceVO.setPlus18024m("");
        webServiceVO.setDisclaimerText("The information contained in this report has been compiled from data provided by financial institutions and does not represent the opinion of Aequitas Information Services Limited with regards to the credit worthiness of the subject. As such, Aequitas Information Services Limited will not be liable for any loss or damage arising from the information contained herein and does not warrant the completeness, timeliness or accuracy of any data. The information contained in this report is supplied on a confidential basis to you and shall not be disclosed to any other person");
        webServiceVO.setRemarks("");


        return webServiceVO;

    }

    public LoansHistoryResponse loansHistoryResponse() {
        LoansHistoryResponse loansHistoryResponse = new LoansHistoryResponse();

        loansHistoryResponse.setRrn("");
        loansHistoryResponse.setResponseCode("00");
        loansHistoryResponse.setResponseDescription("Success");
        loansHistoryResponse.setResponseDateTime("20221229181818");
        History history = new History();
        history.setAmount("656");
        history.setDateTime("23022023");
        history.setStatus("Avail");
        history.setTitle("XtraCash");
        history = new History();
        history.setAmount("656");
        history.setDateTime("23022023");
        history.setStatus("Avail");
        history.setTitle("XtraCash");
        List<History> histories = new ArrayList<>();
        histories.add(history);
        loansHistoryResponse.setHistoryList(histories);


        return loansHistoryResponse;
    }

    public LoanPlanResponse loansPlanResponse() {
        LoanPlanResponse loanPlanResponse = new LoanPlanResponse();

        loanPlanResponse.setRrn("");
        loanPlanResponse.setResponseCode("00");
        loanPlanResponse.setResponseDescription("Success");
        loanPlanResponse.setResponseDateTime("");

        LoanAmount loanAmount = new LoanAmount();
        loanAmount.setAmount("656");
        loanAmount.setFee("5");
        loanAmount.setTitle("Xtra Cash");
        loanAmount.setWeek("1");
        List<LoanAmount> loanAmountList = new ArrayList<>();
        loanAmountList.add(loanAmount);

        DueDatePlan dueDatePlan = new DueDatePlan();
        dueDatePlan.setAmount("656");
        dueDatePlan.setFee("5");
        dueDatePlan.setTitle("Xtra Cash");
        dueDatePlan.setWeek("1");
        List<DueDatePlan> dueDatePlanList = new ArrayList<>();
        dueDatePlanList.add(dueDatePlan);

        loanPlanResponse.setLoanAmountList(loanAmountList);
        loanPlanResponse.setDueDatePlans(dueDatePlanList);


        return loanPlanResponse;
    }

    public TransactionActiveResponse transactionActiveResponse() {
        TransactionActiveResponse transactionActiveResponse = new TransactionActiveResponse();

        transactionActiveResponse.setRrn("");
        transactionActiveResponse.setResponseCode("00");
        transactionActiveResponse.setResponseDescription("Success");
        transactionActiveResponse.setResponseDateTime("");
        transactionActiveResponse.setStatus("Active");
        transactionActiveResponse.setIsStatus(true);

        return transactionActiveResponse;
    }

    public SimpleAccountOpeningResponse simpleAccountOpeningResponse() {
        SimpleAccountOpeningResponse simpleAccountOpeningResponse = new SimpleAccountOpeningResponse();

        simpleAccountOpeningResponse.setRrn("91522103234050");
        simpleAccountOpeningResponse.setResponseCode("00");
        simpleAccountOpeningResponse.setResponseDescription("Success");
        simpleAccountOpeningResponse.setResponseDateTime("");

        return simpleAccountOpeningResponse;
    }

    public static void main(String[] args) throws ParseException {

//        String date = "22072022";
////        SimpleDateFormat formatter1=new SimpleDateFormat("ddMMyyyy");
//        Date startDate=new SimpleDateFormat("ddMMyyyy").parse(date);
//        System.out.printf("Date"+startDate);

//        double amount = 100;
//        double weeklyPlan = 0.045*amount;
//
//        System.out.println("Weekly Plan "+weeklyPlan);

//        String dateStr = format.format(startDate);
//        String endStr = format.format(endDate);
//        String fromDate = "25022023";
//        DateFormat format = new SimpleDateFormat("ddMMyyyy");
////        Date dt = format.parse(fromDate);
//        System.out.println("From Date "+fromDate);
//
//        int dayOfMonth = Integer.parseInt(fromDate.substring(0,2));
//        int month = Integer.parseInt(fromDate.substring(2,4));
//        int year = Integer.parseInt(fromDate.substring(4,8));
//
////        System.out.println("Year: "+year);
////        System.out.println("Month: "+month);
////        System.out.println("Day: "+dayOfMonth);
//
//        Calendar cal = Calendar.getInstance();
//        try{
//            cal.setTime(format.parse(fromDate));
//        }catch(ParseException e){
//            e.printStackTrace();
//        }
//
//        cal.add(Calendar.DAY_OF_MONTH, 7);
//        String afterDate = format.format(cal.getTime());
//
//        System.out.println("Date after adding 7 days "+afterDate);
//
//        LocalDate date = LocalDate.of(year, month,dayOfMonth);
//        int weekOfYear = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
//        System.out.println("Week of Year "+weekOfYear);

//        double amount = 100;
//
//        List<LoanAmount> loanAmountList = new ArrayList<>();
//        LoanAmount loanAmount = new LoanAmount();
//
//        for (int i=0; i<8; i++){
//            if (i<4){
//                loanAmount.setTitle("Week"+i+1);
//                loanAmount.setWeek(String.valueOf(i+1));
//                loanAmount.setFee(String.valueOf(i+1*0.045*amount));
//                loanAmount.setAmount(0.045*amount+loanAmount.getFee());
//                loanAmountList.add(loanAmount);
//            }
//        }
//
        double value = 200.3456;
        DecimalFormat df = new DecimalFormat("####0.00");
        System.out.println("Value: " + df.format(value));

    }
}
