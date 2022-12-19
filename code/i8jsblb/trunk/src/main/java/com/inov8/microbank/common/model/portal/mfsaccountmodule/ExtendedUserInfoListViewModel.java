package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import java.util.Date;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Nov 6, 2012 6:09:09 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
 
public class ExtendedUserInfoListViewModel extends UserInfoListViewModel
{
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = -398373739217153779L;

    private Date startDate;
	private Date endDate;

    @javax.persistence.Transient
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @javax.persistence.Transient
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }
}
