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
public class TotalOneOffCharges implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("charge")
    private String charge;
    @JsonProperty("chargeVAT")
    private String chargeVAT;

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getChargeVAT() {
        return chargeVAT;
    }

    public void setChargeVAT(String chargeVAT) {
        this.chargeVAT = chargeVAT;
    }
}
