package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "chargeAmount",
        "chargeVAT"
})
public class TotalOneOffCharges implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeAmount")
    private String chargeAmount;
    @JsonProperty("chargeVAT")
    private String chargeVAT;

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getChargeVAT() {
        return chargeVAT;
    }

    public void setChargeVAT(String chargeVAT) {
        this.chargeVAT = chargeVAT;
    }
}
