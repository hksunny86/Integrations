package com.inov8.microbank.common.model.postedrransactionreportmodule;

import java.util.Date;


/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 22, 2012 6:55:40 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ExtendedPostedTransactionViewModel extends PostedTransactionViewModel
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2246985956420016603L;

    private Date startDate;
    private Date endDate;

    public Date getEndDate()
    {
        return endDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

}
