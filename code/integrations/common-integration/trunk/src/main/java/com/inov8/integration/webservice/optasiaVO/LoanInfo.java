package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loan",
        "report"
})
public class LoanInfo implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

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

