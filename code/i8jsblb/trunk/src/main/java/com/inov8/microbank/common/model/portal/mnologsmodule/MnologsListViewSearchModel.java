package com.inov8.microbank.common.model.portal.mnologsmodule;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.io.Serializable;
import java.util.Date;


public class MnologsListViewSearchModel extends MnologsListViewModel implements Serializable
{
	private static final long serialVersionUID = 2086471482346930542L;

	private Date fromDate;
	private Date toDate;

	public MnologsListViewSearchModel() {
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
