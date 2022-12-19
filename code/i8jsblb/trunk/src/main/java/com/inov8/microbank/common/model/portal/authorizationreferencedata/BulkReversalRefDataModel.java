package com.inov8.microbank.common.model.portal.authorizationreferencedata;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name="bulkReversalRefDataModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class BulkReversalRefDataModel implements Serializable {

	private static final long serialVersionUID = -1L;

	private Long batchId;
	private Integer validRecordsCount;
//	private String comments;
//	private Long bulkAdjustmentId;
	private String trxnId;
//	private Long adjustmentType;
//	private String fromAccount;
//	private String fromAccountTitle;
//	private String toAccount;
//	private String toAccountTitle;
//	private Double amount;
	private Boolean isProcessed;
	private String errorDescription;
	private Boolean isApproved;
	private String srNo;
//	private String balance;
	private String authorizerId;
	private Long actionAuthorizationId;
	private Long createdBy;
	private Date createdOn;
	
	public Long getBatchID() {
		return batchId;
	}

	public void setBatchID(Long batchId) {
		this.batchId = batchId;
	}

	public Long getActionAuthorizationId() {
		return actionAuthorizationId;
	}

	public void setActionAuthorizationId(Long actionAuthorizationId) {
		this.actionAuthorizationId = actionAuthorizationId;
	}

	public Integer getValidRecordsCount() {
		return validRecordsCount;
	}

	public void setValidRecordsCount(Integer validRecordsCount) {
		this.validRecordsCount = validRecordsCount;
	}

	public String getTrxnId() {
		return trxnId;
	}

	public void setTrxnId(String trxnId) {
		this.trxnId = trxnId;
	}

	public Boolean getApproved() {
		return isApproved;
	}

	public void setApproved(Boolean approved) {
		isApproved = approved;
	}

	public String getSrNo() {
		return srNo;
	}

	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}

	public String getAuthorizerId() {
		return authorizerId;
	}

	public void setAuthorizerId(String authorizerId) {
		this.authorizerId = authorizerId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public Boolean getProcessed() {
		return isProcessed;
	}

	public void setProcessed(Boolean processed) {
		isProcessed = processed;
	}
}