package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;

@XmlRootElement(name="cashTransactionViewModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CASH_TRANSACTION_VIEW")
public class CashTransactionViewModel extends BasePersistableModel implements java.io.Serializable {

	private static final long serialVersionUID = -1L;

	private Long transactionId;
	private Long transactionCodeId;
	private String transactionCode;
	private Date createdOn;
	private Long productId;
	private String productName;
	private Double transactionAmount;
	private Double totalAmount;
	private Double serviceChargesExclusive;
	private Long supProcessingStatusId;
	private String processingStatusName;
	private String saleMobileNo;
	private String senderCnic;
	private String recipientMobileNo;
	private String recipientCnic;
	private String agent1Id;
	private Boolean updateP2PFlag;
	private String updateP2PFlagString;
	private Long reversedByAppUserId;
	private String reversedByName;
	private Date reversedDate;
	private String reversedComments;
	private Boolean isfullReversal;
	private Integer redemptionType;
	
	public CashTransactionViewModel() {}

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getTransactionId();
	}

    @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		return "transactionId";
	}

	@Override
	@javax.persistence.Transient
	public void setPrimaryKey( Long primaryKey ) {
		setTransactionId( primaryKey );
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "&transactionId=" + getTransactionId();
		return parameters;
	}

	@Id
	@Column(name = "TRANSACTION_ID")
	public Long getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "TRANSACTION_CODE_ID")
	public Long getTransactionCodeId() {
    	return transactionCodeId;
	}

    public void setTransactionCodeId(Long transactionCodeId) {
       this.transactionCodeId = transactionCodeId;
    }

	@Column(name = "SALE_MOBILE_NO")
	public String getSaleMobileNo() {
		return this.saleMobileNo;
	}

	public void setSaleMobileNo(String saleMobileNo) {
		this.saleMobileNo = saleMobileNo;
	}

	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return this.transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "PRODUCT_NAME")
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "TRANSACTION_AMOUNT")
	public Double getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@Column(name = "TOTAL_AMOUNT")
	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "SUP_PROCESSING_STATUS_ID")
	public Long getSupProcessingStatusId() {
		return this.supProcessingStatusId;
	}

	public void setSupProcessingStatusId(Long supProcessingStatusId) {
		this.supProcessingStatusId = supProcessingStatusId;
	}

	@Column(name = "PROCESSING_STATUS_NAME")
	public String getProcessingStatusName() {
		return this.processingStatusName;
	}

	public void setProcessingStatusName(String processingStatusName) {
		this.processingStatusName = processingStatusName;
	}

    @Column(name = "AGENT1_ID")
	public String getAgent1Id() {
		return this.agent1Id;
	}

	public void setAgent1Id(String agent1Id) {
		this.agent1Id = agent1Id;
	}

	@Column(name = "SENDER_CNIC")
	public String getSenderCnic() {
		return this.senderCnic;
	}

	public void setSenderCnic(String senderCnic) {
		this.senderCnic = senderCnic;
	}

	@Column(name = "RECIPIENT_MOBILE_NO")
	public String getRecipientMobileNo() {
		return this.recipientMobileNo;
	}

	public void setRecipientMobileNo(String recipientMobileNo) {
		this.recipientMobileNo = recipientMobileNo;
	}

	@Column(name = "RECIPIENT_CNIC")
	public String getRecipientCnic() {
		return this.recipientCnic;
	}

	public void setRecipientCnic(String recipientCnic) {
		this.recipientCnic = recipientCnic;
	}

	@Column(name = "SERVICE_CHARGES_EXCLUSIVE")
	public Double getServiceChargesExclusive() {
		return this.serviceChargesExclusive;
	}

	public void setServiceChargesExclusive(Double serviceChargesExclusive) {
		this.serviceChargesExclusive = serviceChargesExclusive;
	}

	@Column(name = "UPDATE_P2P_FLAG")
	public Boolean getUpdateP2PFlag() {
		return updateP2PFlag;
	}

	public void setUpdateP2PFlag(Boolean updateP2PFlag) {
		this.updateP2PFlag = updateP2PFlag;
	}

	@Column(name = "UPDATE_P2P_FLAG_STRING")
    public String getUpdateP2PFlagString(){
        return updateP2PFlagString;
    }

	public void setUpdateP2PFlagString(String updateP2PFlagString) {
      this.updateP2PFlagString = updateP2PFlagString;
	}

	@Transient
	public Boolean getReversable() {
		boolean reversable = false;
		
		if( productId != null && supProcessingStatusId != null){
			if(productId.longValue() == ProductConstantsInterface.CASH_TRANSFER){
				if((supProcessingStatusId.longValue() == SupplierProcessingStatusConstants.UNCLAIMED 
						|| supProcessingStatusId.longValue() == SupplierProcessingStatusConstants.IN_PROGRESS) 
						&& (updateP2PFlag == null || !updateP2PFlag)){
					reversable = true;
				}
			}else{ // A2P
				if(supProcessingStatusId.longValue() == SupplierProcessingStatusConstants.UNCLAIMED){
					reversable = true;
				}
			}
		}
		return reversable;
	}

	@Column(name = "REVERSED_BY_APP_USER_ID")
	public Long getReversedByAppUserId() {
		return reversedByAppUserId;
	}

	public void setReversedByAppUserId(Long reversedByAppUserId) {
		this.reversedByAppUserId = reversedByAppUserId;
	}

	@Column(name = "REVERSED_BY_NAME")
	public String getReversedByName() {
		return reversedByName;
	}

	public void setReversedByName(String reversedByName) {
		this.reversedByName = reversedByName;
	}

	@Column(name = "REVERSED_ON")
	public Date getReversedDate() {
		return reversedDate;
	}

	public void setReversedDate(Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	@Column(name = "REVERSED_COMMENTS")
	public String getReversedComments() {
		return reversedComments;
	}

	public void setReversedComments(String reversedComments) {
		this.reversedComments = reversedComments;
	}

	@Column(name = "FULL_REVERSAL")
	public Boolean getIsfullReversal() {
		return isfullReversal;
	}

	public void setIsfullReversal(Boolean isfullReversal) {
		this.isfullReversal = isfullReversal;
	}
	
	@Column(name = "REDEMPTION_TYPE")
	public Integer getRedemptionType() {
		return redemptionType;
	}

	public void setRedemptionType(Integer redemptionType) {
		this.redemptionType = redemptionType;
	}

}