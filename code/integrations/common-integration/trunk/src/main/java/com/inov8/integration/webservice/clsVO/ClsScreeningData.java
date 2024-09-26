package com.inov8.integration.webservice.clsVO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClsScreeningData implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String caseId;
    private String caseStatus;
    private String totalGWL;
    private String totalPEPEDD;
    private String totalPrivate;
    private String importStatus;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getTotalGWL() {
        return totalGWL;
    }

    public void setTotalGWL(String totalGWL) {
        this.totalGWL = totalGWL;
    }

    public String getTotalPEPEDD() {
        return totalPEPEDD;
    }

    public void setTotalPEPEDD(String totalPEPEDD) {
        this.totalPEPEDD = totalPEPEDD;
    }

    public String getTotalPrivate() {
        return totalPrivate;
    }

    public void setTotalPrivate(String totalPrivate) {
        this.totalPrivate = totalPrivate;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }
}
