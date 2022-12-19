package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import java.util.Date;

/**
 * The ExtendedTransactionCashReversalViewModel entity bean.
 *
 * @author  Naseer Ullah
 * @since $Date: 2012/07/18 20:00:00 $
 */
public class ExtendedTransactionCashReversalViewModel extends TransactionCashReversalViewModel {

	private static final long serialVersionUID = -5234064714984844821L;

	private Date startDate;
	private Date endDate;

	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
