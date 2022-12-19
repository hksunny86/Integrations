package com.inov8.microbank.common.model.portal.retailertransactionmodule;

import java.util.Date;

import javax.persistence.Transient;

/**
 * Created By : Naseer Ullah <br>
 * Creation Date : Jan 30, 2013 8:18:30 PM
 * <p>
 * Purpose :
 * <p>
 * Updated By : <br>
 * Updated Date : <br>
 * Comments : <br>
 */
public class ExtendedRetailerTransactionViewModel extends
		RetailerTransactionViewModel {
	private static final long serialVersionUID = -2994541278816718482L;

	private Date startDate;
	private Date endDate;

	private Long senderRegHierId;
	private Long receiverRegHierId;

	public ExtendedRetailerTransactionViewModel() {
	}

	@Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getSenderRegHierId() {
		return senderRegHierId;
	}

	public void setSenderRegHierId(Long senderRegHierId) {
		this.senderRegHierId = senderRegHierId;
	}

	public Long getReceiverRegHierId() {
		return receiverRegHierId;
	}

	public void setReceiverRegHierId(Long receiverRegHierId) {
		this.receiverRegHierId = receiverRegHierId;
	}

}
