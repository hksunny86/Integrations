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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;



/**
 * The AccountModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/11/04 19:29:08 $
 *
 *
 * @spring.bean name="AccountModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACCOUNT_seq",sequenceName = "ACCOUNT_seq", allocationSize=1)
@Table(name = "ACCOUNT")
public class AccountModel extends BasePersistableModel implements Serializable {
  

   private AccountHolderModel accountHolderIdAccountHolderModel;
   private OlaStatusModel statusIdStatusModel;

   //private Collection<LedgerModel> accountIdLedgerModelList = new ArrayList<LedgerModel>();

   private Long accountId;
   private String accountNumber;
   private String balance;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private OlaCustomerAccountTypeModel customerAccountTypeModel;
   private Long txLedgerId;

/**
    * Default constructor.
    */
   public AccountModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAccountId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAccountId(primaryKey);
    }
   
   /**
    * Returns the value of the <code>versionNo</code> property.
    *
    */
//      @Version 
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
    * Returns the value of the <code>accountId</code> property.
    *
    */
      @Column(name = "ACCOUNT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCOUNT_seq")
   public Long getAccountId() {
      return accountId;
   }

   /**
    * Sets the value of the <code>accountId</code> property.
    *
    * @param accountId the value for the <code>accountId</code> property
    *    
		    */

   public void setAccountId(Long accountId) {
      this.accountId = accountId;
   }

   
   
   
   /**
    * Returns the value of the <code>accountNumber</code> property.
    *
    */
      @Column(name = "ACCOUNT_NUMBER" , nullable = false )
   public String getAccountNumber() {
      return accountNumber;
   }

   /**
    * Sets the value of the <code>accountNumber</code> property.
    *
    * @param accountNumber the value for the <code>accountNumber</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAccountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
   }
   
   /**
    * Returns the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @return the value of the <code>statusIdStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "STATUS_ID")    
   public OlaStatusModel getRelationStatusIdStatusModel(){
      return statusIdStatusModel;
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
   @JoinColumn(name = "CUSTOMER_ACCOUNT_TYPE_ID")
   public OlaCustomerAccountTypeModel getCustomerAccountTypeModel() {
	return customerAccountTypeModel;
   }

   public void setCustomerAccountTypeModel(OlaCustomerAccountTypeModel customerAccountTypeModel) {
	   this.customerAccountTypeModel = customerAccountTypeModel;
   }   
   
   /**
    * Returns the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @return the value of the <code>statusIdStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public OlaStatusModel getStatusIdStatusModel(){
      return getRelationStatusIdStatusModel();
   }

   /**
    * Sets the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @param statusModel a value for <code>statusIdStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationStatusIdStatusModel(OlaStatusModel statusModel) {
      this.statusIdStatusModel = statusModel;
   }
   
   /**
    * Sets the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @param statusModel a value for <code>statusIdStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setStatusIdStatusModel(OlaStatusModel statusModel) {
      if(null != statusModel)
      {
      	setRelationStatusIdStatusModel((OlaStatusModel)statusModel.clone());
      }      
   }
   
   /**
    * Returns the value of the <code>statusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getStatusId() {
      if (statusIdStatusModel != null) {
         return statusIdStatusModel.getStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>statusId</code> property.
    *
    * @param statusId the value for the <code>statusId</code> property
															    * @spring.validator type="required"
			    */
   
   @javax.persistence.Transient
   public void setStatusId(Long statusId) {
      if(statusId == null)
      {      
      	statusIdStatusModel = null;
      }
      else
      {
        statusIdStatusModel = new OlaStatusModel();
      	statusIdStatusModel.setStatusId(statusId);
      }      
   }

   /**
    * Returns the value of the <code>balance</code> property.
    *
    */
      @Column(name = "BALANCE" , nullable = false, length = 255 )
   public String getBalance() {
      return balance;
   }

   /**
    * Sets the value of the <code>balance</code> property.
    *
    * @param balance the value for the <code>balance</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBalance(String balance) {
      this.balance = balance;
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
    * Returns the value of the <code>accountHolderIdAccountHolderModel</code> relation property.
    *
    * @return the value of the <code>accountHolderIdAccountHolderModel</code> relation property.
    *
    */
   
     
   
   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, 
		   fetch = FetchType.LAZY)
   @JoinColumn(name = "ACCOUNT_HOLDER_ID")
   @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                                      org.hibernate.annotations.CascadeType.PERSIST,
                                      org.hibernate.annotations.CascadeType.MERGE,
                                      org.hibernate.annotations.CascadeType.REFRESH})
   public AccountHolderModel getRelationAccountHolderIdAccountHolderModel(){
      return accountHolderIdAccountHolderModel;
   }
    
   /**
    * Returns the value of the <code>accountHolderIdAccountHolderModel</code> relation property.
    *
    * @return the value of the <code>accountHolderIdAccountHolderModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AccountHolderModel getAccountHolderIdAccountHolderModel(){
      return getRelationAccountHolderIdAccountHolderModel();
   }

   /**
    * Sets the value of the <code>accountHolderIdAccountHolderModel</code> relation property.
    *
    * @param accountHolderModel a value for <code>accountHolderIdAccountHolderModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAccountHolderIdAccountHolderModel(AccountHolderModel accountHolderModel) {
      this.accountHolderIdAccountHolderModel = accountHolderModel;
   }
   
   /**
    * Sets the value of the <code>accountHolderIdAccountHolderModel</code> relation property.
    *
    * @param accountHolderModel a value for <code>accountHolderIdAccountHolderModel</code>.
    */
   @javax.persistence.Transient
   public void setAccountHolderIdAccountHolderModel(AccountHolderModel accountHolderModel) {
      if(null != accountHolderModel)
      {
      	setRelationAccountHolderIdAccountHolderModel((AccountHolderModel)accountHolderModel.clone());
      }      
   }
   


