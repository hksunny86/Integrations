package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanId",
        "internalLoanId",
        "externalLoanId",
        "loanState",
        "loanTimestamp",
        "loanReason",
        "loanReasonDetails",
        "loanOffer"
})
public class Loan implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

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
    private List<LoanOffers> loanOffersList;

    public List<LoanReasonDetail> getLoanReasonDetails() {
        return loanReasonDetails;
    }

    public void setLoanReasonDetails(List<LoanReasonDetail> loanReasonDetails) {
        this.loanReasonDetails = loanReasonDetails;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getExternalLoanId() {
        return externalLoanId;
    }

    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
    }

    public String getInternalLoanId() {
        return internalLoanId;
    }

    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanTimestamp() {
        return loanTimestamp;
    }

    public void setLoanTimestamp(String loanTimestamp) {
        this.loanTimestamp = loanTimestamp;
    }

    public String getLoanReason() {
        return loanReason;
    }

    public void setLoanReason(String loanReason) {
        this.loanReason = loanReason;
    }

    public List<LoanOffers> getLoanOffersList() {
        return loanOffersList;
    }

    public void setLoanOffersList(List<LoanOffers> loanOffersList) {
        this.loanOffersList = loanOffersList;
    }
}
