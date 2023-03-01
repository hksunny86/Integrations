package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "loan",
        "report",
        "outstanding",
        "plan"
})
public class Loans implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("loan")
    private List<Loan> loanList;
    @JsonProperty("report")
    private List<Report> reportList;
    @JsonProperty("outstanding")
    private List<OutstandingStatus> outstandingStatusList;
    @JsonProperty("plan")
    private List<Plan> planList;

    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }

    public List<OutstandingStatus> getOutstandingStatusList() {
        return outstandingStatusList;
    }

    public void setOutstandingStatusList(List<OutstandingStatus> outstandingStatusList) {
        this.outstandingStatusList = outstandingStatusList;
    }

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }
}
