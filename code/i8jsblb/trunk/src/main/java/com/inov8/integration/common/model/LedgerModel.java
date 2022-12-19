package com.inov8.integration.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The LedgerModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LedgerModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate=true)
@org.hibernate.annotations.GenericGenerator(name="LEDGER_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="LEDGER_seq") } )
//@javax.persistence.SequenceGenerator(name = "LEDGER_seq",sequenceName = "LEDGER_seq")
@Table(name = "LEDGER")
public class LedgerModel extends BasePersistableModel implements Serializable {
  

   private OlaTransactionTypeModel transactionTypeIdTransactionTypeModel;
   private ResponseCodeModel responseCodeIdResponseCodeModel;
   private ReasonModel reasonIdReasonModel;
//   private AccountModel accountIdAccountModel;


   private Long ledgerId;
   private Date transactionTime;
   private String microbankTransactionCode;
   private Double balanceAfterTransaction;
   private Double transactionAmount;
   private String authCode;
   private Integer versionNo;
   private Long accountId;
   private Long toAccountId;
   private Double toBalanceAfterTransaction;
   private Boolean isReversal;
   private Long reversalLedgerId;
   private Boolean excludeLimit;
   private Boolean excludeLimitForHandler;
   private Long handlerId;
   private Long category;
   private Long commissionType;
   private Boolean isSettledQueue;
   private Boolean isProcessedByQueue;
   private Long queueStatus;

/**
    * Default constructor.
    */
   public LedgerModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getLedgerId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setLedgerId(primaryKey);
    }

   /**
    * Returns the value of the <code>ledgerId</code> property.
    *
    */
      @Column(name = "LEDGER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LEDGER_seq")
   public Long getLedgerId() {
      return ledgerId;
   }

   /**
    * Sets the value of the <code>ledgerId</code> property.
    *
    * @param ledgerId the value for the <code>ledgerId</code> property
    *    
		    */

   public void setLedgerId(Long ledgerId) {
      this.ledgerId = ledgerId;
   }

   /**
    * Returns the value of the <code>transactionTime</code> property.
    *
    */
      @Column(name = "TRANSACTION_TIME" , nullable = false )
   public Date getTransactionTime() {
      return transactionTime;
   }

   /**
    * Sets the value of the <code>transactionTime</code> property.
    *
    * @param transactionTime the value for the <code>transactionTime</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setTransactionTime(Date transactionTime) {
      this.transactionTime = transactionTime;
   }

   /**
    * Returns the value of the <code>microbankTransactionCode</code> property.
    *
    */
      @Column(name = "MICROBANK_TRANSACTION_CODE" , nullable = false , length=15 )
   public String getMicrobankTransactionCode() {
      return microbankTransactionCode;
   }

   /**
    * Sets the value of the <code>microbankTransactionCode</code> property.
    *
    * @param microbankTransactionCode the value for the <code>microbankTransactionCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="15"
    */

   public void setMicrobankTransactionCode(String microbankTransactionCode) {
      this.microbankTransactionCode = microbankTransactionCode;
   }

   /**
    * Returns the value of the <code>balanceAfterTransaction</code> property.
    *
    */
      @Column(name = "FROM_BALANCE_AFTER_TRANSACTION" , nullable = false )
   public Double getBalanceAfterTransaction() {
      return balanceAfterTransaction;
   }

   /**
    * Sets the value of the <code>balanceAfterTransaction</code> property.
    *
    * @param balanceAfterTransaction the value for the <code>balanceAfterTransaction</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBalanceAfterTransaction(Double balanceAfterTransaction) {
      this.balanceAfterTransaction = balanceAfterTransaction;
   }

   /**
    * Returns the value of the <code>transactionAmount</code> property.
    *
    */
      @Column(name = "TRANSACTION_AMOUNT" , nullable = false )
   public Double getTransactionAmount() {
      return transactionAmount;
   }

   /**
    * Sets the value of the <code>transactionAmount</code> property.
    *
    * @param transactionAmount the value for the <code>transactionAmount</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTransactionAmount(Double transactionAmount) {
      this.transactionAmount = transactionAmount;
   }

   /**
    * Returns the value of the <code>authCode</code> property.
    *
    */
      @Column(name = "AUTH_CODE" , nullable = false )
   public String getAuthCode() {
      return authCode;
   }

   /**
    * Sets the value of the <code>authCode</code> property.
    *
    * @param authCode the value for the <code>authCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="15"
    */

   public void setAuthCode(String authCode) {
      this.authCode = authCode;
   }
   
   
   /**
    * Returns the value of the <code>authCode</code> property.
    *
    */
      @Column(name = "FROM_ACCOUNT_ID" , nullable = false , length=15 )
   public Long getAccountId() {
      return accountId;
   }

   /**
    * Sets the value of the <code>accountId</code> property.
    *
    * @param authCode the value for the <code>authCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="15"
    */

   public void setAccountId(Long accountId) {
      this.accountId = accountId;
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
    * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_TYPE_ID" , nullable = true)    
   public OlaTransactionTypeModel getRelationTransactionTypeIdTransactionTypeModel(){
      return transactionTypeIdTransactionTypeModel;
   }
    
   /**
    * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public OlaTransactionTypeModel getTransactionTypeIdTransactionTypeModel(){
      return getRelationTransactionTypeIdTransactionTypeModel();
   }

   /**
    * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionTypeIdTransactionTypeModel(OlaTransactionTypeModel transactionTypeModel) {
      this.transactionTypeIdTransactionTypeModel = transactionTypeModel;
   }
   
   /**
    * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionTypeIdTransactionTypeModel(OlaTransactionTypeModel transactionTypeModel) {
      if(null != transactionTypeModel)
      {
      	setRelationTransactionTypeIdTransactionTypeModel((OlaTransactionTypeModel)transactionTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>responseCodeIdResponseCodeModel</code> relation property.
    *
    * @return the value of the <code>responseCodeIdResponseCodeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RESPONSE_CODE_ID")    
   public ResponseCodeModel getRelationResponseCodeIdResponseCodeModel(){
      return responseCodeIdResponseCodeModel;
   }
    
   /**
    * Returns the value of the <code>responseCodeIdResponseCodeModel</code> relation property.
    *
    * @return the value of the <code>responseCodeIdResponseCodeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ResponseCodeModel getResponseCodeIdResponseCodeModel(){
      return getRelationResponseCodeIdResponseCodeModel();
   }

   /**
    * Sets the value of the <code>responseCodeIdResponseCodeModel</code> relation property.
    *
    * @param responseCodeModel a value for <code>responseCodeIdResponseCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationResponseCodeIdResponseCodeModel(ResponseCodeModel responseCodeModel) {
      this.responseCodeIdResponseCodeModel = responseCodeModel;
   }
   
   /**
    * Sets the value of the <code>responseCodeIdResponseCodeModel</code> relation property.
    *
    * @param responseCodeModel a value for <code>responseCodeIdResponseCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setResponseCodeIdResponseCodeModel(ResponseCodeModel responseCodeModel) {
      if(null != responseCodeModel)
      {
      	setRelationResponseCodeIdResponseCodeModel((ResponseCodeModel)responseCodeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>reasonIdReasonModel</code> relation property.
    *
    * @return the value of the <code>reasonIdReasonModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "REASON_ID")    
   public ReasonModel getRelationReasonIdReasonModel(){
      return reasonIdReasonModel;
   }
    
   /**
    * Returns the value of the <code>reasonIdReasonModel</code> relation property.
    *
    * @return the value of the <code>reasonIdReasonModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ReasonModel getReasonIdReasonModel(){
      return getRelationReasonIdReasonModel();
   }

   /**
    * Sets the value of the <code>reasonIdReasonModel</code> relation property.
    *
    * @param reasonModel a value for <code>reasonIdReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationReasonIdReasonModel(ReasonModel reasonModel) {
      this.reasonIdReasonModel = reasonModel;
   }
   
   /**
    * Sets the value of the <code>reasonIdReasonModel</code> relation property.
    *
    * @param reasonModel a value for <code>reasonIdReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setReasonIdReasonModel(ReasonModel reasonModel) {
      if(null != reasonModel)
      {
      	setRelationReasonIdReasonModel((ReasonModel)reasonModel.clone());
      }      
   }
   

//   /**
//    * Returns the value of the <code>accountIdAccountModel</code> relation property.
//    *
//    * @return the value of the <code>accountIdAccountModel</code> relation property.
//    *
//    */
//   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//   @JoinColumn(name = "ACCOUNT_ID")    
//   public AccountModel getRelationAccountIdAccountModel(){
//      return accountIdAccountModel;
//   }
//    
//   /**
//    * Returns the value of the <code>accountIdAccountModel</code> relation property.
//    *
//    * @return the value of the <code>accountIdAccountModel</code> relation property.
//    *
//    */
//   @javax.persistence.Transient
//   public AccountModel getAccountIdAccountModel(){
//      return getRelationAccountIdAccountModel();
//   }
//
//   /**
//    * Sets the value of the <code>accountIdAccountModel</code> relation property.
//    *
//    * @param accountModel a value for <code>accountIdAccountModel</code>.
//    */
//   @javax.persistence.Transient
//   public void setRelationAccountIdAccountModel(AccountModel accountModel) {
//      this.accountIdAccountModel = accountModel;
//   }
   
//   /**
//    * Sets the value of the <code>accountIdAccountModel</code> relation property.
//    *
//    * @param accountModel a value for <code>accountIdAccountModel</code>.
//    */
//   @javax.persistence.Transient
//   public void setAccountIdAccountModel(AccountModel accountModel) {
//      if(null != accountModel)
//      {
//      	setRelationAccountIdAccountModel((AccountModel)accountModel.clone());
//      }      
//   }
   



   /**
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionTypeId() {
      if (transactionTypeIdTransactionTypeModel != null) {
         return transactionTypeIdTransactionTypeModel.getTransactionTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionTypeId</code> property.
    *
    * @param transactionTypeId the value for the <code>transactionTypeId</code> property
							    * @spring.validator type="required"
																			    */
   
   @javax.persistence.Transient
   public void setTransactionTypeId(Long transactionTypeId) {
      if(transactionTypeId == null)
      {      
      	transactionTypeIdTransactionTypeModel = null;
      }
      else
      {
        transactionTypeIdTransactionTypeModel = new OlaTransactionTypeModel();
      	transactionTypeIdTransactionTypeModel.setTransactionTypeId(transactionTypeId);
      }      
   }

   /**
    * Returns the value of the <code>responseCodeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getResponseCodeId() {
      if (responseCodeIdResponseCodeModel != null) {
         return responseCodeIdResponseCodeModel.getResponseCodeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>responseCodeId</code> property.
    *
    * @param responseCodeId the value for the <code>responseCodeId</code> property
																					    * @spring.validator type="required"
					    */
   
   @javax.persistence.Transient
   public void setResponseCodeId(Long responseCodeId) {
      if(responseCodeId == null)
      {      
      	responseCodeIdResponseCodeModel = null;
      }
      else
      {
        responseCodeIdResponseCodeModel = new ResponseCodeModel();
      	responseCodeIdResponseCodeModel.setResponseCodeId(responseCodeId);
      }      
   }

   /**
    * Returns the value of the <code>reasonId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getReasonId() {
      if (reasonIdReasonModel != null) {
         return reasonIdReasonModel.getReasonId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>reasonId</code> property.
    *
    * @param reasonId the value for the <code>reasonId</code> property
											    * @spring.validator type="required"
															    */
   
   @javax.persistence.Transient
   public void setReasonId(Long reasonId) {
      if(reasonId == null)
      {      
      	reasonIdReasonModel = null;
      }
      else
      {
        reasonIdReasonModel = new ReasonModel();
      	reasonIdReasonModel.setReasonId(reasonId);
      }      
   }

//   /**
//    * Returns the value of the <code>accountId</code> property.
//    *
//    */
//   @javax.persistence.Transient
//   public Long getAccountId() {
//      if (accountIdAccountModel != null) {
//         return accountIdAccountModel.getAccountId();
//      } else {
//         return null;
//      }
//   }
//
//   /**
//    * Sets the value of the <code>accountId</code> property.
//    *
//    * @param accountId the value for the <code>accountId</code> property
//					    * @spring.validator type="required"
//																					    */
//   
//   @javax.persistence.Transient
//   public void setAccountId(Long accountId) {
//      if(accountId == null)
//      {      
//      	accountIdAccountModel = null;
//      }
//      else
//      {
//        accountIdAccountModel = new AccountModel();
//      	accountIdAccountModel.setAccountId(accountId);
//      }      
//   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getLedgerId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&ledgerId=" + getLedgerId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "ledgerId";
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
    	
    	associationModel.setClassName("TransactionTypeModel");
    	associationModel.setPropertyName("relationTransactionTypeIdTransactionTypeModel");   		
   		associationModel.setValue(getRelationTransactionTypeIdTransactionTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ResponseCodeModel");
    	associationModel.setPropertyName("relationResponseCodeIdResponseCodeModel");   		
   		associationModel.setValue(getRelationResponseCodeIdResponseCodeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ReasonModel");
    	associationModel.setPropertyName("relationReasonIdReasonModel");   		
   		associationModel.setValue(getRelationReasonIdReasonModel());
   		
   		associationModelList.add(associationModel);
   		
//			      associationModel = new AssociationModel();
//    	
//    	associationModel.setClassName("AccountModel");
//    	associationModel.setPropertyName("relationAccountIdAccountModel");   		
//   		associationModel.setValue(getRelationAccountIdAccountModel());
//   		
//   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }

    @Column(name = "TO_ACCOUNT_ID" , nullable = false , length=15 )
	public Long getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(Long toAccountId) {
		this.toAccountId = toAccountId;
	}

    @Column(name = "TO_BALANCE_AFTER_TRANSACTION" , nullable = false )
	public Double getToBalanceAfterTransaction() {
		return toBalanceAfterTransaction;
	}

	public void setToBalanceAfterTransaction(Double toBalanceAfterTransaction) {
		this.toBalanceAfterTransaction = toBalanceAfterTransaction;
	}

    @Column(name = "IS_REVERSAL")
	public Boolean getIsReversal() {
		return isReversal;
	}

	public void setIsReversal(Boolean isReversal) {
		this.isReversal = isReversal;
	}

    @Column(name = "REVERSAL_LEDGER_ID")
	public Long getReversalLedgerId() {
		return reversalLedgerId;
	}

	public void setReversalLedgerId(Long reversalLedgerId) {
		this.reversalLedgerId = reversalLedgerId;
	}

    @Column(name = "EXCLUDE_LIMIT")
	public Boolean getExcludeLimit() {
		return excludeLimit;
	}

	public void setExcludeLimit(Boolean excludeLimit) {
		this.excludeLimit = excludeLimit;
	}    

    @Column(name = "EXCLUDE_LIMIT_HANDLER")
	public Boolean getExcludeLimitForHandler() {
		return excludeLimitForHandler;
	}

	public void setExcludeLimitForHandler(Boolean excludeLimitForHandler) {
		this.excludeLimitForHandler = excludeLimitForHandler;
	}    

	@Column(name = "HANDLER_ID")
	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}

	@Column(name = "category")
	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	@Column(name = "COMMISSION_TYPE")
	public Long getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(Long commissionType) {
		this.commissionType = commissionType;
	}
	
	@Column(name = "IS_SETTLED_BY_QUEUE")
	public Boolean getIsSettledQueue() {
		return isSettledQueue;
	}
		
	public void setIsSettledQueue(Boolean isSettledQueue) {
		this.isSettledQueue = isSettledQueue;
	}

	@Column(name = "IS_PROCESSED_BY_QUEUE")
	public Boolean getIsProcessedByQueue() {
		return isProcessedByQueue;
	}

	public void setIsProcessedByQueue(Boolean isProcessedByQueue) {
		this.isProcessedByQueue = isProcessedByQueue;
	}

	@Column(name = "QUEUE_STATUS")
	public Long getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(Long queueStatus) {
		this.queueStatus = queueStatus;
	}
}
