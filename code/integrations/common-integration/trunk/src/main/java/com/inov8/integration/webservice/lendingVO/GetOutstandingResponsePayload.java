package com.inov8.integration.webservice.lendingVO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetOutstandingResponsePayload implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String totalOutstanding;
    private String principalOutstanding;
    private String markupOutstanding;
    private String lpChargesOutstanding;
    private String epChargesOutstanding;

    public String getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(String totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public String getPrincipalOutstanding() {
        return principalOutstanding;
    }

    public void setPrincipalOutstanding(String principalOutstanding) {
        this.principalOutstanding = principalOutstanding;
    }

    public String getMarkupOutstanding() {
        return markupOutstanding;
    }

    public void setMarkupOutstanding(String markupOutstanding) {
        this.markupOutstanding = markupOutstanding;
    }

    public String getLpChargesOutstanding() {
        return lpChargesOutstanding;
    }

    public void setLpChargesOutstanding(String lpChargesOutstanding) {
        this.lpChargesOutstanding = lpChargesOutstanding;
    }

    public String getEpChargesOutstanding() {
        return epChargesOutstanding;
    }

    public void setEpChargesOutstanding(String epChargesOutstanding) {
        this.epChargesOutstanding = epChargesOutstanding;
    }
}
