package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loanState",
        "loans"
})
public class LoansPerState implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("loanState")
    private String loanState;
    @JsonProperty("loans")
    private List<Loans> loansList;

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public List<Loans> getLoansList() {
        return loansList;
    }

    public void setLoansList(List<Loans> loansList) {
        this.loansList = loansList;
    }
}
