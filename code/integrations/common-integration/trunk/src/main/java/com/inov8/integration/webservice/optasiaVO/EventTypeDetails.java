package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "charge.calculation.name",
        "is.passive.recovery",
        "recovery.passive.auto.recovered",
        "is.passive.advance",
        "pending.operation.id",
        "advance.failed.failure.type"
})

public class EventTypeDetails implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("charge.calculation.name")
    private String chargeCalculationName;
    @JsonProperty("is.passive.recovery")
    private Boolean isPassiveRecovery;
    @JsonProperty("recovery.passive.auto.recovered")
    private Boolean recoveryPassiveAutoRecovered;
    @JsonProperty("is.passive.advance")
    private Boolean isPassiveAdvance;
    @JsonProperty("pending.operation.id")
    private String pendingOperationId;
    @JsonProperty("advance.failed.failure.type")
    private String advanceFailedFailureType;

    @JsonProperty("charge.calculation.name")
    public String getChargeCalculationName() {
        return chargeCalculationName;
    }

    @JsonProperty("charge.calculation.name")
    public void setChargeCalculationName(String chargeCalculationName) {
        this.chargeCalculationName = chargeCalculationName;
    }

    @JsonProperty("is.passive.advance")
    public Boolean getIsPassiveAdvance() {
        return isPassiveAdvance;
    }

    @JsonProperty("is.passive.advance")
    public void setIsPassiveAdvance(Boolean isPassiveAdvance) {
        this.isPassiveAdvance = isPassiveAdvance;
    }

    @JsonProperty("recovery.passive.auto.recovered")
    public Boolean getRecoveryPassiveAutoRecovered() {
        return recoveryPassiveAutoRecovered;
    }

    @JsonProperty("recovery.passive.auto.recovered")
    public void setRecoveryPassiveAutoRecovered(Boolean recoveryPassiveAutoRecovered) {
        this.recoveryPassiveAutoRecovered = recoveryPassiveAutoRecovered;
    }

    @JsonProperty("pending.operation.id")
    public String getPendingOperationId() {
        return pendingOperationId;
    }

    @JsonProperty("pending.operation.id")
    public void setPendingOperationId(String pendingOperationId) {
        this.pendingOperationId = pendingOperationId;
    }

    @JsonProperty("advance.failed.failure.type")
    public String getAdvanceFailedFailureType() {
        return advanceFailedFailureType;
    }

    @JsonProperty("advance.failed.failure.type")
    public void setAdvanceFailedFailureType(String advanceFailedFailureType) {
        this.advanceFailedFailureType = advanceFailedFailureType;
    }

    @JsonProperty("is.passive.recovery")
    public Boolean getIsPassiveRecovery() {
        return isPassiveRecovery;
    }

    @JsonProperty("is.passive.recovery")
    public void setIsPassiveRecovery(Boolean isPassiveRecovery) {
        this.isPassiveRecovery = isPassiveRecovery;
    }
}
