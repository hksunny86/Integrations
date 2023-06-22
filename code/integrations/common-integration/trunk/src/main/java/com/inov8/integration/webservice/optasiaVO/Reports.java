package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "repayment",
        "outstanding",
        "plan"
})
public class Reports implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("repayment")
    private Repayment repayments;
    @JsonProperty("outstanding")
    private OutstandingStatus outstandings;
    @JsonProperty("plan")
    private Plan plans;

    @JsonProperty("repayment")
    public Repayment getRepayment() {
        return repayments;
    }

    @JsonProperty("repayment")
    public void setRepayment(Repayment repayments) {
        this.repayments = repayments;
    }

    @JsonProperty("outstanding")
    public OutstandingStatus getOutstanding() {
        return outstandings;
    }

    @JsonProperty("outstanding")
    public void setOutstanding(OutstandingStatus outstandings) {
        this.outstandings = outstandings;
    }

    @JsonProperty("plan")
    public Plan getPlan() {
        return plans;
    }

    @JsonProperty("plan")
    public void setPlan(Plan plans) {
        this.plans = plans;
    }

}