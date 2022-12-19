package com.inov8.microbank.common.model.portal.authorizationreferencedata;


public class ManualAdjustmentRefDataModel  {
	
	private Long manualAdjustmentID;
	private String transactionCodeId;
	private Long adjustmentType; 
	private String fromACNo;
	private String fromACNick;
	private String toACNo;
	private String toACNick;
	private Double amount;
	private String comments;
	
	public Long getManualAdjustmentID() {
		return manualAdjustmentID;
	}

	public void setManualAdjustmentID(Long manualAdjustmentID) {
		this.manualAdjustmentID = manualAdjustmentID;
	}
	
	public String getTransactionCodeId() {
		return transactionCodeId;
	}

	public void setTransactionCodeId(String transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}
    
	public Long getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(Long adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public String getFromACNo() {
		return fromACNo;
	}
	public void setFromACNo(String fromACNo) {
		this.fromACNo = fromACNo;
	}
	
	public String getFromACNick() {
		return fromACNick;
	}
	public void setFromACNick(String fromACNick) {
		this.fromACNick = fromACNick;
	}
	
	public String getToACNo() {
		return toACNo;
	}
	public void setToACNo(String toACNo) {
		this.toACNo = toACNo;
	}
	
	public String getToACNick() {
		return toACNick;
	}
	public void setToACNick(String toACNick) {
		this.toACNick = toACNick;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

}