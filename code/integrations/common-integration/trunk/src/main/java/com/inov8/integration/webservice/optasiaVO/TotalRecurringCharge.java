package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "charge",
        "chargeVAT"
})
public class TotalRecurringCharge implements Serializable{

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("charge")
    private Float charge;
    @JsonProperty("chargeVAT")
    private Float chargeVAT;

    @JsonProperty("chargeName")
    public String getChargeName() {
        return chargeName;
    }

    @JsonProperty("chargeName")
    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @JsonProperty("charge")
    public Float getCharge() {
        return charge;
    }

    @JsonProperty("charge")
    public void setCharge(Float charge) {
        this.charge = charge;
    }

    @JsonProperty("chargeVAT")
    public Float getChargeVAT() {
        return chargeVAT;
    }

    @JsonProperty("chargeVAT")
    public void setChargeVAT(Float chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

}
