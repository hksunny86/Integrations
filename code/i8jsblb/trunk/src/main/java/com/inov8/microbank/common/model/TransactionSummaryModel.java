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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransactionTypeModel entity bean.
 *
 * @author  Namaloom Afraad
 * @version 0.1
 *
 *
 * @spring.bean name="TransactionTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="TRANSACTION_SUMMARY_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="TRANSACTION_SUMMARY_seq") } )
//@javax.persistence.SequenceGenerator(name = "TRANSACTION_SUMMARY_seq",sequenceName = "TRANSACTION_SUMMARY_seq")
@Table(name = "TRANSACTION_SUMMARY")
public class TransactionSummaryModel extends BasePersistableModel implements Serializable{


	private static final long serialVersionUID = -97352185634027953L;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;

	private Long transactionSummaryId;
	private Double amount;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;
	private ProductModel productIdProductModel;
	private Boolean settled;
	private Long fromAccountId;
	private Long toAccountId;

	/**
	 * Default constructor.
	 */
	public TransactionSummaryModel() {
	}   

	/**
	 * Return the primary key.
	 *
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getTransactionSummaryId();
	}

	/**
	 * Set the primary key.
	 *
	 * @param primaryKey the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setTransactionSummaryId(primaryKey);
	}

	/**
	 * Returns the value of the <code>transactionTypeId</code> property.
	 *
	 */
	@Column(name = "TRANSACTION_SUMMARY_ID" , nullable = false )
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTION_SUMMARY_seq")
	public Long getTransactionSummaryId() {
		return transactionSummaryId;
	}

	/**
	 * Sets the value of the <code>transactionSummaryId</code> property.
	 *
	 * @param transactionSummaryId the value for the <code>transactionSummaryId</code> property
	 *    
	 */

	public void setTransactionSummaryId(Long transactionSummaryId) {
		this.transactionSummaryId = transactionSummaryId;
	}

	/**
	 * Returns the value of the <code>amount</code> property.
	 *
	 */
	@Column(name = "amount" , nullable = false )
	public Double getAmount() {
		return amount;
	}

	/**
	 * Sets the value of the <code>amount</code> property.
	 *
	 * @param name the value for the <code>name</code> property
	 *    
	 */

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	
	/**
	    * Returns the value of the <code>productIdProductModel</code> relation property.
	    *
	    * @return the value of the <code>productIdProductModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "PRODUCT_ID")    
	   public ProductModel getRelationProductIdProductModel(){
	      return productIdProductModel;
	   }
	    
	   /**
	    * Returns the value of the <code>productIdProductModel</code> relation property.
	    *
	    * @return the value of the <code>productIdProductModel</code> relation property.
	    *
	    */
	   @javax.persistence.Transient
	   public ProductModel getProductIdProductModel(){
	      return getRelationProductIdProductModel();
	   }

	   /**
	    * Sets the value of the <code>productIdProductModel</code> relation property.
	    *
	    * @param productModel a value for <code>productIdProductModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setRelationProductIdProductModel(ProductModel productModel) {
	      this.productIdProductModel = productModel;
	   }
	   
	   /**
	    * Sets the value of the <code>productIdProductModel</code> relation property.
	    *
	    * @param productModel a value for <code>productIdProductModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setProductIdProductModel(ProductModel productModel) {
	      if(null != productModel)
	      {
	      	setRelationProductIdProductModel((ProductModel)productModel.clone());
	      }      
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
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&transactionSummaryId=" + getTransactionSummaryId();
		return parameters;
	}
	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName()
	{ 
		String primaryKeyFieldName = "transactionSummaryId";
		return primaryKeyFieldName;				
	}

	/**
	    * Returns the value of the <code>settled</code> property.
	    *
	    */
	      @Column(name = "IS_SETTLED" , nullable = true )
	   public Boolean getSettled() {
	      return settled;
	   }

	   /**
	    * Sets the value of the <code>settled</code> property.
	    *
	    * @param settled the value for the <code>settled</code> property
	    *    
			    */

	   public void setSettled(Boolean settled) {
	      this.settled = settled;
	   }
	   
	   
	   /**
	    * Returns the value of the <code>fromAccountId</code> property.
	    *
	    */
	      @Column(name = "FROM_ACCOUNT_ID" , nullable = false)
	   public Long getFromAccountId() {
	      return fromAccountId;
	   }

	   /**
	    * Sets the value of the <code>fromAccountId</code> property.
	    *
	    * @param accountId the value for the <code>fromAccountId</code> property
	    *    
			    * @spring.validator type="required"
	    * @spring.validator type="maxlength"     
	    * @spring.validator-args arg1value="${var:maxlength}"
	    * @spring.validator-var name="maxlength" value="50"
	    */

	   public void setFromAccountId(Long accountId) {
	      this.fromAccountId = accountId;
	   }
	
	   /**
	    * Returns the value of the <code>toAccountId</code> property.
	    *
	    */
	      @Column(name = "TO_ACCOUNT_ID" , nullable = false , length=50 )
	   public Long getToAccountId() {
	      return toAccountId;
	   }

	   /**
	    * Sets the value of the <code>toAccountId</code> property.
	    *
	    * @param accountId the value for the <code>toAccountId</code> property
	    *    
			    * @spring.validator type="required"
	    * @spring.validator type="maxlength"     
	    * @spring.validator-args arg1value="${var:maxlength}"
	    * @spring.validator-var name="maxlength" value="50"
	    */

	   public void setToAccountId(Long accountId) {
	      this.toAccountId = accountId;
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

		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);

		return associationModelList;
	}

}
