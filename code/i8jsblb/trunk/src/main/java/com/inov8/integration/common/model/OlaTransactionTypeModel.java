package com.inov8.integration.common.model;

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
 * The TransactionTypeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "OLA_TRANSACTION_TYPE_seq",sequenceName = "OLA_TRANSACTION_TYPE_seq", allocationSize=1)
@Table(name = "OLA_TRANSACTION_TYPE")
public class OlaTransactionTypeModel extends BasePersistableModel implements Serializable {
  


   private Collection<LedgerModel> transactionTypeIdLedgerModelList = new ArrayList<LedgerModel>();
   private Collection<LimitModel> transactionTypeIdLimitModelList = new ArrayList<LimitModel>();

   private Long transactionTypeId;
   private String name;
   private String description;

   /**
    * Default constructor.
    */
   public OlaTransactionTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OLA_TRANSACTION_TYPE_seq")
   public Long getTransactionTypeId() {
      return transactionTypeId;
   }

   /**
    * Sets the value of the <code>transactionTypeId</code> property.
    *
    * @param transactionTypeId the value for the <code>transactionTypeId</code> property
    *    
		    */

   public void setTransactionTypeId(Long transactionTypeId) {
      this.transactionTypeId = transactionTypeId;
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
    * Add the related LedgerModel to this one-to-many relation.
    *
    * @param ledgerModel object to be added.
    */
    
   public void addTransactionTypeIdLedgerModel(LedgerModel ledgerModel) {
      ledgerModel.setRelationTransactionTypeIdTransactionTypeModel(this);
      transactionTypeIdLedgerModelList.add(ledgerModel);
   }
   
   /**
    * Remove the related LedgerModel to this one-to-many relation.
    *
    * @param ledgerModel object to be removed.
    */
   
   public void removeTransactionTypeIdLedgerModel(LedgerModel ledgerModel) {      
      ledgerModel.setRelationTransactionTypeIdTransactionTypeModel(null);
      transactionTypeIdLedgerModelList.remove(ledgerModel);      
   }

   /**
    * Get a list of related LedgerModel objects of the TransactionTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionTypeId member.
    *
    * @return Collection of LedgerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionTypeIdTransactionTypeModel")
   @JoinColumn(name = "TRANSACTION_TYPE_ID")
   public Collection<LedgerModel> getTransactionTypeIdLedgerModelList() throws Exception {
   		return transactionTypeIdLedgerModelList;
   }


   /**
    * Set a list of LedgerModel related objects to the TransactionTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionTypeId member.
    *
    * @param ledgerModelList the list of related objects.
    */
    public void setTransactionTypeIdLedgerModelList(Collection<LedgerModel> ledgerModelList) throws Exception {
		this.transactionTypeIdLedgerModelList = ledgerModelList;
   }


   /**
    * Add the related LimitModel to this one-to-many relation.
    *
    * @param limitModel object to be added.
    */
    
   public void addTransactionTypeIdLimitModel(LimitModel limitModel) {
      limitModel.setRelationTransactionTypeIdTransactionTypeModel(this);
      transactionTypeIdLimitModelList.add(limitModel);
   }
   
   /**
    * Remove the related LimitModel to this one-to-many relation.
    *
    * @param limitModel object to be removed.
    */
   
   public void removeTransactionTypeIdLimitModel(LimitModel limitModel) {      
      limitModel.setRelationTransactionTypeIdTransactionTypeModel(null);
      transactionTypeIdLimitModelList.remove(limitModel);      
   }

   /**
    * Get a list of related LimitModel objects of the TransactionTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionTypeId member.
    *
    * @return Collection of LimitModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionTypeIdTransactionTypeModel")
   @JoinColumn(name = "TRANSACTION_TYPE_ID")
   public Collection<LimitModel> getTransactionTypeIdLimitModelList() throws Exception {
   		return transactionTypeIdLimitModelList;
   }


   /**
    * Set a list of LimitModel related objects to the TransactionTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionTypeId member.
    *
    * @param limitModelList the list of related objects.
    */
    public void setTransactionTypeIdLimitModelList(Collection<LimitModel> limitModelList) throws Exception {
		this.transactionTypeIdLimitModelList = limitModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionTypeId=" + getTransactionTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionTypeId";
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
