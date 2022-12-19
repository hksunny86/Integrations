package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "TOPUP_SCHEDULER_EXEC_SEQ",sequenceName = "TOPUP_SCHEDULER_EXEC_SEQ", allocationSize=1)
@Table(name = "TOPUP_SCHEDULER_EXEC")
public class TopupSchedulerExecModel extends BasePersistableModel implements Serializable {
  
	private static final long serialVersionUID = -6374777449655436946L;
	
	private Long topupSchedulerExecId;
	private Double amount;
	private String rrn;
	private Integer status;
	private String responseCode;
	private Date createdOn;
	private Date updatedOn;	
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private Long transactionCodeId;
	private Integer versionNo;

	public TopupSchedulerExecModel() {}

	public TopupSchedulerExecModel(Long topupSchedulerExecId) {
		
		this.setPrimaryKey(topupSchedulerExecId);
	}
	
    @Id 
    @Column(name = "TOPUP_SCHEDULER_EXEC_ID" , nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TOPUP_SCHEDULER_EXEC_SEQ")
	public Long getTopupSchedulerExecId() {
		return this.topupSchedulerExecId;
	}

	public void setTopupSchedulerExecId(Long topupSchedulerExecId) {
		this.topupSchedulerExecId = topupSchedulerExecId;
	}

	@Column(name = "AMOUNT" )
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name = "RRN" )
	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	
	@Column(name = "STATUS" )
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "CREATED_ON" )
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

    @Column(name = "UPDATED_ON" )
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY") 
	public AppUserModel getUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
		this.updatedByAppUserModel = updatedByAppUserModel;
	}
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY") 
	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}
	
    @Version 
    @Column(name = "VERSION_NO")
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	
	
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getTopupSchedulerExecId();
	}

	public void setPrimaryKey(Long primaryKey) {
		setTopupSchedulerExecId(primaryKey);
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		return "&topupSchedulerExecId=" + getTopupSchedulerExecId();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() { 
		return "topupSchedulerExecId";				
	}

	@Column(name = "RESPONSE_CODE" )
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Column(name = "TRANSACTION_CODE_ID" )
	public Long getTransactionCodeId() {
		return transactionCodeId;
	}

	public void setTransactionCodeId(Long transactionCode) {
		this.transactionCodeId = transactionCode;
	}	
}