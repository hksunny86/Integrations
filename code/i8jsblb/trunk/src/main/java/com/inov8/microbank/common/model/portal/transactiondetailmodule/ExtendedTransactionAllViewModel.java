package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author naseer
 */
@XmlRootElement(name="extendedTransactionAllViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtendedTransactionAllViewModel extends TransactionAllViewModel {

	private static final long serialVersionUID = -1107430786755902357L;

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
