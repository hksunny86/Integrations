package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "repayment"
})
public class Report implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("repayment")
    private List<Repayment> repaymentList;

    public List<Repayment> getRepaymentList() {
        return repaymentList;
    }

    public void setRepaymentList(List<Repayment> repaymentList) {
        this.repaymentList = repaymentList;
    }
}
