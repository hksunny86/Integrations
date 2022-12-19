package com.inov8.microbank.common.model.portal.financereportsmodule;

import java.util.Date;

import javax.persistence.Transient;

/** 
 * Created By    : Hassan javaid <br>
 * Creation Date : 30 DEC, 2014 3:11:03 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ExtendedCustomerClosingBalanceViewModel extends CustomerClosingBalanceViewModel
{
    private static final long serialVersionUID = -1754790881034523044L;

    private Date startDate;
    private Date endDate;

    public ExtendedCustomerClosingBalanceViewModel()
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

