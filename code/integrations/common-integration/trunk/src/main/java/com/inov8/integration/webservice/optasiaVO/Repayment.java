package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "repaymentsCount",
        "gross",
        "principal",
        "setupFees",
        "interest",
        "interestVAT",
        "charges",
        "chargesVAT",
})
public class Repayment implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


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

    public String getRepaymentsCount() {
        return repaymentsCount;
    }

    public void setRepaymentsCount(String repaymentsCount) {
        this.repaymentsCount = repaymentsCount;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getSetupFees() {
        return setupFees;
    }

    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestVAT() {
        return interestVAT;
    }

    public void setInterestVAT(String interestVAT) {
        this.interestVAT = interestVAT;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getChargesVAT() {
        return chargesVAT;
    }

    public void setChargesVAT(String chargesVAT) {
        this.chargesVAT = chargesVAT;
    }
}
