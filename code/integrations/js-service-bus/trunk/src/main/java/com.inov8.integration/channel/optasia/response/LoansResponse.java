package com.inov8.integration.channel.optasia.response;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

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
@Generated("jsonschema2pojo")
public class LoansResponse extends Response {

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

        i8SBSwitchControllerResponseVO.setResponseCode("00");
        i8SBSwitchControllerResponseVO.setIdentityValue(this.getIdentityValue());
        i8SBSwitchControllerResponseVO.setIdentityType(this.getIdentityType());
        i8SBSwitchControllerResponseVO.setOrigSource(this.getOrigSource());
        i8SBSwitchControllerResponseVO.setReceivedTimestamp(this.getReceivedTimestamp());
        for (int i = 0; i < loansPerState.size(); i++) {
            i8SBSwitchControllerResponseVO.setLoanState(loansPerState.get(i).getLoanState());
            i8SBSwitchControllerResponseVO.setInternalLoanId(loansPerState.get(i).getLoans().get(i).getLoan().getInternalLoanId());
            i8SBSwitchControllerResponseVO.setExternalLoanId(loansPerState.get(i).getLoans().get(i).getLoan().getExternalLoanId());
            i8SBSwitchControllerResponseVO.setLoanState(loansPerState.get(i).getLoans().get(i).getLoan().getLoanState());
            i8SBSwitchControllerResponseVO.setLoanTimeStamp(loansPerState.get(i).getLoans().get(i).getLoan().getLoanTimestamp());
            i8SBSwitchControllerResponseVO.setLoanReason(loansPerState.get(i).getLoans().get(i).getLoan().getLoanReason());
            i8SBSwitchControllerResponseVO.setAdvanceOfferId(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getAdvanceOfferId());
            i8SBSwitchControllerResponseVO.setOfferName(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getOfferName());
            i8SBSwitchControllerResponseVO.setCommodityType(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getCommodityType());
            i8SBSwitchControllerResponseVO.setCurrencyCode(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getCurrencyCode());
            i8SBSwitchControllerResponseVO.setPrincipalAmount(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getPrincipalAmount());
            i8SBSwitchControllerResponseVO.setSetUpFees(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getSetupFees());
            i8SBSwitchControllerResponseVO.setLoanPlanId(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanPlanId());
            i8SBSwitchControllerResponseVO.setLoanPlanName(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanPlanName());
            i8SBSwitchControllerResponseVO.setLoanProductGroup(loansPerState.get(i).getLoans().get(i).getLoan().getLoanOffer().getLoanProductGroup());
            i8SBSwitchControllerResponseVO.setRepaymentCounts(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getRepaymentsCount());
            i8SBSwitchControllerResponseVO.setGross(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getGross());
            i8SBSwitchControllerResponseVO.setPrincipal(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getPrincipal());
            i8SBSwitchControllerResponseVO.setSetUpFees(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getSetupFees());
            i8SBSwitchControllerResponseVO.setInterest(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getInterest());
            i8SBSwitchControllerResponseVO.setInterestVAT(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getInterestVAT());
            i8SBSwitchControllerResponseVO.setCharges(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getCharges());
            i8SBSwitchControllerResponseVO.setChargesVAT(loansPerState.get(i).getLoans().get(i).getReport().getRepayment().getChargesVAT());
            i8SBSwitchControllerResponseVO.setCurrencyCode(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getCurrencyCode());
            i8SBSwitchControllerResponseVO.setTotalGross(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalGross());
            i8SBSwitchControllerResponseVO.setTotalPrincipal(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalPrincipal());
            i8SBSwitchControllerResponseVO.setTotalSetupFees(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalSetupFees());
            i8SBSwitchControllerResponseVO.setTotalInterest(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalInterest());
            i8SBSwitchControllerResponseVO.setTotalInterestVAT(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalInterestVAT());
            i8SBSwitchControllerResponseVO.setTotalCharges(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalCharges());
            i8SBSwitchControllerResponseVO.setTotalChargesVAT(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalChargesVAT());
            i8SBSwitchControllerResponseVO.setTotalPendingRecoveries(loansPerState.get(i).getLoans().get(i).getReport().getOutstanding().getTotalPendingRecoveries());
            i8SBSwitchControllerResponseVO.setCurrentPeriod(loansPerState.get(i).getLoans().get(i).getReport().getPlan().getCurrentPeriod());
            i8SBSwitchControllerResponseVO.setDaysLeftInPeriod(loansPerState.get(i).getLoans().get(i).getReport().getPlan().getDaysLeftInPeriod());
            i8SBSwitchControllerResponseVO.setNextPeriod(loansPerState.get(i).getLoans().get(i).getReport().getPlan().getNextPeriod());
            collectionOfList.put("LoansPerState", loansPerState);
            i8SBSwitchControllerResponseVO.setCollectionOfList(collectionOfList);

        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loan",
        "report"
})
class Loan {

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
class LoanOffer {

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
@Generated("jsonschema2pojo")
class Loan__1 {

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
@Generated("jsonschema2pojo")
class LoansPerState {

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
@Generated("jsonschema2pojo")
class Outstanding {

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
@Generated("jsonschema2pojo")
class Plan {

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
@Generated("jsonschema2pojo")
class Repayment {

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
@Generated("jsonschema2pojo")
class Report {

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