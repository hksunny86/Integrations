package com.inov8.microbank.common.model.portal.blbreports;

import java.beans.Transient;
import java.util.Date;

public class ExtendedBdeKpiReportModel extends BdeKpiReportViewModel{

    private Date startDate;
    private Date endDate;

    @Transient
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
