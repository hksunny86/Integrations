package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
@XmlRootElement(name="extendedCashTransactionViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class ExtendedCashTransactionViewModel extends CashTransactionViewModel {

	private static final long serialVersionUID = -1L;

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