//   /**
//    * Add the related LedgerModel to this one-to-many relation.
//    *
//    * @param ledgerModel object to be added.
//    */
//    
//   public void addAccountIdLedgerModel(LedgerModel ledgerModel) {
//      ledgerModel.setRelationAccountIdAccountModel(this);
//      accountIdLedgerModelList.add(ledgerModel);
//   }
//   
//   /**
//    * Remove the related LedgerModel to this one-to-many relation.
//    *
//    * @param ledgerModel object to be removed.
//    */
//   
//   public void removeAccountIdLedgerModel(LedgerModel ledgerModel) {      
//      ledgerModel.setRelationAccountIdAccountModel(null);
//      accountIdLedgerModelList.remove(ledgerModel);      
//   }

//   /**
//    * Get a list of related LedgerModel objects of the AccountModel object.
//    * These objects are in a bidirectional one-to-many relation by the AccountId member.
//    *
//    * @return Collection of LedgerModel objects.
//    *
//    */
//   
//   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAccountIdAccountModel")
//   @JoinColumn(name = "ACCOUNT_ID")
//   public Collection<LedgerModel> getAccountIdLedgerModelList() throws Exception {
//   		return accountIdLedgerModelList;
//   }
//
//
//   /**
//    * Set a list of LedgerModel related objects to the AccountModel object.
//    * These objects are in a bidirectional one-to-many relation by the AccountId member.
//    *
//    * @param ledgerModelList the list of related objects.
//    */
//    public void setAccountIdLedgerModelList(Collection<LedgerModel> ledgerModelList) throws Exception {
//		this.accountIdLedgerModelList = ledgerModelList;
//   }



   /**
    * Returns the value of the <code>accountHolderId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAccountHolderId() {
      if (accountHolderIdAccountHolderModel != null) {
         return accountHolderIdAccountHolderModel.getAccountHolderId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>accountHolderId</code> property.
    *
    * @param accountHolderId the value for the <code>accountHolderId</code> property
									    * @spring.validator type="required"
									    */
   
   @javax.persistence.Transient
   public void setAccountHolderId(Long accountHolderId) {
      if(accountHolderId == null)
      {      
      	accountHolderIdAccountHolderModel = null;
      }
      else
      {
        accountHolderIdAccountHolderModel = new AccountHolderModel();
      	accountHolderIdAccountHolderModel.setAccountHolderId(accountHolderId);
      }      
   }

   @Column(name = "TX_LEDGER_ID")
  	public Long getTxLedgerId() {
  		return txLedgerId;
  	}

  	public void setTxLedgerId(Long txLedgerId) {
  		this.txLedgerId = txLedgerId;
  	}    

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAccountId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&accountId=" + getAccountId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "accountId";
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
    	
    	associationModel.setClassName("StatusModel");
    	associationModel.setPropertyName("relationStatusIdStatusModel");   		
   		associationModel.setValue(getRelationStatusIdStatusModel());
   		
   		associationModelList.add(associationModel);
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AccountHolderModel");
    	associationModel.setPropertyName("relationAccountHolderIdAccountHolderModel");   		
   		associationModel.setValue(getRelationAccountHolderIdAccountHolderModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
