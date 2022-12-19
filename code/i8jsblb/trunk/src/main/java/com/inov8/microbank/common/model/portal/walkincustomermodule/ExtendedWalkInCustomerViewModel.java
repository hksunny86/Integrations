package com.inov8.microbank.common.model.portal.walkincustomermodule;

import javax.persistence.Transient;
import java.util.Date;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 2, 2012 2:57:06 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ExtendedWalkInCustomerViewModel extends WalkInCustomerViewModel
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3492415668038546310L;

    private Date startDate;
    private Date endDate;
    private Double creditLimit;
    private Double debitLimit;

    public ExtendedWalkInCustomerViewModel()
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

    @Transient
    public Double getCreditLimit()
    {
        return creditLimit;
    }

    public void setCreditLimit( Double creditLimit )
    {
        this.creditLimit = creditLimit;
    }

    @Transient
    public Double getDebitLimit()
    {
        return debitLimit;
    }

    public void setDebitLimit( Double debitLimit )
    {
        this.debitLimit = debitLimit;
    }

}
