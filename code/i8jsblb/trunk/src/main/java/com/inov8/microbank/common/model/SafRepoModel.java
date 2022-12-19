package com.inov8.microbank.common.model;

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
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.common.model.OlaTransactionTypeModel;
import com.inov8.integration.common.model.ReasonModel;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="SAF_REPO_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="SAF_REPO_seq") } )
//@javax.persistence.SequenceGenerator(name = "SAF_REPO_seq", sequenceName = "SAF_REPO_seq")
@Table(name = "SAF_REPO")
public class SafRepoModel extends BasePersistableModel implements Serializable{
	
	private Long safRepoId;
	private String transactionCode;
	private AccountModel accountIdAccountModel;
	private OlaTransactionTypeModel transactionTypeIdTransactionTypeModel;
	private ReasonModel reasonIdReasonModel;
	private OlaCustomerAccountTypeModel customerAccountTypeModel;
	private Double transactionAmount;
	private Date transactionTime;
	private Long category;
	private ProductModel productIdProductModel;
	private Long segmentId;
	private Long ledgerId; 
	private String transactionStatus;
	private Boolean isComplete;
	
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

	   
	@Column(name = "SAF_REPO_ID" , nullable = false )
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SAF_REPO_seq")
	public Long getSafRepoId() {
		return safRepoId;
	}

	public void setSafRepoId(Long safRepoId) {
		this.safRepoId = safRepoId;
	}

	

	   /**
	    * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
	    *
	    * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
	    *
	    */
	   
	   @Column(name = "TRANSACTION_CODE")    
	   public String getTransactionCode(){
	      return transactionCode;
	   }
	    
	
	   /**
	    * Returns the value of the <code>accountIdAccountModel</code> relation property.
	    *
	    * @return the value of the <code>accountIdAccountModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "ACCOUNT_ID")    
	   public AccountModel getRelationAccountIdAccountModel(){
	      return accountIdAccountModel;
	   }
	    
	   /**
	    * Returns the value of the <code>accountIdAccountModel</code> relation property.
	    *
	    * @return the value of the <code>accountIdAccountModel</code> relation property.
	    *
	    */
	   @javax.persistence.Transient
	   public AccountModel getAccountIdAccountModel(){
	      return getRelationAccountIdAccountModel();
	   }

	   /**
	    * Sets the value of the <code>accountIdAccountModel</code> relation property.
	    *
	    * @param accountModel a value for <code>accountIdAccountModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setRelationAccountIdAccountModel(AccountModel accountModel) {
	      this.accountIdAccountModel = accountModel;
	   }
	   
	   /**
	    * Sets the value of the <code>accountIdAccountModel</code> relation property.
	    *
	    * @param accountModel a value for <code>accountIdAccountModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setAccountIdAccountModel(AccountModel accountModel) {
	      if(null != accountModel)
	      {
	      	setRelationAccountIdAccountModel((AccountModel)accountModel.clone());
	      }      
	   }
	   



	   /**
	    * Returns the value of the <code>accountId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getAccountId() {
	      if (accountIdAccountModel != null) {
	         return accountIdAccountModel.getAccountId();
	      } else {
	         return null;
	      }
	   }

	   /**
	    * Sets the value of the <code>accountId</code> property.
	    *
	    * @param accountId the value for the <code>accountId</code> property
																						    */
	   
