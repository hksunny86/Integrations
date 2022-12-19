package com.inov8.microbank.tax.model;

import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by Malik on 8/10/2016.
 */
public class ExtendedWHTTrxViewModel extends WHTTrxViewModel
{
    private Date startDate;
    private Date endDate;

    @Transient
    public Date getStartDate()
    {
        return startDate;
    }


    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
    @Transient
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
}
