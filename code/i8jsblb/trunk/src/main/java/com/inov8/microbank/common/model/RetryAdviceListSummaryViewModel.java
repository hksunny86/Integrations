package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "RETRY_ADVICE_LIST_SUMMARY_VIEW")
public class RetryAdviceListSummaryViewModel extends BasePersistableModel implements
		Serializable {

	private Long middlewareRetryAdvRprtId;
	private Long transactionCodeId;
	private String transactionCode;
	private Long productId;
	private String productName;
	private Long intgTransactionTypeId;
	private String fromAccount;
	private String toAccount;
	private Double transactionAmount;
	private Date transmissionTime;
	private Date requestTime;
	private String stan;
	private String reversalStan;
	private String reversalRequestTime;
	private String responseCode;
	private String status;
	private Date createdOn;
	private Date updatedOn;
	private String adviceType;
	private String cnicNo;
	private String consumerNo;

	private Date reversalRequestDateTime;

	@Id
	@Column(name = "MIDDLEWARE_RETRY_ADV_RPRT_ID")
	public Long getMiddlewareRetryAdvRprtId() {
		return middlewareRetryAdvRprtId;
	}

	public void setMiddlewareRetryAdvRprtId(Long middlewareRetryAdvRprtId) {
		this.middlewareRetryAdvRprtId = middlewareRetryAdvRprtId;
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getMiddlewareRetryAdvRprtId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "middlewareRetryAdvRprtId";
		return primaryKeyFieldName;
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&middlewareRetryAdvRprtId="
				+ getMiddlewareRetryAdvRprtId();
		return parameters;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		setMiddlewareRetryAdvRprtId(arg0);
	}

	@Column(name = "TRANSACTION_CODE_ID")
	public Long getTransactionCodeId() {
		return transactionCodeId;
	}

	public void setTransactionCodeId(Long transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}

	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "INTG_TRANSACTION_TYPE_ID")
	public Long getIntgTransactionTypeId() {
		return intgTransactionTypeId;
	}

	public void setIntgTransactionTypeId(Long intgTransactionTypeId) {
		this.intgTransactionTypeId = intgTransactionTypeId;
	}

	@Column(name = "FROM_ACCOUNT")
	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	@Column(name = "TO_ACCOUNT")
	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	@Column(name = "TRANSACTION_AMOUNT")
	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@Column(name = "TRANSMISSION_TIME")
	public Date getTransmissionTime() {
		return transmissionTime;
	}

	public void setTransmissionTime(Date transmissionTime) {
		this.transmissionTime = transmissionTime;
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

	@Column(name = "REVERSAL_STAN")
	public String getReversalStan() {
		return reversalStan;
	}

	public void setReversalStan(String reversalStan) {
		this.reversalStan = reversalStan;
	}

	@Column(name = "REVERSAL_REQUEST_TIME")
	public String getReversalRequestTime() {
		return reversalRequestTime;
	}

	public void setReversalRequestTime(String reversalRequestTime) {
		this.reversalRequestTime = reversalRequestTime;
	}

	@Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "ADVICE_TYPE")
	public String getAdviceType() {
		return adviceType;
	}

	public void setAdviceType(String adviceType) {
		this.adviceType = adviceType;
	}

	@Transient
	public Date getReversalRequestDateTime() {
		if(intgTransactionTypeId.longValue() == IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE_REVERSAL.longValue()){
			return requestTime;
		}else{
			return null;
		}
	}

	@Column(name = "CNIC")
	public String getCnicNo() { return cnicNo; }

	public void setCnicNo(String cnicNo) { this.cnicNo = cnicNo; }

	@Column(name = "CUSTOMER_MOBILE_NO")
	public String getConsumerNo() { return consumerNo; }

	public void setConsumerNo(String consumerNo) { this.consumerNo = consumerNo; }
}