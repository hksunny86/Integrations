package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "chargeName",
        "chargeType",
        "chargeValue",
        "chargeVAT",
        "daysOffset"
})
public class OneOffCharges implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("chargeName")
    private String chargeName;
    @JsonProperty("chargeType")
    private String chargeType;
    @JsonProperty("chargeValue")
    private String chargeValue;
    @JsonProperty("chargeVAT")
    private String chargeVAT;
    @JsonProperty("daysOffset")
    private String daysOffset;

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    public String getChargeVAT() {
        return chargeVAT;
    }

    public void setChargeVAT(String chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

    public String getDaysOffset() {
        return daysOffset;
    }

    public void setDaysOffset(String daysOffset) {
        this.daysOffset = daysOffset;
    }
}
