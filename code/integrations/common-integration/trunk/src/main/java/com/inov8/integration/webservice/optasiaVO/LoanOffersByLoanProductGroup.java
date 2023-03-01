package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanProductGroup",
        "loanOffers"
})
public class LoanOffersByLoanProductGroup implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("loanProductGroup")
    private String loanProductGroup;
    @JsonProperty("loanOffers")
    private List<LoanOffers> loanOffersList;

    public String getLoanProductGroup() {
        return loanProductGroup;
    }

    public void setLoanProductGroup(String loanProductGroup) {
        this.loanProductGroup = loanProductGroup;
    }

    public List<LoanOffers> getLoanOffersList() {
        return loanOffersList;
    }

    public void setLoanOffersList(List<LoanOffers> loanOffersList) {
        this.loanOffersList = loanOffersList;
    }
}
