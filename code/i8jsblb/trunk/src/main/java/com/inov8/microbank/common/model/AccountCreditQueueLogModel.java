package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="ACCOUNT_CR_QUEUE_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="ACCOUNT_CR_QUEUE_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "ACCOUNT_CR_QUEUE_SEQ",sequenceName = "ACCOUNT_CR_QUEUE_SEQ")
@Table(name = "ACCOUNT_QUEUE_CR")
public class AccountCreditQueueLogModel extends BasePersistableModel implements Serializable{

   private static final long serialVersionUID = -3183432472310526991L;

   private Long accountCreditQueueLogId;
   private String toBankAccountNumber;
   private String transactionCode;
   private String responseCode;
   private String description;
   private Long customerAccountTypeId;
   private Double balance;
   private String authCode;
   private Long transactionTypeId;
   private Long ledgerId;
   private Date transactionDateTime;
   private Double balanceAfterTransaction;
   private Long receivingAccountId;
   private Long reasonId;
   private Boolean isCreditPush = Boolean.TRUE;
   private String status;
   private Date createdOn;
   private Date updatedOn;  	
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;	
   private Integer versionNo;

   public static final String FAILED = "Failed";
   public static final String PROCESSING = "Processing";
   public static final String SUCCESSFUL = "Successful";
   
   
   
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAccountCreditQueueLogId();
   }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setAccountCreditQueueLogId(primaryKey);
   }

   @Column(name = "ACCOUNT_CR_QUEUE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCOUNT_CR_QUEUE_SEQ")
   public Long getAccountCreditQueueLogId() {
      return accountCreditQueueLogId;
   }

   public void setAccountCreditQueueLogId(Long accountCreditQueueLogId) {
      this.accountCreditQueueLogId = accountCreditQueueLogId;
   }




   @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID")
	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}

	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}


   @Column(name = "BALANCE")
	public Double getBalance()
	{
		return balance;
	}
	
	public void setBalance(Double balance)
	{
		this.balance = balance;
	}
   @Column(name = "AUTH_CODE")
	public String getAuthCode()
	{
		return authCode;
	}
	
	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}
   @Column(name = "TRANSACTION_TYPE_ID")
	public Long getTransactionTypeId()
	{
		return transactionTypeId;
	}
	
	public void setTransactionTypeId(Long transactionTypeId)
	{
		this.transactionTypeId = transactionTypeId;
	}
   @Column(name = "LEDGER_ID")
	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}
   @Column(name = "TRANSACTION_DATE")
	public Date getTransactionDateTime()
	{
		return transactionDateTime;
	}
	
	public void setTransactionDateTime(Date transactionDateTime)
	{
		this.transactionDateTime = transactionDateTime;
	}
   @Column(name = "BALANCE_AFTER_TRANSACTION")
	public Double getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}

	public void setBalanceAfterTransaction(Double balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}
	
   @Column(name = "RECEIVING_ACCOUNT_ID")
   public Long getReceivingAccountId() {
	return receivingAccountId;
	}
	
	public void setReceivingAccountId(Long receivingAccountId) {
		this.receivingAccountId = receivingAccountId;
	}
   @Column(name = "REASON_ID")
	public Long getReasonId() {
		return reasonId;
	}
	
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
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

   @Version 
   @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }

   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "UPDATED_BY")    
   public AppUserModel getRelationUpdatedByAppUserModel(){
      return updatedByAppUserModel;
   }
    

   @javax.persistence.Transient
   public AppUserModel getUpdatedByAppUserModel(){
      return getRelationUpdatedByAppUserModel();
   }


   @javax.persistence.Transient
   public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
      this.updatedByAppUserModel = appUserModel;
   }
   

   @javax.persistence.Transient
   public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CREATED_BY")    
   public AppUserModel getRelationCreatedByAppUserModel(){
      return createdByAppUserModel;
   }

   @javax.persistence.Transient
   public AppUserModel getCreatedByAppUserModel(){
      return getRelationCreatedByAppUserModel();
   }


   @javax.persistence.Transient
   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
      this.createdByAppUserModel = appUserModel;
   }
   

   @javax.persistence.Transient
   public void setCreatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }


   @javax.persistence.Transient
   public Long getUpdatedBy() {
      if (updatedByAppUserModel != null) {
         return updatedByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   
   @javax.persistence.Transient
   public void setUpdatedBy(Long appUserId) {
      if(appUserId == null)
      {      
      	updatedByAppUserModel = null;
      }
      else
      {
        updatedByAppUserModel = new AppUserModel();
      	updatedByAppUserModel.setAppUserId(appUserId);
      }      
   }


   @javax.persistence.Transient
   public Long getCreatedBy() {
      if (createdByAppUserModel != null) {
         return createdByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   
   @javax.persistence.Transient
   public void setCreatedBy(Long appUserId) {
      if(appUserId == null) {      
      	createdByAppUserModel = null;
      } else {
        createdByAppUserModel = new AppUserModel();
      	createdByAppUserModel.setAppUserId(appUserId);
      }      
   }


   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&accountCreditQueueLogId=" + accountCreditQueueLogId;
      return parameters;
   }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() { 
			String primaryKeyFieldName = "accountCreditQueueLogId";
			return primaryKeyFieldName;				
    }         

    @Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return this.transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	

    @Column(name = "ACCOUNT_NUMBER")
	public String getToBankAccountNumber() {
		return toBankAccountNumber;
	}
			
	public void setToBankAccountNumber(String toBankAccountNumber) {
		this.toBankAccountNumber = toBankAccountNumber;
	}

    @Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}
		
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}	
    @Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Column(name = "IS_CREDIT_PUSH")
	public Boolean getIsCreditPush() {
		return isCreditPush;
	}

	public void setIsCreditPush(Boolean isCreditPush) {
		this.isCreditPush = isCreditPush;
	}

    @Column(name = "STATUS")	   
	public String getStatus() {
		return status;
	}
			
	public void setStatus(String status) {
		this.status = status;
	}
}