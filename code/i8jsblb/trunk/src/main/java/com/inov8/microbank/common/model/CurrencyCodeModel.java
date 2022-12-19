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
 * The CurrencyCodeModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CurrencyCodeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CURRENCY_CODE_seq",sequenceName = "CURRENCY_CODE_seq", allocationSize=1)
@Table(name = "CURRENCY_CODE")
public class CurrencyCodeModel extends BasePersistableModel implements Serializable {
  


   private Collection<SmartMoneyAccountModel> currencyCodeIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();

   private Long currencyCodeId;
   private String name;
   private String currencyCode;
   private String description;

   /**
    * Default constructor.
    */
   public CurrencyCodeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCurrencyCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCurrencyCodeId(primaryKey);
    }

   /**
    * Returns the value of the <code>currencyCodeId</code> property.
    *
    */
      @Column(name = "CURRENCY_CODE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CURRENCY_CODE_seq")
   public Long getCurrencyCodeId() {
      return currencyCodeId;
   }

   /**
    * Sets the value of the <code>currencyCodeId</code> property.
    *
    * @param currencyCodeId the value for the <code>currencyCodeId</code> property
    *    
		    */

   public void setCurrencyCodeId(Long currencyCodeId) {
      this.currencyCodeId = currencyCodeId;
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
    * Returns the value of the <code>currencyCode</code> property.
    *
    */
      @Column(name = "CURRENCY_CODE" , nullable = false , length=50 )
   public String getCurrencyCode() {
      return currencyCode;
   }

   /**
    * Sets the value of the <code>currencyCode</code> property.
    *
    * @param currencyCode the value for the <code>currencyCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCurrencyCode(String currencyCode) {
      this.currencyCode = currencyCode;
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
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */
    
   public void addCurrencyCodeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationCurrencyCodeIdCurrencyCodeModel(this);
      currencyCodeIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }
   
   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */
   
   public void removeCurrencyCodeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {      
      smartMoneyAccountModel.setRelationCurrencyCodeIdCurrencyCodeModel(null);
      currencyCodeIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);      
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the CurrencyCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the CurrencyCodeId member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCurrencyCodeIdCurrencyCodeModel")
   @JoinColumn(name = "CURRENCY_CODE_ID")
   public Collection<SmartMoneyAccountModel> getCurrencyCodeIdSmartMoneyAccountModelList() throws Exception {
   		return currencyCodeIdSmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the CurrencyCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the CurrencyCodeId member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
    public void setCurrencyCodeIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
		this.currencyCodeIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCurrencyCodeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&currencyCodeId=" + getCurrencyCodeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "currencyCodeId";
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
