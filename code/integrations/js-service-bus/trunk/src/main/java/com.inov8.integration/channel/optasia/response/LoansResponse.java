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

            com.inov8.integration.webservice.optasiaVO.LoansPerState loansPerState;

            com.inov8.integration.webservice.optasiaVO.Report report;

            com.inov8.integration.webservice.optasiaVO.Loan loan;

            com.inov8.integration.webservice.optasiaVO.LoanOffers loanOffers;

            com.inov8.integration.webservice.optasiaVO.Loans loans;

            com.inov8.integration.webservice.optasiaVO.Repayment repayment;

            com.inov8.integration.webservice.optasiaVO.OutstandingStatus outstandingStatus;

            com.inov8.integration.webservice.optasiaVO.Plan plan;

            for (int i = 0; i < this.getLoansPerState().size(); i++) {

                loansPerState = new com.inov8.integration.webservice.optasiaVO.LoansPerState();
                report = new com.inov8.integration.webservice.optasiaVO.Report();
                loan = new com.inov8.integration.webservice.optasiaVO.Loan();
                loanOffers = new com.inov8.integration.webservice.optasiaVO.LoanOffers();
                loans = new com.inov8.integration.webservice.optasiaVO.Loans();
                repayment = new com.inov8.integration.webservice.optasiaVO.Repayment();
                outstandingStatus = new com.inov8.integration.webservice.optasiaVO.OutstandingStatus();
                plan = new com.inov8.integration.webservice.optasiaVO.Plan();


                loansPerState.setLoanState(this.getLoansPerState().get(i).getLoanState());
                loan.setInternalLoanId(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getInternalLoanId());
                loan.setExternalLoanId(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getExternalLoanId());
                loan.setLoanState(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanState());
                loan.setLoanTimestamp(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanTimestamp());
                loan.setLoanReason(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanReason());


                loanOffers.setAdvanceOfferId(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getAdvanceOfferId());
                loanOffers.setOfferName(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getOfferName());
                loanOffers.setCommodityType(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getCommodityType());
                loanOffers.setCurrencyCode(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getCurrencyCode());
                loanOffers.setPrincipalAmount(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getPrincipalAmount());
                loanOffers.setSetupFees(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getSetupFees());
                loanOffers.setLoanPlanId(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanPlanId());
                loanOffers.setLoanPlanName(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanPlanName());
                loanOffers.setLoanProductGroup(this.getLoansPerState().get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanProductGroup());


                repayment.setRepaymentsCount(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getRepaymentsCount());
                repayment.setGross(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getGross());
                repayment.setPrincipal(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getPrincipal());
                repayment.setSetupFees(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getSetupFees());
                repayment.setInterest(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getInterest());
                repayment.setInterestVAT(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getInterestVAT());
                repayment.setCharges(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getCharges());
                repayment.setChargesVAT(this.getLoansPerState().get(i).getLoans().get(i).getReport().getRepayment().getChargesVAT());


                outstandingStatus.setCurrencyCode(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getCurrencyCode());
                outstandingStatus.setTotalGross(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalGross());
                outstandingStatus.setTotalPrincipal(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalPrincipal());
                outstandingStatus.setTotalSetupFees(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalSetupFees());
                outstandingStatus.setTotalInterest(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalInterest());
                outstandingStatus.setTotalInterestVAT(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalInterestVAT());
                outstandingStatus.setTotalCharges(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalCharges());
                outstandingStatus.setTotalChargesVAT(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalChargesVAT());
                outstandingStatus.setTotalPendingRecoveries(this.getLoansPerState().get(i).getLoans().get(i).getReport().getOutstanding().getTotalPendingRecoveries());


                plan.setCurrentPeriod(this.getLoansPerState().get(i).getLoans().get(i).getReport().getPlan().getCurrentPeriod());
                plan.setDaysLeftInPeriod(this.getLoansPerState().get(i).getLoans().get(i).getReport().getPlan().getDaysLeftInPeriod());
                plan.setNextPeriod(this.getLoansPerState().get(i).getLoans().get(i).getReport().getPlan().getNextPeriod());


//            loans.setPlanList(planList);
//            loans.setOutstandingStatusList(outstandingStatusList);
//            report.setRepaymentList(repaymentList);
//            loans.setReportList(reportList);
//            loan.setLoanOffersList(loanOffersList);
//            loans.setLoanList(loanList);
//            loansPerState.setLoansList(loansList);

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

            }
        }


        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loan",
        "report"
})
class Loan implements Serializable {

    @JsonProperty("loan")
    private Loan__1 loan;
    @JsonProperty("report")
    private Report report;

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

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "advanceOfferId",
        "offerName",
        "commodityType",
        "currencyCode",
        "principalAmount",
        "setupFees",
        "loanPlanId",
        "loanPlanName",
        "loanProductGroup"
})
class LoanOffer implements Serializable {

    @JsonProperty("advanceOfferId")
    private String advanceOfferId;
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

    @JsonProperty("advanceOfferId")
    public String getAdvanceOfferId() {
        return advanceOfferId;
    }

    @JsonProperty("advanceOfferId")
    public void setAdvanceOfferId(String advanceOfferId) {
        this.advanceOfferId = advanceOfferId;
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

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "internalLoanId",
        "externalLoanId",
        "loanState",
        "loanTimestamp",
        "loanReason",
        "loanOffer"
})
class Loan__1 implements Serializable {

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
    @JsonProperty("loanOffer")
    private LoanOffer loanOffer;

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
class Plan implements Serializable {

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