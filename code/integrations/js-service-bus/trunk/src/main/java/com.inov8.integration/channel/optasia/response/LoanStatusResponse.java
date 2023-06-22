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
                repayment.setRepaymentsCount(this.getLoanInfo().getReport().getRepayment().getRepaymentsCount().toString());
                repayment.setGross(this.getLoanInfo().getReport().getRepayment().getGross());
                repayment.setPrincipal(this.getLoanInfo().getReport().getRepayment().getPrincipal());
                repayment.setSetupFees(this.getLoanInfo().getReport().getRepayment().getSetupFees());
                repayment.setInterest(this.getLoanInfo().getReport().getRepayment().getInterest());
                repayment.setInterestVAT(this.getLoanInfo().getReport().getRepayment().getInterestVAT());
                repayment.setCharges(this.getLoanInfo().getReport().getRepayment().getCharges());
                repayment.setChargesVAT(this.getLoanInfo().getReport().getRepayment().getChargesVAT());

                outstandingStatus.setCurrencyCode(this.getLoanInfo().getReport().getOutstanding().getCurrencyCode());
                outstandingStatus.setTotalGross(this.getLoanInfo().getReport().getOutstanding().getTotalGross());
                outstandingStatus.setTotalPrincipal(this.getLoanInfo().getReport().getOutstanding().getTotalPrincipal());
                outstandingStatus.setTotalSetupFees(this.getLoanInfo().getReport().getOutstanding().getTotalSetupFees());
                outstandingStatus.setTotalInterest(this.getLoanInfo().getReport().getOutstanding().getTotalInterest());
                outstandingStatus.setTotalInterestVAT(this.getLoanInfo().getReport().getOutstanding().getTotalInterestVAT());
                outstandingStatus.setTotalCharges(this.getLoanInfo().getReport().getOutstanding().getTotalCharges());
                outstandingStatus.setTotalChargesVAT(this.getLoanInfo().getReport().getOutstanding().getTotalChargesVAT());
                outstandingStatus.setTotalPendingRecoveries(this.getLoanInfo().getReport().getOutstanding().getTotalPendingRecoveries());

                plan.setCurrentPeriod(this.getLoanInfo().getReport().getPlan().getCurrentPeriod());
                plan.setDaysLeftInPeriod(this.getLoanInfo().getReport().getPlan().getDaysLeftInPeriod());
                plan.setNextPeriod(this.getLoanInfo().getReport().getPlan().getNextPeriod());
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
        "internalLoanId",
        "externalLoanId",
        "loanState",
        "loanTimestamp",
        "loanReason",
        "loanOffer"
})
class LoanSummary implements Serializable {

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
    private LoanOffers loanOffers;

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
        "report"
})
class LoanInfo implements Serializable {

    @JsonProperty("loan")
    private LoanSummary loanSummary;
    @JsonProperty("report")
    private Reports reports;

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

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
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
class LoanOffers implements Serializable {

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
    private MaturityDetail maturityDetail;

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
@Generated("jsonschema2pojo")
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