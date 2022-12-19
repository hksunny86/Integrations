package com.inov8.microbank.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "IBFT_RETRY_ADVICE_LIST_VIEW")
public class IBFTRetryAdviceListViewModel extends BasePersistableModel {

	private Long ibftRetryAdviceId;
   	private String mobileNo; //AccountNo2
   	private String accountNo; //getAccountNo1
   	private Double transactionAmount;
   	private Date requestTime;
	private String stan;
	private String retrievalReferenceNumber;
	private String status;
   	
   	private String transactionCode;
   	
   	private Long createdBy;
	private Long updatedBy;
	private Date updatedOn;
	private Date createdOn;
	
   	// For report criteria
	private Date requestTimeStart;
   	private Date requestTimeEnd;
	
	
	@Id
	@Column(name = "IBFT_RETRY_ADVICE_ID")
	public Long getIbftRetryAdviceId() {
		return ibftRetryAdviceId;
	}

	public void setIbftRetryAdviceId(Long ibftRetryAdviceId) {
		this.ibftRetryAdviceId = ibftRetryAdviceId;
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getIbftRetryAdviceId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "ibftRetryAdviceId";
		return primaryKeyFieldName;
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		return "&ibftRetryAdviceId=" + getIbftRetryAdviceId();
	}

	@Override
	public void setPrimaryKey(Long ibftRetryAdviceId) {
		setIbftRetryAdviceId(ibftRetryAdviceId);
	}
	
    @Column(name = "MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

    @Column(name = "ACCOUNT_NO")
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

    @Column(name = "TRANSACTION_AMOUNT")
	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

    @Column(name = "REQUEST_TIME")
	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

    @Column(name = "STAN")
    public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

    @Column(name = "RRN")
	public String getRetrievalReferenceNumber() {
		return retrievalReferenceNumber;
	}

	public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
		this.retrievalReferenceNumber = retrievalReferenceNumber;
	}

    @Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    @Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

    @Column(name = "CREATED_BY")
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

    @Column(name = "UPDATED_BY")
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

    @Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

    @Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Transient
	public Date getRequestTimeStart() {
		return requestTimeStart;
	}

	public void setRequestTimeStart(Date requestTimeStart) {
		this.requestTimeStart = requestTimeStart;
	}

	@Transient
	public Date getRequestTimeEnd() {
		return requestTimeEnd;
	}

	public void setRequestTimeEnd(Date requestTimeEnd) {
		this.requestTimeEnd = requestTimeEnd;
	}
	
	
	
}