package com.inov8.microbank.common.model.portal.inovtransactiondetailmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "MINI_STATEMENT_LIST_VIEW")
public class MiniStatementListViewModel extends BasePersistableModel implements Serializable {

   private static final long serialVersionUID = -4350194068949610720L;
   private Long transactionCodeId;
   private String mfsId;
   private String transactionCode;
   private Date createdOn;
   private String productName;
   private Double transactionAmount;
   private Double totalAmount;
   private Boolean isIssue;
   private String processingStatusName;
   private Long processingStatusId;
   private String agent1Id;
   private String agent2Id;
   private String recipientMfsId;
   private Double inclusiveCharges;
   private Double exclusiveCharges;
   private Long userType;
   private Long productId;
   private Double agentCommission;
   private Double agent2Commission;
	//Added by Sheheryaar
	private Long paymentModeId;
	private String senderAccountNick;
	private String recipientAccountNick;
   
	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionCodeId(primaryKey);
    }



   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getTransactionCodeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }

	public void setTransactionCodeId(Long transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}
	
	@Id
	@Column(name = "TRANSACTION_CODE_ID")
	public Long getTransactionCodeId() {
		return transactionCodeId;
	}
	@Column(name = "MFS_ID")
	public String getMfsId() {
		return mfsId;
	}
	
	public void setMfsId(String mfsId) {
		this.mfsId = mfsId;
	}
	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}
	
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	@Column(name = "PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Column(name = "TRANSACTION_AMOUNT")
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	@Column(name = "TOTAL_AMOUNT")
	public Double getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Column(name = "IS_ISSUE")
	public Boolean getIsIssue() {
		return isIssue;
	}
	
	public void setIsIssue(Boolean isIssue) {
		this.isIssue = isIssue;
	}
	@Column(name = "PROCESSING_STATUS_NAME")
	public String getProcessingStatusName() {
		return processingStatusName;
	}
	
	public void setProcessingStatusName(String processingStatusName) {
		this.processingStatusName = processingStatusName;
	}
	@Column(name = "SUP_PROCESSING_STATUS_ID")
	public Long getProcessingStatusId() {
		return processingStatusId;
	}
	
	public void setProcessingStatusId(Long processingStatusId) {
		this.processingStatusId = processingStatusId;
	}
	@Column(name = "AGENT1_ID")
	public String getAgent1Id() {
		return agent1Id;
	}
	
	public void setAgent1Id(String agent1Id) {
		this.agent1Id = agent1Id;
	}
	@Column(name = "AGENT2_ID")
	public String getAgent2Id() {
		return agent2Id;
	}
	
	public void setAgent2Id(String agent2Id) {
		this.agent2Id = agent2Id;
	}
	@Column(name = "RECIPIENT_MFS_ID")
	public String getRecipientMfsId() {
		return recipientMfsId;
	}
	
	public void setRecipientMfsId(String recipientMfsId) {
		this.recipientMfsId = recipientMfsId;
	}
	@Column(name = "SERVICE_CHARGES_INCLUSIVE")
	public Double getInclusiveCharges() {
		return inclusiveCharges;
	}
	
	public void setInclusiveCharges(Double inclusiveCharges) {
		this.inclusiveCharges = inclusiveCharges;
	}
	@Column(name = "SERVICE_CHARGES_EXCLUSIVE")
	public Double getExclusiveCharges() {
		return exclusiveCharges;
	}
	
	public void setExclusiveCharges(Double exclusiveCharges) {
		this.exclusiveCharges = exclusiveCharges;
	}

    @Column(name="TO_AGENT1")
	public Double getAgentCommission() {
		return agentCommission;
	}

	public void setAgentCommission(Double agentCommission) {
		this.agentCommission = agentCommission;
	}

	@Column(name="TO_AGENT2")
	public Double getAgent2Commission() {
        return agent2Commission;
    }
	
	public void setAgent2Commission(Double agent2Commission) {
        this.agent2Commission = agent2Commission;
    }
	
	@Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}
		
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@javax.persistence.Transient
    public Long getUserType() {
		return userType;
	}
	
	public void setUserType(Long userType) {
		this.userType = userType;
	}

	@Column(name="PAYMENT_MODE")
	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	@Column(name="SENDER_ACCOUNT_NICK")
	public String getSenderAccountNick() {
		return senderAccountNick;
	}

	public void setSenderAccountNick(String senderAccountNick) {
		this.senderAccountNick = senderAccountNick;
	}

	@Column(name="RECIPIENT_ACCOUNT_NICK")
	public String getRecipientAccountNick() {
		return recipientAccountNick;
	}

	public void setRecipientAccountNick(String recipientAccountNick) {
		this.recipientAccountNick = recipientAccountNick;
	}
}