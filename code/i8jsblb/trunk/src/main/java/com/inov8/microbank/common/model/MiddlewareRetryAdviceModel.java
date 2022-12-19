package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.CoreAdviceInterface;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="MIDDLEWARE_RETRY_ADVICE_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="MIDDLEWARE_RETRY_ADVICE_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "MIDDLEWARE_RETRY_ADVICE_SEQ",sequenceName = "MIDDLEWARE_RETRY_ADVICE_SEQ")
@Table(name = "MIDDLEWARE_RETRY_ADVICE_REPORT")
public class MiddlewareRetryAdviceModel extends BasePersistableModel implements Serializable, CoreAdviceInterface{
	
   	/**
	 * 
	 */
	private static final long serialVersionUID = 1640433713544167742L;
	
	private Long middlewareRetryAdviceId;
   	private Long productId;
   	private Long intgTransactionTypeId;
   	private Long transactionCodeId;
   	private String transactionCode;
   	private String fromAccount;
   	private String toAccount;
   	private Double transactionAmount;
   	private String stan;
   	private String reversalStan;
   	private String status;
   	private String responseCode;
   	private Date transmissionTime;
   	private String reversalRequestTime;
   	private Date requestTime;
   	private String adviceType;

	private String billAggregator;
	private String cnicNo;
	private String consumerNo;
	private String billCategoryCode;
	private String compnayCode;
	private Long transactionId;
	private Long actionLogId;   	
	private String retrievalReferenceNumber;
	private String ubpBBStan;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;

