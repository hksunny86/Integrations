package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "is.passive.advance",
        "is.passive.recovery",
        "pending.operation.id",
        "advance.failed.failure.type"
})

public class EventTypeDetails implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("is.passive.advance")
    private Boolean isPassiveAdvance;
    @JsonProperty("is.passive.recovery")
    private Boolean isPassiveRecovery;
    @JsonProperty("pending.operation.id")
    private Integer pendingOperationId;
    @JsonProperty("advance.failed.failure.type")
    private String advanceFailedFailureType;

    @JsonProperty("is.passive.advance")
    public Boolean getIsPassiveAdvance() {
        return isPassiveAdvance;
    }

    @JsonProperty("is.passive.advance")
    public void setIsPassiveAdvance(Boolean isPassiveAdvance) {
        this.isPassiveAdvance = isPassiveAdvance;
    }


    @JsonProperty("pending.operation.id")
    public Integer getPendingOperationId() {
        return pendingOperationId;
    }

    @JsonProperty("pending.operation.id")
    public void setPendingOperationId(Integer pendingOperationId) {
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

}
