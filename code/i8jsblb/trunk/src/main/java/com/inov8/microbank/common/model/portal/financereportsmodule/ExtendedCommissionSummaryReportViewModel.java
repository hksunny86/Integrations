package com.inov8.microbank.common.model.portal.financereportsmodule;

import java.util.Date;

import javax.persistence.Transient;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 12, 2013 3:19:51 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ExtendedCommissionSummaryReportViewModel extends CommissionSummaryReportViewModel
{
    private static final long serialVersionUID = 5614751814818472218L;

    private Date startDate;
    private Date endDate;

    public ExtendedCommissionSummaryReportViewModel()
    {
    }

    @Transient
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

}
