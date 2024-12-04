package com.inov8.integration.webservice.euronetVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inov8.integration.webservice.jazzVO.Status;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InAuditDS implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;
    private String application;
    private String userId;
    private String organization;
    private String serviceID;
    private String sequence;
    private String sourceDate;
    private String sourceTime;
    private String transmissionDate;
    private String transmissionTime;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public String getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(String sourceTime) {
        this.sourceTime = sourceTime;
    }

    public String getTransmissionDate() {
        return transmissionDate;
    }

    public void setTransmissionDate(String transmissionDate) {
        this.transmissionDate = transmissionDate;
    }

    public String getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(String transmissionTime) {
        this.transmissionTime = transmissionTime;
    }
}