    //IBFT
    private String senderBankImd;
    private String crDr;
    private String senderIBAN;
    private String beneIBAN;
    private String beneBankName;
    private String beneBranchName;
    private String senderName;
    private String cardAcceptorNameAndLocation;
    private String agentId;
    private String purposeOfPayment;
    private String beneAccountTitle;
    private String beneBankImd;

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getMiddlewareRetryAdviceId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setMiddlewareRetryAdviceId(primaryKey);
    }

   /**
    * Returns the value of the <code>middlewareRetryAdviceId</code> property.
    *
    */
      @Column(name = "MIDDLEWARE_RETRY_ADV_RPRT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MIDDLEWARE_RETRY_ADVICE_SEQ")
   public Long getMiddlewareRetryAdviceId() {
      return middlewareRetryAdviceId;
   }

   /**
    * Sets the value of the <code>middlewareRetryAdviceId</code> property.
    *
    * @param middlewareRetryAdviceId the value for the <code>middlewareRetryAdviceId</code> property
    *    
		    */

   public void setMiddlewareRetryAdviceId(Long middlewareRetryAdviceId) {
      this.middlewareRetryAdviceId = middlewareRetryAdviceId;
   }




   /**
    * Returns the value of the <code>updatedOn</code> property.
    *
    */
      @Column(name = "UPDATED_ON")
   public Date getUpdatedOn() {
      return updatedOn;
   }

   /**
    * Sets the value of the <code>updatedOn</code> property.
    *
    * @param updatedOn the value for the <code>updatedOn</code> property
    *    
		    */

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON")
   public Date getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   /**
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   /**
    * Sets the value of the <code>versionNo</code> property.
    *
    * @param versionNo the value for the <code>versionNo</code> property
    *    
		    */

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }

   /**
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CREATED_BY")    
   public AppUserModel getRelationCreatedByAppUserModel(){
      return createdByAppUserModel;
   }
    
   /**
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getCreatedByAppUserModel(){
      return getRelationCreatedByAppUserModel();
   }

   /**
    * Sets the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>createdByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
      this.createdByAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>createdByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setCreatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @return the value of the <code>updatedByAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "UPDATED_BY")    
   public AppUserModel getRelationUpdatedByAppUserModel(){
      return updatedByAppUserModel;
   }
    
   /**
    * Returns the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @return the value of the <code>updatedByAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getUpdatedByAppUserModel(){
      return getRelationUpdatedByAppUserModel();
   }

   /**
    * Sets the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>updatedByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
      this.updatedByAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>updatedByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   


   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCreatedBy() {
      if (createdByAppUserModel != null) {
         return createdByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																			    */
   
   @javax.persistence.Transient
   public void setCreatedBy(Long appUserId) {
      if(appUserId == null)
      {      
      	createdByAppUserModel = null;
      }
      else
      {
        createdByAppUserModel = new AppUserModel();
      	createdByAppUserModel.setAppUserId(appUserId);
      }      
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUpdatedBy() {
      if (updatedByAppUserModel != null) {
         return updatedByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																			    */
   
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


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getMiddlewareRetryAdviceId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&middlewareRetryAdviceId=" + getMiddlewareRetryAdviceId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "middlewareRetryAdviceId";
			return primaryKeyFieldName;				
    }
    
    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }



    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

    @Column(name = "INTG_TRANSACTION_TYPE_ID")
	public Long getIntgTransactionTypeId() {
		return intgTransactionTypeId;
	}

	public void setIntgTransactionTypeId(Long intgTransactionTypeId) {
		this.intgTransactionTypeId = intgTransactionTypeId;
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

    @Column(name = "STATUS")
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    @Column(name = "RESPONSE_CODE")
    public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

    @Column(name = "TRANSMISSION_TIME")
    public Date getTransmissionTime() {
		return transmissionTime;
	}

	public void setTransmissionTime(Date transmissionTime) {
		this.transmissionTime = transmissionTime;
	}

    @Column(name = "REVERSAL_REQUEST_TIME")
    public String getReversalRequestTime() {
		return reversalRequestTime;
	}

	public void setReversalRequestTime(String reversalRequestTime) {
		this.reversalRequestTime = reversalRequestTime;
	}

    @Column(name = "REQUEST_TIME")
    public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

    @Column(name = "ADVICE_TYPE")
	public String getAdviceType() {
		return adviceType;
	}

	public void setAdviceType(String adviceType) {
		this.adviceType = adviceType;
	}    

    @Column(name = "BILL_AGGREGATOR")
	public String getBillAggregator() {
		return billAggregator;
	}

	public void setBillAggregator(String billAggregator) {
		this.billAggregator = billAggregator;
	}

    @Column(name = "CNIC")
	public String getCnicNo() {
		return cnicNo;
	}

	public void setCnicNo(String cnicNo) {
		this.cnicNo = cnicNo;
	}

    @Column(name = "CONSUMER_NUMBER")
	public String getConsumerNo() {
		return consumerNo;
	}

	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}

    @Column(name = "BILL_CATEGORY_CODE")
	public String getBillCategoryCode() {
		return billCategoryCode;
	}

	public void setBillCategoryCode(String billCategoryCode) {
		this.billCategoryCode = billCategoryCode;
	}

    @Column(name = "BILL_COMPANY_CODE")
	public String getCompnayCode() {
		return compnayCode;
	}

	public void setCompnayCode(String compnayCode) {
		this.compnayCode = compnayCode;
	}

    @Column(name = "TRANSACTION_ID")
	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

    @Column(name = "ACTION_LOG_ID")
	public Long getActionLogId() {
		return actionLogId;
	}

	public void setActionLogId(Long actionLogId) {
		this.actionLogId = actionLogId;
	}

    @Column(name = "RRN")
	public String getRetrievalReferenceNumber() {
		return retrievalReferenceNumber;
	}

	public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
		this.retrievalReferenceNumber = retrievalReferenceNumber;
	}

    @Column(name = "UBP_BB_STAN")	
   	public String getUbpBBStan() {
		return ubpBBStan;
	}

	public void setUbpBBStan(String ubpBBStan) {
		this.ubpBBStan = ubpBBStan;
	}

    @Column(name = "SENDER_BANK_IMD")
    public String getSenderBankImd() {
        return senderBankImd;
    }

    public void setSenderBankImd(String senderBankImd) {
        this.senderBankImd = senderBankImd;
    }

    @Column(name = "CR_DR")
    public String getCrDr() {
        return crDr;
    }

    public void setCrDr(String crDr) {
        this.crDr = crDr;
    }

    @Column(name = "BENE_IBAN")
    public String getBeneIBAN() {
        return beneIBAN;
    }

    public void setBeneIBAN(String beneIBAN) {
        this.beneIBAN = beneIBAN;
    }

    @Column(name = "BENE_BANK_NAME")
    public String getBeneBankName() {
        return beneBankName;
    }

    public void setBeneBankName(String beneBankName) {
        this.beneBankName = beneBankName;
    }

    @Column(name = "BENE_BRANCH_NAME")
    public String getBeneBranchName() {
        return beneBranchName;
    }

    public void setBeneBranchName(String beneBranchName) {
        this.beneBranchName = beneBranchName;
    }

    @Column(name = "SENDER_NAME")
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Column(name = "ACCEPTOR_DETAILS")
    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    @Column(name = "AGENT_ID")
    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Column(name = "PAYMENT_PURPOSE")
    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    @Column(name = "SENDER_IBAN")
    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    @Column(name = "BENE_NAME")
    public String getBeneAccountTitle() {
        return beneAccountTitle;
    }

    public void setBeneAccountTitle(String beneAccountTitle) {
        this.beneAccountTitle = beneAccountTitle;
    }

    @Column(name = "BENE_BANK_IMD")
    public String getBeneBankImd() {
        return beneBankImd;
    }

    public void setBeneBankImd(String beneBankImd) {
        this.beneBankImd = beneBankImd;
    }
          
}
