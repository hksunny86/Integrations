package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="MESSAGE_QUEUE_LOG_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="MESSAGE_QUEUE_LOG_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "MESSAGE_QUEUE_LOG_SEQ",sequenceName = "MESSAGE_QUEUE_LOG_SEQ")
@Table(name = "MESSAGE_QUEUE_LOG")
public class MessageQueueLogModel extends BasePersistableModel implements Serializable{

   private static final long serialVersionUID = -3183432472310526991L;

   private Long messageQueueLogId;
   private String utilityCompanyId;
   private TransactionDetailModel transactionDetailIdTransactionDetailModel;
   private Integer retryCount;
   private String toBankAccountNumber;
   private String transactionCode;
   private String consumerNumber;
   private String queueName;
   private String serviceUrl;
   private Double totalAmount;
   private String responseCode;
   private Date createdOn;
   private Date updatedOn;  	
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;	
   private Integer versionNo;
   
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getMessageQueueLogId();
   }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setMessageQueueLogId(primaryKey);
   }

   @Column(name = "MESSAGE_QUEUE_LOG_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MESSAGE_QUEUE_LOG_SEQ")
   public Long getMessageQueueLogId() {
      return messageQueueLogId;
   }

   public void setMessageQueueLogId(Long messageQueueLogId) {
      this.messageQueueLogId = messageQueueLogId;
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

   @Column(name = "UTILITY_COMPANY_ID")
   public String getUtilityCompanyId() {
	   return utilityCompanyId;
   }

   public void setUtilityCompanyId(String utilityCompanyId) {
		this.utilityCompanyId = utilityCompanyId;
	}

   
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_DETAIL_ID")    
   public TransactionDetailModel getRelationTransactionDetailIdTransactionDetailModel(){
      return transactionDetailIdTransactionDetailModel;
   }

   @javax.persistence.Transient
   public TransactionDetailModel getTransactionDetailIdTransactionDetailModel(){
      return getRelationTransactionDetailIdTransactionDetailModel();
   }

   @javax.persistence.Transient
   public void setRelationTransactionDetailIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {
      this.transactionDetailIdTransactionDetailModel = transactionDetailModel;
   }

   @javax.persistence.Transient
   public void setTransactionDetailIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {
	  if(transactionDetailModel != null) {
      	setRelationTransactionDetailIdTransactionDetailModel((TransactionDetailModel)transactionDetailModel.clone());
      }      
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
      parameters += "&messageQueueLogId=" + messageQueueLogId;
      return parameters;
   }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() { 
			String primaryKeyFieldName = "messageQueueLogId";
			return primaryKeyFieldName;				
    }         
    @Column(name = "QUEUE_NAME")
    public String getQueueName() {
    	return queueName;
    }

    public void setQueueName(String queueName) {
    	this.queueName = queueName;
    }

    @Column(name = "AMOUNT")
	public Double getTotalAmount() {
		return totalAmount;
	}
		
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

    @Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return this.transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	
    @Column(name = "CONSUMER_NUMBER")
	public String getConsumerNumber() {
		return consumerNumber;
	}
		
	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

    @Column(name = "RETRY_COUNT")
	public Integer getRetryCount() {
		return retryCount;
	}
			
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

    @Column(name = "TO_ACCOUNT_NUMBER")
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

    @Column(name = "SERVICE_URI")
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}