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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The SwitchModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SwitchModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SWITCH_seq",sequenceName = "SWITCH_seq", allocationSize=1)
@Table(name = "SWITCH")
public class SwitchModel extends BasePersistableModel implements Serializable {
  


   /**
	 * 
	 */
	private static final long	serialVersionUID	= 3243112342941589677L;
private Collection<SwitchFinderModel> switchIdSwitchFinderModelList = new ArrayList<SwitchFinderModel>();
   private Collection<SwitchUtilityMappingModel> switchIdSwitchUtilityMappingModelList = new ArrayList<SwitchUtilityMappingModel>();
   private Collection<TransactionModel> processingSwitchIdTransactionModelList = new ArrayList<TransactionModel>();

   private Long switchId;
   private String name;
   private String className;
   private String description;
   private Boolean active;
   private String userId;
   private String password;
   private String url;
   private Boolean cvvRequired;
   private Boolean tpinRequired;
   private Boolean mpinRequired;
   private String paymentGatewayCode;

   /**
    * Default constructor.
    */
   public SwitchModel() {
   }   

   public SwitchModel( Long switchId ) {
       this.switchId = switchId;
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSwitchId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSwitchId(primaryKey);
    }

   /**
    * Returns the value of the <code>switchId</code> property.
    *
    */
      @Column(name = "SWITCH_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SWITCH_seq")
   public Long getSwitchId() {
      return switchId;
   }

   /**
    * Sets the value of the <code>switchId</code> property.
    *
    * @param switchId the value for the <code>switchId</code> property
    *    
		    */