	   @javax.persistence.Transient
	   public void setAccountId(Long accountId) {
	      if(accountId == null)
	      {      
	      	accountIdAccountModel = null;
	      }
	      else
	      {
	        accountIdAccountModel = new AccountModel();
	      	accountIdAccountModel.setAccountId(accountId);
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
	    * Sets the value of the <code>transactionCodeId</code> property.
	    *
	    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
								    * @spring.validator type="required"
																																																																																    */
	   
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getSafRepoId();
	}
	@Override
	  @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "safRepoId";
		return primaryKeyFieldName;				
	}
	@Override
	  @javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		 String parameters = "";
	      parameters += "&safRepoId=" + getSafRepoId();
	      return parameters;
	}
	@Override
	  @javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
	     setSafRepoId(primaryKey);
		
	}
	
	
	
	 /**
	    * Returns the value of the <code>createdOn</code> property.
	    *
	    */
	      @Column(name = "CREATED_ON" , nullable = false )
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
	    * Returns the value of the <code>updatedOn</code> property.
	    *
	    */
	      @Column(name = "UPDATED_ON" , nullable = false )
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

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
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
	   
	   @Transient
	   public Long getCustomerAccountTypeId() {
		   
		   OlaCustomerAccountTypeModel customerAccountTypeModel = getCustomerAccountTypeModel();
		   return (customerAccountTypeModel != null ? customerAccountTypeModel.getCustomerAccountTypeId() : null);	   
	   }
	   
	   public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		   if(customerAccountTypeId == null){
			   customerAccountTypeModel = null;
		   }else{
			   customerAccountTypeModel = new OlaCustomerAccountTypeModel();
			   customerAccountTypeModel.setCustomerAccountTypeId(customerAccountTypeId);
		   }
	   }
	   
	   
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "CUST_ACC_TYPE_ID")
	   public OlaCustomerAccountTypeModel getCustomerAccountTypeModel() {
		return customerAccountTypeModel;
	   }

	   public void setCustomerAccountTypeModel(OlaCustomerAccountTypeModel customerAccountTypeModel) {
		   this.customerAccountTypeModel = customerAccountTypeModel;
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
	   
	   @Column(name = "category")
		public Long getCategory() {
			return category;
		}

		public void setCategory(Long category) {
			this.category = category;
		}
		
		 /**
		    * Returns the value of the <code>productId</code> property.
		    *
		    */
		
		 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		   @JoinColumn(name = "PRODUCT_ID" , nullable = true)    
		   public ProductModel getRelationProductIdProductModel(){
		      return productIdProductModel;
		   }
		    
		   @javax.persistence.Transient
		   public ProductModel getProductIdProductModel(){
		      return getRelationProductIdProductModel();
		   }

		   @javax.persistence.Transient
		   public void setRelationProductIdProductModel(ProductModel productModel) {
		      this.productIdProductModel = productModel;
		   }
		   
		   @javax.persistence.Transient
		   public void setProductIdProductModel(ProductModel productModel) {
		      if(null != productModel)
		      {
		      	setRelationProductIdProductModel((ProductModel)productModel.clone());
		      }      
		   }
		   
		   @Transient
		   public Long getProductId() {
			   
			   ProductModel productModel = getProductIdProductModel();
			   return (productModel != null ? productModel.getProductId() : null);	   
		   }
		   
		   @Transient
		   public void setProductId(Long productId) {
			   if(productId == null){
				   this.productIdProductModel = null;
			   }else{
				   productIdProductModel = new ProductModel();
				   productIdProductModel.setProductId(productId);
			   }
		   }
		
		      @Column(name = "SEGMENT_ID" )
		public Long getSegmentId() {
			return segmentId;
		}

		public void setSegmentId(Long segmentId) {
			this.segmentId = segmentId;
		}

	      @Column(name = "LEDGER_ID" )
		public Long getLedgerId() {
			return ledgerId;
		}

		public void setLedgerId(Long ledgerId) {
			this.ledgerId = ledgerId;
		}

		  @Column(name = "TRANSACTION_STATUS" )
		public String getTransactionStatus() {
			return transactionStatus;
		}

		public void setTransactionStatus(String transactionStatus) {
			this.transactionStatus = transactionStatus;
		}
		
		 @javax.persistence.Transient
		    @Override
		    public List<AssociationModel> getAssociationModelList()
		    {
		    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
		    	AssociationModel associationModel = null;    	
		    	
		    	associationModel = new AssociationModel();
		     	
			 	associationModel.setClassName("OlaTransactionTypeModel");
			 	associationModel.setPropertyName("relationTransactionTypeIdTransactionTypeModel");   		
				associationModel.setValue(getRelationTransactionTypeIdTransactionTypeModel());
				
				associationModelList.add(associationModel);
				
				associationModel = new AssociationModel();
			 	
			 	associationModel.setClassName("ProductModel");
			 	associationModel.setPropertyName("relationProductIdProductModel");   		
				associationModel.setValue(getRelationProductIdProductModel());
				
				associationModelList.add(associationModel);
				
				associationModel = new AssociationModel();
			 	
			 	associationModel.setClassName("AccountModel");
			 	associationModel.setPropertyName("relationAccountIdAccountModel");   		
				associationModel.setValue(getRelationAccountIdAccountModel());
				
				associationModelList.add(associationModel);
				
				return associationModelList;
		    }

		@Column(name = "IS_COMPLETE" )
		public Boolean getIsComplete() {
			return isComplete;
		}

		public void setIsComplete(Boolean isComplete) {
			this.isComplete = isComplete;
		}

}
