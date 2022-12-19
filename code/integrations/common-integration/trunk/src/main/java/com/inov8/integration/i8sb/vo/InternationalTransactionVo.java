package com.inov8.integration.i8sb.vo;
import java.io.Serializable;

/**
 * Created by Administrator on 11/14/2019.
 */
public class InternationalTransactionVo implements Serializable {

    private String relationshipId;
    private String customerId;
    private String status;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String internationalTransWindowId;
    private String agentId;


    public String getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInternationalTransWindowId() {
        return internationalTransWindowId;
    }

    public void setInternationalTransWindowId(String internationalTransWindowId) {
        this.internationalTransWindowId = internationalTransWindowId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

}
