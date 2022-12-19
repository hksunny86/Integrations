package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AccountTypeModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AccountTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACCOUNT_TYPE_seq",sequenceName = "ACCOUNT_TYPE_seq",allocationSize=1)
@Table(name = "ACCOUNT_TYPE")
public class AccountTypeModel extends BasePersistableModel implements Serializable {
  

   private PaymentModeModel paymentModeIdPaymentModeModel;

   private Collection<SmartMoneyAccountModel> accountTypeIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();

   private Long accountTypeId;
   private String name;
   private String accountTypeCode;
   private String description;

   /**
    * Default constructor.
    */
   public AccountTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAccountTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAccountTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>accountTypeId</code> property.
    *
    */
      @Column(name = "ACCOUNT_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCOUNT_TYPE_seq")
   public Long getAccountTypeId() {
      return accountTypeId;
   }

   /**
    * Sets the value of the <code>accountTypeId</code> property.
    *
    * @param accountTypeId the value for the <code>accountTypeId</code> property
    *    
		    */

   public void setAccountTypeId(Long accountTypeId) {
      this.accountTypeId = accountTypeId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the value of the <code>accountTypeCode</code> property.
    *
    */
      @Column(name = "ACCOUNT_TYPE_CODE" , nullable = false , length=50 )
   public String getAccountTypeCode() {
      return accountTypeCode;
   }

   /**
    * Sets the value of the <code>accountTypeCode</code> property.
    *
    * @param accountTypeCode the value for the <code>accountTypeCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountTypeCode(String accountTypeCode) {
      this.accountTypeCode = accountTypeCode;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=255 )
   public String getDescription() {
      return description;
   }

   /**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PAYMENT_MODE_ID")    
   public PaymentModeModel getRelationPaymentModeIdPaymentModeModel(){
      return paymentModeIdPaymentModeModel;
   }
    
   /**
    * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public PaymentModeModel getPaymentModeIdPaymentModeModel(){
      return getRelationPaymentModeIdPaymentModeModel();
   }

   /**
    * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPaymentModeIdPaymentModeModel(PaymentModeModel paymentModeModel) {
      this.paymentModeIdPaymentModeModel = paymentModeModel;
   }
   
   /**
    * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
    */
   @javax.persistence.Transient
   public void setPaymentModeIdPaymentModeModel(PaymentModeModel paymentModeModel) {
      if(null != paymentModeModel)
      {
      	setRelationPaymentModeIdPaymentModeModel((PaymentModeModel)paymentModeModel.clone());
      }      
   }
   


   /**
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */
    
   public void addAccountTypeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationAccountTypeIdAccountTypeModel(this);
      accountTypeIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }
   
   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */
   
   public void removeAccountTypeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {      
      smartMoneyAccountModel.setRelationAccountTypeIdAccountTypeModel(null);
      accountTypeIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);      
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the AccountTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AccountTypeId member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAccountTypeIdAccountTypeModel")
   @JoinColumn(name = "ACCOUNT_TYPE_ID")
   public Collection<SmartMoneyAccountModel> getAccountTypeIdSmartMoneyAccountModelList() throws Exception {
   		return accountTypeIdSmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the AccountTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AccountTypeId member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
    public void setAccountTypeIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
		this.accountTypeIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
   }



   /**
    * Returns the value of the <code>paymentModeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPaymentModeId() {
      if (paymentModeIdPaymentModeModel != null) {
         return paymentModeIdPaymentModeModel.getPaymentModeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>paymentModeId</code> property.
    *
    * @param paymentModeId the value for the <code>paymentModeId</code> property
													    */
   
   @javax.persistence.Transient
   public void setPaymentModeId(Long paymentModeId) {
      if(paymentModeId == null)
      {      
      	paymentModeIdPaymentModeModel = null;
      }
      else
      {
        paymentModeIdPaymentModeModel = new PaymentModeModel();
      	paymentModeIdPaymentModeModel.setPaymentModeId(paymentModeId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAccountTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&accountTypeId=" + getAccountTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "accountTypeId";
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
    	
    	associationModel.setClassName("PaymentModeModel");
    	associationModel.setPropertyName("relationPaymentModeIdPaymentModeModel");   		
   		associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
