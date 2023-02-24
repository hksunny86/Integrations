package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maturityDuration",
        "interest",
        "oneOffCharges",
        "recurringCharges"
})
public class MaturityDetails implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("maturityDuration")
    private String maturityDuration;
    @JsonProperty("interest")
    private List<Interest> interestList;
    @JsonProperty("oneOffCharges")
    private List<OneOffCharges> oneOffChargesList;
    @JsonProperty("recurringCharges")
    private List<RecurringCharges> recurringChargesList;

    public List<OneOffCharges> getOneOffChargesList() {
        return oneOffChargesList;
    }

    public void setOneOffChargesList(List<OneOffCharges> oneOffChargesList) {
        this.oneOffChargesList = oneOffChargesList;
    }

    public List<RecurringCharges> getRecurringChargesList() {
        return recurringChargesList;
    }

    public void setRecurringChargesList(List<RecurringCharges> recurringChargesList) {
        this.recurringChargesList = recurringChargesList;
    }

    public String getMaturityDuration() {
        return maturityDuration;
    }

    public void setMaturityDuration(String maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    public List<Interest> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interest> interestList) {
        this.interestList = interestList;
    }
}
