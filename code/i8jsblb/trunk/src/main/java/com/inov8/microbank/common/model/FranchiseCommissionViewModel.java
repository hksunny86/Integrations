package com.inov8.microbank.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommissionTransactionModel entity bean.
 *
 * @author  Mudassir Hanif
 * @version 1.0
 *
 *
 * @spring.bean name="FranchiseCommissionViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "FRANCHISE_COMMISSION_VIEW")
public class FranchiseCommissionViewModel extends BasePersistableModel implements Serializable{

	private static final long serialVersionUID = -7596619954265300644L;
	private Long commissionTransactionId;
	private Long agentId;
	private Long headAgentId;
	private Long smartMoneyAccountId;
	private Long supProcessingStatusId;
	private Double commissionAmount;
	private Boolean settled;
	private Boolean posted;
	private Long productId;
	private Long commissionStakeholderId;

	
	
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getCommissionTransactionId();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "commissionTransactionId";
		return primaryKeyFieldName;			
	}

	/**
	    * Helper method for Struts with displaytag
	    */
	   @javax.persistence.Transient
	   public String getPrimaryKeyParameter() {
	      String parameters = "";
	      parameters += "&commissionTransactionId=" + getCommissionTransactionId();
	      return parameters;
	   }
	   
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setCommissionTransactionId(primaryKey);
		
	}
	
	@Column(name = "COMMISSION_TRANSACTION_ID" , nullable = false )
	@Id 
	public Long getCommissionTransactionId() {
		return commissionTransactionId;
	}
	
	public void setCommissionTransactionId(Long commissionTransactionId) {
		this.commissionTransactionId = commissionTransactionId;
	}
	
	@Column(name = "AGENT_ID" , nullable = false )
	public Long getAgentId() {
		return agentId;
	}
	
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	@Column(name = "HEAD_AGENT_ID" , nullable = false )
	public Long getHeadAgentId() {
		return headAgentId;
	}
	public void setHeadAgentId(Long agentHeadId) {
		this.headAgentId = agentHeadId;
	}
	
	
	@Column(name = "FRANCHISE_ACCOUNT" , nullable = false )
	public Long getSmartMoneyAccountId() {
		return smartMoneyAccountId;
	}
	public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
		this.smartMoneyAccountId = smartMoneyAccountId;
	}
	
	
	@Column(name = "TRANSACTION_STATUS" , nullable = false )
	public Long getSupProcessingStatusId() {
		return supProcessingStatusId;
	}
	
	public void setSupProcessingStatusId(Long supProcessingStatusId) {
		this.supProcessingStatusId = supProcessingStatusId;
	}
	
	
	@Column(name = "COMMISSION_AMOUNT" , nullable = false )
	public Double getCommissionAmount() {
		return commissionAmount;
	}
	
	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	
	
	@Column(name = "IS_SETTLED" , nullable = false )
	public Boolean getSettled() {
		return settled;
	}
	
	public void setSettled(Boolean settled) {
		this.settled = settled;
	}
	
	
	@Column(name = "POSTED" , nullable = false )
	public Boolean getPosted() {
		return posted;
	}
	
	public void setPosted(Boolean posted) {
		this.posted = posted;
	}
	
	
	@Column(name = "PRODUCT_ID" , nullable = false )
	public Long getProductId() {
		return productId;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	
	@Column(name = "COMMISSION_STAKEHOLDER" , nullable = false )
	public Long getCommissionStakeholderId() {
		return commissionStakeholderId;
	}
	
	public void setCommissionStakeholderId(Long commissionStakeholderId) {
		this.commissionStakeholderId = commissionStakeholderId;
	}
	   
}
