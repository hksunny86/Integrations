package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "repayment",
        "outstanding",
        "outstanding",
        "plan"
})
public class Report implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("repayment")
    private List<Repayment> repaymentList;
    @JsonProperty("outstanding")
    private List<OutstandingStatus> outstandingStatusList;
    @JsonProperty("endOfPeriodOutstanding")
    private List<EndOfPeriodOutstanding> endOfPeriodOutstandingList;
    @JsonProperty("plan")
    private List<Plan> planList;

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

    public List<Repayment> getRepaymentList() {
        return repaymentList;
    }

    public void setRepaymentList(List<Repayment> repaymentList) {
        this.repaymentList = repaymentList;
    }

    public List<EndOfPeriodOutstanding> getEndOfPeriodOutstandingList() {
        return endOfPeriodOutstandingList;
    }

    public void setEndOfPeriodOutstandingList(List<EndOfPeriodOutstanding> endOfPeriodOutstandingList) {
        this.endOfPeriodOutstandingList = endOfPeriodOutstandingList;
    }
}
