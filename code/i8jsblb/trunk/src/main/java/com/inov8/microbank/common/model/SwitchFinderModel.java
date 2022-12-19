package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
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
 * The SwitchFinderModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SwitchFinderModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SWITCH_FINDER_seq",sequenceName = "SWITCH_FINDER_seq", allocationSize=1)
@Table(name = "SWITCH_FINDER")
public class SwitchFinderModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 4750628373025109226L;
private SwitchModel switchIdSwitchModel;
   private PaymentModeModel paymentModeIdPaymentModeModel;
   private BankModel bankIdBankModel;


   private Long switchFinderId;

   /**
    * Default constructor.
    */
   public SwitchFinderModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSwitchFinderId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSwitchFinderId(primaryKey);
    }

   /**
    * Returns the value of the <code>switchFinderId</code> property.
    *
    */
      @Column(name = "SWITCH_FINDER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SWITCH_FINDER_seq")
   public Long getSwitchFinderId() {
      return switchFinderId;
   }

   /**
    * Sets the value of the <code>switchFinderId</code> property.
    *
    * @param switchFinderId the value for the <code>switchFinderId</code> property
    *    
		    */

   public void setSwitchFinderId(Long switchFinderId) {
      this.switchFinderId = switchFinderId;
   }

   /**
    * Returns the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @return the value of the <code>switchIdSwitchModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SWITCH_ID")    
   public SwitchModel getRelationSwitchIdSwitchModel(){
      return switchIdSwitchModel;
   }
    
   /**
    * Returns the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @return the value of the <code>switchIdSwitchModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SwitchModel getSwitchIdSwitchModel(){
      return getRelationSwitchIdSwitchModel();
   }

   /**
    * Sets the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @param switchModel a value for <code>switchIdSwitchModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSwitchIdSwitchModel(SwitchModel switchModel) {
      this.switchIdSwitchModel = switchModel;
   }
   
   /**
    * Sets the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @param switchModel a value for <code>switchIdSwitchModel</code>.
    */
   @javax.persistence.Transient
   public void setSwitchIdSwitchModel(SwitchModel switchModel) {
      if(null != switchModel)
      {
      	setRelationSwitchIdSwitchModel((SwitchModel)switchModel.clone());
      }      
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
    * Returns the value of the <code>switchId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSwitchId() {
      if (switchIdSwitchModel != null) {
         return switchIdSwitchModel.getSwitchId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>switchId</code> property.
    *
    * @param switchId the value for the <code>switchId</code> property
									    * @spring.validator type="required"
			    */
   
   @javax.persistence.Transient
   public void setSwitchId(Long switchId) {
      if(switchId == null)
      {      
      	switchIdSwitchModel = null;
      }
      else
      {
        switchIdSwitchModel = new SwitchModel();
      	switchIdSwitchModel.setSwitchId(switchId);
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSwitchFinderId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&switchFinderId=" + getSwitchFinderId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "switchFinderId";
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
    	
    	associationModel.setClassName("SwitchModel");
    	associationModel.setPropertyName("relationSwitchIdSwitchModel");   		
   		associationModel.setValue(getRelationSwitchIdSwitchModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PaymentModeModel");
    	associationModel.setPropertyName("relationPaymentModeIdPaymentModeModel");   		
   		associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationBankIdBankModel");   		
   		associationModel.setValue(getRelationBankIdBankModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
