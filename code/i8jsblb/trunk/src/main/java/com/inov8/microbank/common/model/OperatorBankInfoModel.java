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

/**
 * The OperatorBankInfoModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="OperatorBankInfoModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "OPERATOR_BANK_INFO_seq",sequenceName = "OPERATOR_BANK_INFO_seq", allocationSize=1)
@Table(name = "OPERATOR_BANK_INFO")
public class OperatorBankInfoModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -4056946714881519669L;
private PaymentModeModel paymentModeIdPaymentModeModel;
   private OperatorModel operatorIdOperatorModel;
   private BankModel bankIdBankModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;


   private Long operatorBankInfoId;
   private String name;
   private String receivingAccountNo;
   private String payingAccountNo;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private String merchantCategory;

   /**
    * Default constructor.
    */
   public OperatorBankInfoModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getOperatorBankInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setOperatorBankInfoId(primaryKey);
    }

   /**
    * Returns the value of the <code>operatorBankInfoId</code> property.
    *
    */
      @Column(name = "OPERATOR_BANK_INFO_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OPERATOR_BANK_INFO_seq")
   public Long getOperatorBankInfoId() {
      return operatorBankInfoId;
   }

   /**
    * Sets the value of the <code>operatorBankInfoId</code> property.
    *
    * @param operatorBankInfoId the value for the <code>operatorBankInfoId</code> property
    *    
		    */

   public void setOperatorBankInfoId(Long operatorBankInfoId) {
      this.operatorBankInfoId = operatorBankInfoId;
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
    * Returns the value of the <code>receivingAccountNo</code> property.
    *
    */
      @Column(name = "RECEIVING_ACCOUNT_NO" , nullable = false , length=50 )
   public String getReceivingAccountNo() {
      return receivingAccountNo;
   }

   /**
    * Sets the value of the <code>receivingAccountNo</code> property.
    *
    * @param receivingAccountNo the value for the <code>receivingAccountNo</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setReceivingAccountNo(String receivingAccountNo) {
      this.receivingAccountNo = receivingAccountNo;
   }

   /**
    * Returns the value of the <code>payingAccountNo</code> property.
    *
    */
      @Column(name = "PAYING_ACCOUNT_NO" , nullable = false , length=50 )
   public String getPayingAccountNo() {
      return payingAccountNo;
   }

   /**
    * Sets the value of the <code>payingAccountNo</code> property.
    *
    * @param payingAccountNo the value for the <code>payingAccountNo</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPayingAccountNo(String payingAccountNo) {
      this.payingAccountNo = payingAccountNo;
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

   /**
    * Returns the value of the <code>merchantCategory</code> property.
    *
    */
      @Column(name = "MERCHANT_CATEGORY" , nullable = false , length=50 )
   public String getMerchantCategory() {
      return merchantCategory;
   }

   /**
    * Sets the value of the <code>merchantCategory</code> property.
    *
    * @param merchantCategory the value for the <code>merchantCategory</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMerchantCategory(String merchantCategory) {
      this.merchantCategory = merchantCategory;
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
    * Returns the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @return the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "OPERATOR_ID")    
   public OperatorModel getRelationOperatorIdOperatorModel(){
      return operatorIdOperatorModel;
   }
    
   /**
    * Returns the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @return the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public OperatorModel getOperatorIdOperatorModel(){
      return getRelationOperatorIdOperatorModel();
   }

   /**
    * Sets the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @param operatorModel a value for <code>operatorIdOperatorModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationOperatorIdOperatorModel(OperatorModel operatorModel) {
      this.operatorIdOperatorModel = operatorModel;
   }
   
   /**
    * Sets the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @param operatorModel a value for <code>operatorIdOperatorModel</code>.
    */
   @javax.persistence.Transient
   public void setOperatorIdOperatorModel(OperatorModel operatorModel) {
      if(null != operatorModel)
      {
      	setRelationOperatorIdOperatorModel((OperatorModel)operatorModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>bankIdBankModel</code> relation property.
    *
    * @return the value of the <code>bankIdBankModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "BANK_ID")    
   public BankModel getRelationBankIdBankModel(){
      return bankIdBankModel;
   }
    
   /**
    * Returns the value of the <code>bankIdBankModel</code> relation property.
    *
    * @return the value of the <code>bankIdBankModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public BankModel getBankIdBankModel(){
      return getRelationBankIdBankModel();
   }

   /**
    * Sets the value of the <code>bankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>bankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationBankIdBankModel(BankModel bankModel) {
      this.bankIdBankModel = bankModel;
   }
   
   /**
    * Sets the value of the <code>bankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>bankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setBankIdBankModel(BankModel bankModel) {
      if(null != bankModel)
      {
      	setRelationBankIdBankModel((BankModel)bankModel.clone());
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
																									    * @spring.validator type="required"
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
    * Returns the value of the <code>operatorId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getOperatorId() {
      if (operatorIdOperatorModel != null) {
         return operatorIdOperatorModel.getOperatorId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>operatorId</code> property.
    *
    * @param operatorId the value for the <code>operatorId</code> property
					    * @spring.validator type="required"
																									    */
   
   @javax.persistence.Transient
   public void setOperatorId(Long operatorId) {
      if(operatorId == null)
      {      
      	operatorIdOperatorModel = null;
      }
      else
      {
        operatorIdOperatorModel = new OperatorModel();
      	operatorIdOperatorModel.setOperatorId(operatorId);
      }      
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getBankId() {
      if (bankIdBankModel != null) {
         return bankIdBankModel.getBankId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
							    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setBankId(Long bankId) {
      if(bankId == null)
      {      
      	bankIdBankModel = null;
      }
      else
      {
        bankIdBankModel = new BankModel();
      	bankIdBankModel.setBankId(bankId);
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
        checkBox += "_"+ getOperatorBankInfoId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&operatorBankInfoId=" + getOperatorBankInfoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "operatorBankInfoId";
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
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("OperatorModel");
    	associationModel.setPropertyName("relationOperatorIdOperatorModel");   		
   		associationModel.setValue(getRelationOperatorIdOperatorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationBankIdBankModel");   		
   		associationModel.setValue(getRelationBankIdBankModel());
   		
   		associationModelList.add(associationModel);
   		
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
          
}
