package com.inov8.microbank.common.model.portal.financereportsmodule;

import java.util.Date;

import javax.persistence.Transient;

public class ExtendedCustomerBalanceReportSummaryModel extends CustomerBalanceReportSummaryModel {
	
		private static final long serialVersionUID = -4537537174988292930L;
		private Date startDate;
	    private Date endDate;

	    public ExtendedCustomerBalanceReportSummaryModel()
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
