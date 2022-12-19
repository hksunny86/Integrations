package com.inov8.microbank.fonepay.common;

import java.io.Serializable;
import java.util.Date;

import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;

public class FonePayListViewModel extends FonePayLogModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7256959533508353609L;

	
	private Date fromDate;
	private Date toDate;

	public FonePayListViewModel() {
		// TODO Auto-generated constructor stub
	}

	public Date getFromDate()
	{
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
}