   public void setSwitchId(Long switchId) {
      this.switchId = switchId;
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
    * Returns the value of the <code>className</code> property.
    *
    */
      @Column(name = "CLASS_NAME" , nullable = false , length=250 )
   public String getClassName() {
      return className;
   }

   /**
    * Sets the value of the <code>className</code> property.
    *
    * @param className the value for the <code>className</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setClassName(String className) {
      this.className = className;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=250 )
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
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>userId</code> property.
    *
    */
      @Column(name = "USER_ID" , nullable = false , length=50 )
   public String getUserId() {
      return userId;
   }

   /**
    * Sets the value of the <code>userId</code> property.
    *
    * @param userId the value for the <code>userId</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Returns the value of the <code>password</code> property.
    *
    */
      @Column(name = "PASSWORD" , nullable = false , length=50 )
   public String getPassword() {
      return password;
   }

   /**
    * Sets the value of the <code>password</code> property.
    *
    * @param password the value for the <code>password</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * Returns the value of the <code>url</code> property.
    *
    */
      @Column(name = "URL"  , length=250 )
   public String getUrl() {
      return url;
   }

   /**
    * Sets the value of the <code>url</code> property.
    *
    * @param url the value for the <code>url</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setUrl(String url) {
      this.url = url;
   }

   /**
    * Returns the value of the <code>cvvRequired</code> property.
    *
    */
      @Column(name = "IS_CVV_REQUIRED" , nullable = false )
   public Boolean getCvvRequired() {
      return cvvRequired;
   }

   /**
    * Sets the value of the <code>cvvRequired</code> property.
    *
    * @param cvvRequired the value for the <code>cvvRequired</code> property
    *    
		    */

   public void setCvvRequired(Boolean cvvRequired) {
      this.cvvRequired = cvvRequired;
   }

   /**
    * Returns the value of the <code>tpinRequired</code> property.
    *
    */
      @Column(name = "IS_TPIN_REQUIRED" , nullable = false )
   public Boolean getTpinRequired() {
      return tpinRequired;
   }

   /**
    * Sets the value of the <code>tpinRequired</code> property.
    *
    * @param tpinRequired the value for the <code>tpinRequired</code> property
    *    
		    */

   public void setTpinRequired(Boolean tpinRequired) {
      this.tpinRequired = tpinRequired;
   }

   
   /**
    * Returns the value of the <code>mpinRequired</code> property.
    *
    */
      @Column(name = "IS_MPIN_REQUIRED" , nullable = false )
   public Boolean getMpinRequired() {
      return mpinRequired;
   }

   /**
    * Sets the value of the <code>mpinRequired</code> property.
    *
    * @param mpinRequired the value for the <code>mpinRequired</code> property
    *    
		    */

   public void setMpinRequired(Boolean mpinRequired) {
      this.mpinRequired = mpinRequired;
   }

   
   /**
    * Returns the value of the <code>paymentGatewayCode</code> property.
    *
    */
      @Column(name = "PAYMENT_GATEWAY_CODE"  , length=50 )
   public String getPaymentGatewayCode() {
      return paymentGatewayCode;
   }

   /**
    * Sets the value of the <code>paymentGatewayCode</code> property.
    *
    * @param paymentGatewayCode the value for the <code>paymentGatewayCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPaymentGatewayCode(String paymentGatewayCode) {
      this.paymentGatewayCode = paymentGatewayCode;
   }


   /**
    * Add the related SwitchFinderModel to this one-to-many relation.
    *
    * @param switchFinderModel object to be added.
    */
    
   public void addSwitchIdSwitchFinderModel(SwitchFinderModel switchFinderModel) {
      switchFinderModel.setRelationSwitchIdSwitchModel(this);
      switchIdSwitchFinderModelList.add(switchFinderModel);
   }
   
   /**
    * Remove the related SwitchFinderModel to this one-to-many relation.
    *
    * @param switchFinderModel object to be removed.
    */
   
   public void removeSwitchIdSwitchFinderModel(SwitchFinderModel switchFinderModel) {      
      switchFinderModel.setRelationSwitchIdSwitchModel(null);
      switchIdSwitchFinderModelList.remove(switchFinderModel);      
   }

   /**
    * Get a list of related SwitchFinderModel objects of the SwitchModel object.
    * These objects are in a bidirectional one-to-many relation by the SwitchId member.
    *
    * @return Collection of SwitchFinderModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSwitchIdSwitchModel")
   @JoinColumn(name = "SWITCH_ID")
   public Collection<SwitchFinderModel> getSwitchIdSwitchFinderModelList() throws Exception {
   		return switchIdSwitchFinderModelList;
   }


   /**
    * Set a list of SwitchFinderModel related objects to the SwitchModel object.
    * These objects are in a bidirectional one-to-many relation by the SwitchId member.
    *
    * @param switchFinderModelList the list of related objects.
    */
    public void setSwitchIdSwitchFinderModelList(Collection<SwitchFinderModel> switchFinderModelList) throws Exception {
		this.switchIdSwitchFinderModelList = switchFinderModelList;
   }


   /**
    * Get a list of related SwitchUtilityMappingModel objects of the SwitchModel object.
    * These objects are in a bidirectional one-to-many relation by the SwitchId member.
    *
    * @return Collection of SwitchUtilityMappingModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSwitchIdSwitchModel")
   @JoinColumn(name = "SWITCH_ID")
   public Collection<SwitchUtilityMappingModel> getSwitchIdSwitchUtilityMappingModelList() throws Exception {
   		return switchIdSwitchUtilityMappingModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addProcessingSwitchIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationProcessingSwitchIdSwitchModel(this);
      processingSwitchIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeProcessingSwitchIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationProcessingSwitchIdSwitchModel(null);
      processingSwitchIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the SwitchModel object.
    * These objects are in a bidirectional one-to-many relation by the ProcessingSwitchId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProcessingSwitchIdSwitchModel")
   @JoinColumn(name = "PROCESSING_SWITCH_ID")
   public Collection<TransactionModel> getProcessingSwitchIdTransactionModelList() throws Exception {
   		return processingSwitchIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the SwitchModel object.
    * These objects are in a bidirectional one-to-many relation by the ProcessingSwitchId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setProcessingSwitchIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.processingSwitchIdTransactionModelList = transactionModelList;
   }


   /**
    * Add the related SwitchUtilityMappingModel to this one-to-many relation.
    *
    * @param switchUtilityMappingModel object to be added.
    */
    
   public void addSwitchIdSwitchUtilityMappingModel(SwitchUtilityMappingModel switchUtilityMappingModel) {
      switchUtilityMappingModel.setRelationSwitchIdSwitchModel(this);
      switchIdSwitchUtilityMappingModelList.add(switchUtilityMappingModel);
   }
   
   /**
    * Remove the related SwitchUtilityMappingModel to this one-to-many relation.
    *
    * @param switchUtilityMappingModel object to be removed.
    */
   
   public void removeSwitchIdSwitchUtilityMappingModel(SwitchUtilityMappingModel switchUtilityMappingModel) {      
      switchUtilityMappingModel.setRelationSwitchIdSwitchModel(null);
      switchIdSwitchUtilityMappingModelList.remove(switchUtilityMappingModel);      
   }

   /**
    * Get a list of related SwitchUtilityMappingModel objects of the SwitchModel object.
    * These objects are in a bidirectional one-to-many relation by the SwitchId member.
    *
    * @return Collection of SwitchUtilityMappingModel objects.
    *
    */
   
   /**
    * Set a list of SwitchUtilityMappingModel related objects to the SwitchModel object.
    * These objects are in a bidirectional one-to-many relation by the SwitchId member.
    *
    * @param switchUtilityMappingModelList the list of related objects.
    */
    public void setSwitchIdSwitchUtilityMappingModelList(Collection<SwitchUtilityMappingModel> switchUtilityMappingModelList) throws Exception {
		this.switchIdSwitchUtilityMappingModelList = switchUtilityMappingModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSwitchId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&switchId=" + getSwitchId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "switchId";
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
    	
    	    	
    	return associationModelList;
    }    
          
}
