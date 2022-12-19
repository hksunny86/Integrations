package com.inov8.microbank.common.model.portal.financereportsmodule;

import java.util.Date;

import javax.persistence.Transient;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 11, 2013 3:11:03 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ExtendedAgentClosingBalanceViewModel extends AgentClosingBalanceViewModel
{
    private static final long serialVersionUID = -1754790881034523044L;

    private Date startDate;
    private Date endDate;

    public ExtendedAgentClosingBalanceViewModel()
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
